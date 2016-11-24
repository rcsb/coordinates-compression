package org.rcsb.mmtf.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Point3d;

import org.biojava.nbio.structure.AminoAcid;
import org.biojava.nbio.structure.Atom;
import org.biojava.nbio.structure.Chain;
import org.biojava.nbio.structure.Group;
import org.biojava.nbio.structure.GroupType;
import org.biojava.nbio.structure.Structure;

import scala.Tuple2;



public class StructureUtils implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 948010827983816130L;
	
	public static boolean sameSize(List<Point3d[]> coordinates) {
		
		int len = coordinates.get(0).length;
		for (int i=1; i < coordinates.size(); i++) {
			int len_i = coordinates.get(i).length;
			if ( len_i != len)
				return false;
		}
		
		return true;
	}
	
	public static Point3d[][] arrayToChains (Point3d[][][] structure) {
		
		int n=0;
		for (Point3d[][] model : structure) {
			n+=model.length;
		}	
		Point3d[][] out = new Point3d[n][];
		
		int m=0;
		for (Point3d[][] model : structure) {
			for (Point3d[] chain : model) {
				int k=0;
				out[m] = new Point3d[chain.length];
				for (Point3d atom : chain) {
					out[m][k]=atom;
					k++;
				}
				m++;
			}
		}
		return out;
	}
	
	public static Point3d[][] arrayToModels (Point3d[][][] structure) {
		
		Point3d[][] out = new Point3d[structure.length][];
		
		int m=0;
		for (Point3d[][] model : structure) {
			int n=0;
			for (Point3d[] chain : model) {
				n+=chain.length;
			}
			out[m] = new Point3d[n];
			int i=0;
			for (Point3d[] chain : model) {
				for (Point3d atom : chain) {
					out[m][i]=atom;
					i++;
				}
			}
			m++;
		}
		return out;
	}
	
	public List<Atom> getChainAtoms(Chain chain, boolean caTrace) {
		
		List<Atom> atoms = new ArrayList<Atom>();
		List<Group> groups = chain.getAtomGroups(GroupType.AMINOACID);

		for (Group g : groups) {
			
			AminoAcid aa = (AminoAcid) g;
			if (caTrace) {
				Atom ca = aa.getCA();
				if (ca != null) {
					atoms.add(ca);
				}
			}
			else {
				atoms.addAll(aa.getAtoms());
			}	
		}
		
		return atoms;
	}
	
	public Point3d[] getAtomsCoordinates3d(List<Atom> atoms) {

		Point3d[] coordinates = new Point3d[atoms.size()];
		for (int i = 0; i < atoms.size(); i++) {
			coordinates[i] = new Point3d(atoms.get(i).getX(), atoms.get(i).getY(), atoms.get(i).getZ());
		}
		return coordinates;
	}
	
	public Point3d[] getChainCoordinates3d(Chain chain, boolean caTrace) {
		
		List<Atom> atoms = getChainAtoms(chain, caTrace);
		if (atoms.isEmpty())
			return new Point3d[0];
		Point3d[] coordinates = getAtomsCoordinates3d(atoms);
		
		return coordinates;
	}
	
	public Tuple2<String, List<Point3d[]>> getCoordinatesAsTuple2(Tuple2<String, Structure> t, boolean caTrace) {
		
		Tuple2<String, List<Point3d[]>> to = new Tuple2<String, List<Point3d[]>>(t._1, new ArrayList<Point3d[]>());

		List<Chain> chains = t._2.getChains();
		for (Chain chain : chains) {
			Point3d[] coords = getChainCoordinates3d(chain, caTrace);
			if (coords.length != 0)
				to._2.add(coords);
		}
		return to;
	}
}
