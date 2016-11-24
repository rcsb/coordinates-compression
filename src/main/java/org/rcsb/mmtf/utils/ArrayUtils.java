/**
 * 
 */
package org.rcsb.mmtf.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Point3d;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;


/**
 * @author Yana Valasatava
 *
 */
public class ArrayUtils {
	
	public static int getMax(int[] in) {
		
		int[] val = getMinMax99(in);
		int minVal = val[0];
		int maxVal = val[1];
		int max;
		
		if ( minVal >= Byte.MIN_VALUE && maxVal <= Byte.MAX_VALUE) {max = Byte.MAX_VALUE; }
		else if (minVal >= Short.MIN_VALUE && maxVal <= Short.MAX_VALUE) {max = Short.MAX_VALUE; }
		else {max = Integer.MAX_VALUE;}
		
		return max;
	}
	
	public static int[] getMinMax99(int[] in) {
		
		DescriptiveStatistics statistics = new DescriptiveStatistics();
		for (int i : in) {
			statistics.addValue((double)i);
		}
		int maxVal = (int) Math.floor(statistics.getPercentile(99.0));
		int minVal = (int) Math.floor(statistics.getPercentile(1.0));
		
		return new int[]{minVal, maxVal};
	}
	
	public List<Integer> multiplyIntegerArrayBy(List<Integer> v, int m) {
		
		List<Integer> w = new ArrayList<Integer>();
		
		for (int i=0; i < v.size(); i++) {
			w.add(v.get(i) * m);
		}
		return w;
	}
	
	public static boolean floatArrayIsEmpty(float[][][] in) {
		
		for (float[][] fs : in) {
			for (float[] fs2 : fs) {
				if (fs2.length != 0)
					return false;
			}
		}
		return true;
	}
	
	public double[][] getDimentions(Point3d[] coordinates) {
		
		// array[number of arrays][how many elements in each of those arrays]
				
		// number of atoms
		int n = coordinates.length;
		
		// an array to store coordinates
		double[][] out =  new double[3][n];
		for (int i = 0; i < n; i++) {
			out[0][i] = coordinates[i].x;
			out[1][i] = coordinates[i].y;
			out[2][i] = coordinates[i].z;
		}
		return out;
	}

	public static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    BigDecimal bd = new BigDecimal(value);
	    bd = bd.setScale(places, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	}
	
	public void roundVectorPoint3d(Point3d[] v, int presicion) {
		
		for (Point3d p : v) {
			p.x=round(p.x, presicion);
			p.y=round(p.y, presicion);
			p.z=round(p.z, presicion);
		}
	}
	
	public Point3d[] multiplyVectorPoint3dBy(Point3d[] v, int m) {
		
		for (int i=0; i < v.length; i++) {
			v[i].x *= m;
			v[i].y *= m;
			v[i].z *= m;
		}
		return v;
	}
	
	public Point3d[] generateUnitVectorPoint3d() {
		
		Point3d[] v = new Point3d[2];
		v[0] = new Point3d(0.0, 0.0, 0.0);
		v[1] = new Point3d(1.0, 1.0, 1.0);
		return v;
	}
			
	public Point3d[] generateVectorPoint3d(int lenght) {
		
		double d1 = 1.0;
		double d2 = 2.0;
		double d3 = 3.0;

		Point3d[] v = new Point3d[lenght];
		
		int k = 1;
		for (int i=0; i < lenght; i++) {
			
			double[] pC = new double[3];
			pC[0] = d1*k;
			pC[1] = d2*k;
			pC[2] = d3*k;
			
			Point3d p = new Point3d();
			p.set(pC);
			v[i] = p;
			
			k +=1;
		}
		return v;
	}
	
	public static int[] mergeCoordinatesArrays(int[] xCoords, int[] yCoords, int[] zCoords) {
		
		int n = xCoords.length;
		int[] out = new int[3*n];
		
		int i = 0;
		for (int j=0; j < n; j++) {
			out[i] = xCoords[j];
			i++;
		}
		for (int j=0; j < n; j++) {
			out[i] = yCoords[j];
			i++;
		}
		for (int j=0; j < n; j++) {
			out[i] = zCoords[j];
			i++;
		}
		return out;
	}

	/**
	 * @param in
	 * @param k
	 * @return
	 */
	public static boolean hasDataPoints(float[][][] in, int k) {
		
		if (floatArrayIsEmpty(in))
			return false;
		
		for (float[][] fs : in) {
			for (float[] fp : fs) {
				if ( fp.length/3 < k ) {
					return false;
				}
			}
		}
		return true;
	}
	
	public static boolean sameSize(float[][][] input) {
		
		int len = 0;	
		for (float[][] in : input) {
			int currl = 0;
			for (float[] jn : in) {
				currl += jn.length;
			}	
			if ( len == 0 )
				len = currl;
			else if (len != currl)
				return false;
		}
		return true;
	}

	public static boolean sameModelLength(float[][][] input) {
		
		int len = 0;
		for (float[][] in : input) {
			int currl = 0;
			for (float[] jn : in) {
				currl += jn.length;
			if ( len == 0 )
				len = currl;
			else if (len != currl)
				return false;
			}
		}
		return true;
	}
	
	public static boolean sameChainLength(float[][][] input) {
		
		int len = 0;	
		for (float[][] in : input) {
			for (float[] jn : in) {
				int currl = jn.length;
				if ( len == 0 )
					len = currl;
				else if (len != currl)
					return false;
			}
		}
		return true;
	}

	public static boolean contains(char c, char[] array) {
	    for (char x : array) {
	        if (x == c) {
	            return true;
	        }
	    }
	    return false;
	}
}
