/**
 * 
 */
package org.rcsb.mmtf.compression;

import java.io.Serializable;

/**
 * The class Compressed3 contains methods to compress in/decompress from
 * 32-bit storage a 3D unit-length vector with 32-bit doubleing point *
 * components.
 *
 * The method implements algorithm described in `Compressed Unit Vectors'
 * by David Eberly. The algorithm is based on a sampling of angles that
 * define a unit vector in a polar coordinate system, which results in
 * approximately uniform distribution of samples on the unit sphere.
 *
 * A unit vector (x,y,z) is coded with three sign bits to determine which
 * octant the point is in and with a single 29-bit index of point sample
 * in the first octant. A triangular array of indices is used.
 *  
 * @author Egor Sobolev
 * @author adapted by Yana Valasatava
 */

public class CompressUnitVector32Bit implements UnitVectorTransform, Serializable {

	private static final long serialVersionUID = -2926260348355423586L;
	
	private static final int B = 29;
	private static final int N = (int) Math.floor(0.5*Math.sqrt(1+8*Math.pow(2, B))-1);
	private static final double piDivTwo = 0.5*Math.PI;
	private static final double twoDivPi = 2.0/Math.PI;
	private static final double factor = twoDivPi*(N-1);
	private static final double invFactor = 1.0/factor;

	/**
	 * Compresses a unit vector (x,y,z) in 32-bit storage.
	 *
	 * @param x	the x-coordinate of a 3D unit vector
	 * @param y	the y-coordinate of a 3D unit vector
	 * @param z	the z-coordinate of a 3D unit vector
	 * @return	a 32-bit compressed vector
	 */
	public int compress3b(double x, double y, double z)
	{
		int compressedValue = 0;
		if (x < 0.0) {
			compressedValue |= 0x80000000; // 32 bit
			x = -x;
		}
		if (y < 0.0) {
			compressedValue |= 0x40000000;
			y = -y;
		}
		if (z < 0.0) {
			compressedValue |= 0x20000000;
			z = -z;
		}

		// replaced asin to atan2 and fixed round to nearest integer
		int s = (int) (factor*Math.atan2(Math.sqrt(x*x+y*y), z) + 0.5);
		if (s > 0) {
			// fixed round to nearest integer
			int a = (int) (s*twoDivPi*Math.atan2(y,x) + 0.5);
			compressedValue |= (a + s*(s+1)/2);
		}

		return compressedValue;
	}

	/**
	 * Decompresses a unit vector (x,y,z) from 32-bit storage.
	 *
	 * @param compressedValue	the 32-bit compressed vector
	 * @return			a 3-cell array storing the decoded vector (x,y,z)
	 */
	public double[] decompress3b(int compressedValue)
	{
		int m = compressedValue & 0x1fffffff; // 32 bit
		int s = (int) (0.5*(Math.sqrt(1.0 + 8.0*m)-1.0));
		double[] v = new double[3];
		if (s > 0) {
			int a = m - s*(s+1)/2; // fixed, was: a = m - s;
			double theta = piDivTwo*a/s;
			double phi = invFactor*s;
			double sinPhi = Math.sin(phi); // fixed, was: sinPhi = sin(s);
			v[0] = Math.cos(theta)*sinPhi;
			v[1] = Math.sin(theta)*sinPhi;
			v[2] = Math.cos(phi);
		} else {
			v[0] = 0;
			v[1] = 0;
			v[2] = 1;
		}
		if ((compressedValue & 0x80000000) == 0x80000000) { v[0] = -v[0]; }
		if ((compressedValue & 0x40000000) == 0x40000000) { v[1] = -v[1]; }
		if ((compressedValue & 0x20000000) == 0x20000000) { v[2] = -v[2]; }

		return v;
	}

	public String name() {
		return "unit vector (32-bit)";
	}
}