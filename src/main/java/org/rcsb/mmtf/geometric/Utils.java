package org.rcsb.mmtf.geometric;

public class Utils {
	
	public static void main(String[] args) {
		
//		double[] atom1 = {-29.772, 18.741, 8.906};
//		double[] atom2 = {-32.710, 20.719, 1.049};

		double[] atom1 = {-38.160,-1.231, -24.219};
		double[] atom2 = {-37.953, 8.011, -21.787};
		
		System.out.println(distance2points3D(atom2, atom1));
	}
	
	public static double distance2points3D(double[] p1, double[] p2) {
		
		double dx = p2[0]-p1[0];
		double dy = p2[1]-p1[1];
		double dz = p2[2]-p1[2];
		
		double dSq = dx*dx +dy*dy + dz*dz;
		return Math.sqrt(dSq);		
	}
}
