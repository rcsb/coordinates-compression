/**
 * 
 */
package org.rcsb.mmtf.decoders;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.rcsb.mmtf.api.StructureDataInterface;
import org.rcsb.mmtf.codec.ArrayConverters;
import org.rcsb.mmtf.dataholders.Entity;
import org.rcsb.mmtf.dataholders.MmtfStructure;
import org.rcsb.mmtf.decoder.ArrayDecoders;
import org.rcsb.mmtf.decoder.GenericDecoder;
import org.rcsb.mmtf.utils.MmtfUtils;

/**
 * Custom decoding of MMTF fields.
 * 
 * @author Yana Valasatava
 *
 */
public class MmtfCustomDecoder {
	
	/**
	 * Decodes sequences of resolved residues. Filters out non-polymer chains and waters.
	 *  
	 * @param mmtfData The input MMTF structure.
	 */
	public static String[][] decodeFullSequences(MmtfStructure mmtfData) {
		
		String[][] structure = new String[1][];		
		Entity[] entities = mmtfData.getEntityList();
		int chainsAdded = 0;
		String[] arr = new String[entities.length];
		for (Entity entity : entities) {
			if ( entity.getType().equals("polymer") ) {
				String chain = entity.getSequence();
				chain = chain.replace("?", "X");
				arr[chainsAdded]=chain;
				chainsAdded++;
			}
		}
		
		String[] chains = new String[chainsAdded];
		System.arraycopy(arr, 0, chains, 0, chainsAdded);
		structure[0] = chains;

		return structure;
	}
	
	/**
	 * Decodes sequences of resolved residues. Filters out non-polymer chains and waters.
	 *  
	 * @param mmtfData The input MMTF structure.
	 */
	public static String[][] decodeResolvedSequences(MmtfStructure mmtfData) {
		
		StructureDataInterface dataApi = new GenericDecoder(mmtfData);
		
		int nModels = dataApi.getNumModels();
		String[][] structure = new String[nModels][];
		
		int[] chainsPerModel = dataApi.getChainsPerModel();
		int[] groupsPerChain = mmtfData.getGroupsPerChain();
		int[] groupTypeInds = dataApi.getGroupTypeIndices();
		
		// indices
		int chainInd = 0;
		int groupInd = 0;
		
		for (int mId=0; mId < nModels; mId++) {
			
			int nChains = chainsPerModel[mId];
			structure[mId] = new String[nChains];
			
			int chainAddedInd = 0;
			for (int cId=0; cId < nChains; cId++) {
				
				int numGroups = groupsPerChain[chainInd];
				String type = MmtfUtils.getType(dataApi, chainInd);
				
				if ( type.equals("polymer") ) {
					String chain = "";
					int gi = groupInd;
					for (int k=0; k < numGroups; k++) {
						chain += dataApi.getGroupSingleLetterCode(groupTypeInds[gi+k]);
					}
					chain = chain.replace("?", "X");
					structure[mId][chainAddedInd] = chain;
					chainAddedInd++;
				}
				chainInd++;
				groupInd+=numGroups;
			}
			// shrink the array with chains for a given model
			String[] arr = new String[chainAddedInd];
			System.arraycopy(structure[mId], 0, arr, 0, chainAddedInd);
			structure[mId] = arr;
		}
		return structure;
	}
	
	/**
	 * Decodes coordinates from MMTF structure as float arrays. Filters out non-polymer chains and waters.
	 * 
	 * Takes an input MMTF structure and converts it to arrays of coordinates in hierarchical representation, 
	 * e.g. [model1: [chain1: [x1...xn, y1...yn, z1...zn], chain2[...]], model2[...], ...].
	 *  
	 * @param mmtfData The input MMTF structure.
	 * @param caTrace The input flag for full (all atoms) or reduced (only carbon-alpha atoms) representations
	 * @throws IOException an error using a byte stream.
	 */
	public static float[][][] decodeCoordinates(MmtfStructure mmtfData, boolean caTrace) throws IOException {
		
		StructureDataInterface dataApi = new GenericDecoder(mmtfData);

		float[] xCoords = dataApi.getxCoords();
		float[] yCoords = dataApi.getyCoords();
		float[] zCoords = dataApi.getzCoords();

		// indices
		int chainInd = 0;
		int groupInd = 0;
		int atomInd = 0;

		int nModels = dataApi.getNumModels();
		float[][][] structure = new float[nModels][][];

		int[] chainsPerModel = dataApi.getChainsPerModel();
		int[] groupTypeInds = dataApi.getGroupTypeIndices();
		int[] groupsPerChain = mmtfData.getGroupsPerChain();
		
		char[] altLocIds = dataApi.getAltLocIds();
		
		for (int mId=0; mId < nModels; mId++) {

			int nChains = chainsPerModel[mId];
			structure[mId] = new float[nChains][];
			
			int chainAddedInd = 0;
			for (int cId=0; cId < nChains; cId++) {

				// count all atoms in a given chain
				int atomsPerChain = 0;
				int numGroups = groupsPerChain[chainInd];
				
				int gi = groupInd;
				for (int k=0; k < numGroups; k++) {				
					int atomsPerGroup = dataApi.getNumAtomsInGroup(groupTypeInds[gi]);
					atomsPerChain += atomsPerGroup;
					gi += 1;
				}
				
				List<Integer> atoms = new ArrayList<Integer>();
				String type = MmtfUtils.getType(dataApi, chainInd);
				
				// add atoms only from polymers
				if ( type.equals("polymer") ) {

					int gj = groupInd; // point the group counter to the index of the last group in the precedent chain
					int aj = atomInd; // point the atom counter to the index of the last atom in the precedent chain
					for (int k=0; k < numGroups; k++) {
						
						int atomsPerGroup = dataApi.getNumAtomsInGroup(groupTypeInds[gj]);
						String[] atomNames = dataApi.getGroupAtomNames(groupTypeInds[gj]);
						int added=0;
						String[] atomNamesAdded = new String[atomNames.length];
						
						for (int ai = 0; ai < atomsPerGroup; ai++ ) {
							char al = altLocIds[aj+ai];
							if (al != ' ') {
								if ( ArrayUtils.contains(atomNamesAdded, atomNames[ai]) ) { continue; }
							}
							if (caTrace) { 
								if ( dataApi.getGroupAtomNames(groupTypeInds[gj])[ai].equals("CA") || dataApi.getGroupAtomNames(groupTypeInds[gj])[ai].equals("P")) { 
									atoms.add(aj+ai); 
									} 
								}
							else { 
								atoms.add(aj+ai); 
								}
							
							atomNamesAdded[added] = atomNames[ai];
							added++;
						}
						gj++; // increment counter of a given group index
						aj+=atomsPerGroup; // shift atom counter to count all atoms in a group
					}				
				}
				// else shift the pointer for groups and atoms to "jump over" non-polymer and water chains
				groupInd += numGroups;
				atomInd += atomsPerChain;
				
				if ( !atoms.isEmpty() ) {
					
					int atomsAdded = atoms.size();
					float[] chain = new float[3*atomsAdded];
					
					// chain contains x, y, z coordinates as [x1...xn, y1...yn, z1...zn]
					for ( int q=0; q < atoms.size(); q++ ) {
						chain[q] = xCoords[atoms.get(q)];
						chain[atomsAdded+q] = yCoords[atoms.get(q)];
						chain[2*atomsAdded+q] = zCoords[atoms.get(q)];
					}		
					structure[mId][chainAddedInd] = chain;
					chainAddedInd ++;
				}
				chainInd++;
			}
			// shrink the array with chains for a given model
			float[][] arr = new float[chainAddedInd][];
			System.arraycopy(structure[mId], 0, arr, 0, chainAddedInd);
			structure[mId] = arr;
		}
		return structure;
	}

	/**
	 * Decodes coordinates from MMTF structure to integer arrays
	 * @param mmtfData The input mmtfBean structure.
	 * @throws IOException  an error using a byte stream.
	 */
	public static int[][][] decodeIntegerCoordinates(MmtfStructure mmtfData) throws IOException {

		int[][] coordinates = unpackCoordinates(mmtfData);

		StructureDataInterface dataApi = new GenericDecoder(mmtfData);

		int nModels = mmtfData.getChainsPerModel().length;
		int[] numChains = mmtfData.getChainsPerModel();
		int[] groupsPerChain = mmtfData.getGroupsPerChain();
		int[] groupInds = dataApi.getGroupTypeIndices();

		int gInd = 0;
		int gNumInd = 0;

		int aInd = 0;

		int[][][] structure = new int[nModels][][];

		// get each model in structure
		for (int i=0; i < nModels; i++) {

			structure[i] = new int[numChains[i]][];

			// get each chain in structure
			for (int j=0; j < numChains[i]; j++) {

				int atomsPerChain = 0;
				int numGroups = groupsPerChain[gNumInd];
				gNumInd ++;

				// count all atoms in a given chain
				for (int k=0; k < numGroups; k++) {
					atomsPerChain += dataApi.getNumAtomsInGroup(groupInds[gInd]); 
					gInd += 1;
				}

				structure[i][j] = new int[atomsPerChain*3];
				for (int m=0; m < atomsPerChain; m++) {

					structure[i][j][m] = coordinates[0][aInd];
					structure[i][j][atomsPerChain+m] = coordinates[1][aInd];
					structure[i][j][2*atomsPerChain+m] = coordinates[2][aInd];

					aInd += 1;
				}
			}			
		}

		return structure; 
	}

	/**
	 * Gets coordinates data from MmtfStructure and decodes it to arrays of integers.
	 * 
	 * @param mmtfData The input MmtfStructure data to be decoded.
	 * @throws IOException an error using a byte stream.
	 */
	public static int[][] unpackCoordinates(MmtfStructure mmtfData) throws IOException {

		// Convert byte arrays to integer arrays and combine arrays of small and big integers in one  
		int[] xCoordsEncoded = ArrayConverters.convertByteToIntegers(mmtfData.getxCoordList());
		int[] yCoordsEncoded = ArrayConverters.convertByteToIntegers(mmtfData.getyCoordList());
		int[] zCoordsEncoded = ArrayConverters.convertByteToIntegers(mmtfData.getzCoordList());

		// Decode delta encoded coordinate arrays to integer encoded coordinate arrays
		int[] xCoords = ArrayDecoders.deltaDecode(xCoordsEncoded);
		int[] yCoords = ArrayDecoders.deltaDecode(yCoordsEncoded);
		int[] zCoords = ArrayDecoders.deltaDecode(zCoordsEncoded);

		int[][] coordinates = new int[3][xCoords.length];
		coordinates[0] = xCoords;
		coordinates[1] = yCoords;
		coordinates[2] = zCoords;

		// Return coordinates as arrays of integers laid out as [[x1...xn][y1...yn][z1...zn]] 
		return coordinates;
	}
}