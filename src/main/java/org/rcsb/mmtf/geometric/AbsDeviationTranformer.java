package org.rcsb.mmtf.geometric;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.vecmath.Point3d;

import org.biojava.nbio.structure.Atom;
import org.biojava.nbio.structure.Chain;
import org.biojava.nbio.structure.GroupType;
import org.biojava.nbio.structure.Structure;
import org.biojava.nbio.structure.StructureIO;
import org.biojava.nbio.structure.StructureTools;
import org.biojava.nbio.structure.align.util.AtomCache;
import org.biojava.nbio.structure.align.util.HTTPConnectionTools;
import org.biojava.nbio.structure.io.FileParsingParameters;
import org.biojava.nbio.structure.io.mmcif.ChemCompGroupFactory;
import org.biojava.nbio.structure.io.mmcif.DownloadChemCompProvider;

/**
 *
 */

public class AbsDeviationTranformer {
	private static AtomCache cache = initializeCache();
	private static String allUrl = "http://www.rcsb.org/pdb/rest/getCurrent/";

	/**
	 * 
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		AbsDeviationTranformer analyzer = new AbsDeviationTranformer();
		analyzer.run();
	}

	private void run() {
		StructureIO.setAtomCache(cache);
		long start = System.nanoTime();
		int failure = 0;
		int success = 0;
		int chains = 0;
//
//		 List<String> subset = new ArrayList<>(getAll());
//		 List<String> pdbIds = subset;
//		 pdbIds = subset.subList(30000, 35000);

		List<String> pdbIds = new ArrayList<>();
//		pdbIds.add("2N2M"); // NMR conserved
//		pdbIds.add("2N1B"); // NMR conserved
//		pdbIds.add("2N22"); // NMR conserved
//		pdbIds.add("2N1G"); // NMR conserved
//		pdbIds.add("1CFC"); // NMR not well aligned
//		pdbIds.add("1G11"); // NMR not  aligned
//		pdbIds.add("2MW5"); // NMR diverse
//		pdbIds.add("2K8P"); // NMR diverse
		pdbIds.add("2N6F"); // NMR diverse

        double oDev = 0;
        double aDev = 0;

		for (String pdbId : pdbIds) {
			System.out.println(pdbId);
			Structure s = null;
			try {
				s = StructureIO.getStructure(pdbId);
				success++;
			} catch (Exception e) {
				// some files can't be read. Let's just skip those!
				e.printStackTrace();
				failure++;
				continue;
			}
			if (s == null) {
				System.err.println("structure null: " + pdbId);
				continue;
			}

			List<Point3d[]> modelCoords = getModelCoordinates(s);
			System.out.println("Number of models: " + s.nrModels());
		
			for (int i = 1; i < modelCoords.size(); i++) {
				System.out.println("***** Model " + i + "*********");
				if (modelCoords.get(0).length != modelCoords.get(i).length) {
					continue;
				}
				double oAbsDev = AbsDev3dMinimizer.absoluteDeviation(modelCoords.get(0), modelCoords.get(i));	
				
				AbsDev3dMinimizer minimizer = new AbsDev3dMinimizer();
				minimizer.set(modelCoords.get(0), modelCoords.get(i));
				double pAbsDev = minimizer.getAbsoluteDeviation();
	
				System.out.println("absDev: " + oAbsDev + " -> " + pAbsDev);
				oDev += oAbsDev;
				aDev += pAbsDev;
				System.out.println();
			}
		}

		
		System.out.println("original deviation : " + oDev);
		System.out.println("minimized deviation: " + aDev);
		System.out.println("Success: " + success);
		System.out.println("Failure: " + failure);
		System.out.println("Chains: " + chains);
		System.out.println("Time: " + (System.nanoTime() - start) / 1E9
				+ " sec.");
	}
	
	private List<Point3d[]> getModelCoordinates(Structure s) {
		List<Point3d[]> modelCoords = new ArrayList<>();
		for (int i = 0; i < s.nrModels(); i++) {
			for (Chain c : s.getModel(i)) {
				if (StructureTools.getPredominantGroupType(c).equals(GroupType.AMINOACID)) {
					Atom[] atoms = StructureTools.getRepresentativeAtomArray(c);
					modelCoords.add(getCoordinates(atoms));
					break; // for now, use only first peptide chain
				}
			}
		}
		return modelCoords;
	}

	private static Point3d[] getCoordinates(Atom[] atoms) {
		Point3d[] coords = new Point3d[atoms.length];
		for (int i = 0; i < atoms.length; i++) {
			coords[i] = new Point3d(atoms[i].getCoords());
		}
		return coords;
	}

	/**
	 * Returns the current list of all PDB IDs.
	 * 
	 * @return representatives set of all PDB IDs.
	 */
	public static SortedSet<String> getAll() {
		SortedSet<String> representatives = new TreeSet<String>();
		try {

			URL u = new URL(allUrl);

			InputStream stream = HTTPConnectionTools.getInputStream(u, 60000);

			if (stream != null) {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(stream));

				String line = null;

				while ((line = reader.readLine()) != null) {
					int index = line.lastIndexOf("structureId=");
					if (index > 0) {
						representatives.add(line.substring(index + 13,
								index + 17));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return representatives;
	}

	/**
	 * 
	 * @return
	 */
	private static AtomCache initializeCache() {
		AtomCache cache = new AtomCache();
		cache.setUseMmCif(true);
		FileParsingParameters params = cache.getFileParsingParams();
		// params.setStoreEmptySeqRes(true);
		params.setAlignSeqRes(true);
		// params.setParseCAOnly(true);
		// params.setLoadChemCompInfo(true);
		params.setCreateAtomBonds(true);
		cache.setFileParsingParams(params);
		ChemCompGroupFactory
				.setChemCompProvider(new DownloadChemCompProvider());
		return cache;
	}
}