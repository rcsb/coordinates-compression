package org.rcsb.mmtf.utils;

import org.biojava.nbio.core.exceptions.CompoundNotFoundException;
import org.rcsb.mmtf.api.StructureDataInterface;
import org.rcsb.mmtf.dataholders.Entity;
import org.rcsb.mmtf.dataholders.MmtfStructure;
import org.rcsb.mmtf.decoder.GenericDecoder;
import org.rcsb.mmtf.decoders.MmtfCustomDecoder;

/**
 * An utilities class to work with MMTF data.
 * 
 * @author Yana Valasatava
 *
 */
public class MmtfUtils {

	public static int getNumOfPolymerChains(MmtfStructure mmtfData) throws CompoundNotFoundException {
		
		StructureDataInterface dataApi = new GenericDecoder(mmtfData);
		
		int[] chainsPerModel = dataApi.getChainsPerModel();
		int nChains = chainsPerModel[0];	
		int chainAddedInd = 0;
		for (int cId=0; cId < nChains; cId++) {
			String type = MmtfUtils.getType(dataApi, cId);
			if ( type.equals("polymer") ) {
				chainAddedInd++;
			}
		}
		return chainAddedInd;
	}
	
	public static boolean hasAllIdenticalSubunits(MmtfStructure mmtfData) throws CompoundNotFoundException {
		
		StructureDataInterface dataApi = new GenericDecoder(mmtfData);
		
		int entityChainsCount = 0;
		Entity[] entities = mmtfData.getEntityList();
		for (Entity entity : entities) {
			if ( entity.getType().equals("polymer") ) {
				entityChainsCount++;
			}
		}
		if (entityChainsCount > 1) { return false;}
		
		int polymerChainsCount = 0;
		int[] chainsPerModel = dataApi.getChainsPerModel();
		int nChains = chainsPerModel[0];
		for (int i=0; i< nChains; i++) {
			String type = MmtfUtils.getType(dataApi, i);
			if ( type.equals("polymer") ) {
				polymerChainsCount++;
			}
		}
		
		if (polymerChainsCount==1) { return false;}

		return true;
	}
	
	/**
	 * Check if the structure contains all identical chains.
	 * 
	 * @param mmtfData the input MMTF structure
	 * @return true if all chains are identical 
	 */
	public static boolean hasAllChainsIdentical(MmtfStructure mmtfData) throws CompoundNotFoundException {

		String[][] structure = MmtfCustomDecoder.decodeResolvedSequences(mmtfData);

		for (String[] chains : structure) {
			if ( chains.length < 2 ) { return false; }
			int lenght = chains[0].length();
			for (String chain : chains) {
				if (chain.equals("") || chain.replace("X", "").equals("")) { return false; }
				if ( chain.length()!=lenght ) { return false; }
			}

			int n = chains.length;
			String seq1 = chains[0];

			for (int i=1; i < n; i++) {

				String seq2 = chains[i];
				if ( ! seq1.equals(seq2) ) { return false; }
			}
		}

		return true;
	}

	/**
	 * Get the type of a given chain index.
	 * @param structureDataInterface the input {@link StructureDataInterface}
	 * @param chainInd the index of the relevant chain
	 * @return the {@link String} describing the chain 
	 */
	public static String getType(StructureDataInterface structureDataInterface, int chainInd) {
		for(int i=0; i<structureDataInterface.getNumEntities(); i++){
			for(int chainIndex : structureDataInterface.getEntityChainIndexList(i)){
				if(chainInd==chainIndex){
					return structureDataInterface.getEntityType(i);
				}
			}
		}
		System.err.println("ERROR FINDING ENTITY FOR CHAIN: "+chainInd);
		return "NULL";
	}
}
