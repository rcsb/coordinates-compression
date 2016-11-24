/**
 * 
 */
package org.rcsb.mmtf.utils;

import java.nio.ByteBuffer;
import java.util.List;

import javax.vecmath.Point3d;

import org.rcsb.mmtf.codec.ArrayConverters;
import org.rcsb.mmtf.statistics.DescriptiveStatistics;

/**
 * 
 * A class of different converters.
 * 
 * @author Yana Valasatava
 *
 */
public class Convertors {

	/**
	 * Convert a float array to byte array, where each float is encoded by 4 bytes.
	 * @param input the input array of floats
	 * @return the byte array of the integers
	 */
	public static byte[] arrayToByteArray(float[] input) {

		int index;
		int iterations = input.length;

		ByteBuffer bb = ByteBuffer.allocate(input.length * 4);
		for(index = 0; index != iterations; ++index) {
			bb.putFloat(input[index]);   
		}
		return bb.array(); 
	}

	/**
	 * Convert an integer array to byte array, where each integer is encoded by 4 bytes.
	 * @param input the input array of integers
	 * @return the byte array of the integers
	 */
	public static byte[] arrayToByteArray(int[] input, int max) {

		int index;
		int iterations = input.length;
		
		ByteBuffer bb = ByteBuffer.allocate(0);
		
		int maxInputValue = DescriptiveStatistics.getMax(input);
		if (maxInputValue > max) {
			max=Integer.MAX_VALUE;
		}
		if ( max <= Byte.MAX_VALUE ) {
			bb = ByteBuffer.allocate(input.length);
			for(index = 0; index != iterations; ++index) {
				bb.put((byte) input[index]);   
			}			
		}
		else if ( max <= Short.MAX_VALUE ) {
			bb = ByteBuffer.allocate(input.length * 2);
			for(index = 0; index != iterations; ++index) {
				bb.putShort((short) input[index]);   
			}			
		}
		else {
			bb = ByteBuffer.allocate(input.length * 4);
			for(index = 0; index != iterations; ++index) {
				bb.putInt(input[index]);   
			}
		}
		return bb.array(); 
	}

	/**
	 * Takes a nested 3-dimensional integer array in[m][k][l] laid out as follow [[x_1...x_l y_1...y_l z_1...z_l]_1 ...[x_1...x_l y_1...y_l z_1...z_l]_k]_1
	 * and converts it to a flat array out[] = [x_1...x_mkl y_1...y_mkl z_1...z_mkl]
	 *  
	 * @param input the input 3-dimensional arrays of integers
	 * @return the output flat 1-dimensional array of integers
	 */
	public static int[] nestedArrayToFlat(int[][][] input) {

		int n=0; // accumulator to count all coordinates of all atoms
		for (int[][] is : input) {
			for (int[] js : is) {
				n += js.length;
			}
		}	
		int k = 0;
		int[] out = new int[n]; // Layout: out[n] = [x_1...x_mkl y_1...y_mkl z_1...z_mkl]
		int lenght = n/3; // number of all atoms

		for (int[][] is : input) {
			for (int[] js : is) {				
				int len = js.length/3; // number of atoms for m_th vector
				for (int l=0; l < js.length; l++) {
					out[k] = js[l]; // writing x coordinates to output
					out[k+lenght] = js[l+len]; // writing y coordinates to output
					out[k+lenght*2] = js[l+len*2]; // writing z coordinates to output
				}
			}
		}
		return out;
	}

	/**
	 * Takes a nested 3-dimensional float array in[m][k][l] laid out as follow [[x_1...x_l y_1...y_l z_1...z_l]_1 ...[x_1...x_l y_1...y_l z_1...z_l]_k]_1
	 * and converts it to a flat float array out[] = [x_1...x_mkl y_1...y_mkl z_1...z_mkl]
	 *  
	 * @param input the input 3-dimensional arrays of integers
	 * @return the output flat 1-dimensional array of integers
	 */
	public static float[] nestedArrayToFlat(float[][][] input) {

		int n=0; // accumulator to count all coordinates of all atoms
		for (float[][] is : input) {
			for (float[] js : is) {
				n += js.length;
			}
		}	
		int k = 0;
		float[] out = new float[n]; // Layout: out[n] = [x_1...x_mkl y_1...y_mkl z_1...z_mkl]
		int lenght = n/3; // number of all atoms

		for (float[][] is : input) {
			for (float[] js : is) {				
				int len = js.length/3; // number of atoms for m_th vector
				for (int l=0; l < len; l++) {
					out[k] = js[l]; // writing x coordinates to output
					out[k+lenght] = js[l+len]; // writing y coordinates to output
					out[k+(lenght*2)] = js[l+(len*2)]; // writing z coordinates to output
					k++;
				}
			}
		}
		return out;
	}

	/**
	 * Convert nested float array to nested Point3d arrays, where each float array  
	 * holds coordinates of PDB chain.
	 *  
	 * @param in the input nested arrays of integers
	 * @return the output nested arrays of Point3d
	 */
	public static Point3d[][][] arrayFloatToPoint3dArray(float[][][] structure) {

		Point3d[][][] out = new Point3d[structure.length][][];

		// iterate trough models 
		for (int i=0; i < structure.length; i++) {

			float[][] model = structure[i];
			out[i] = new Point3d[model.length][];

			// iterate trough chains
			for (int j=0; j < model.length; j++) {

				// iterate trough chain coordinates
				float[] chain = model[j];
				Point3d[] chain_atoms = arrayToPoint3dArray(chain);
				out[i][j] = chain_atoms;
			}
		}
		return out;
	}
	
	/**
	 * Convert nested integer arrays to nested Point3d arrays, where each integer array  
	 * holds coordinates of PDB chain.
	 *  
	 * @param in the input nested arrays of integers
	 * @return the output nested arrays of Point3d
	 */
	public static Point3d[][][] arrayIntegerToPoint3dArray(int[][][] structure) {

		Point3d[][][] out = new Point3d[structure.length][][];

		// iterate trough models 
		for (int i=0; i < structure.length; i++) {

			int[][] model = structure[i];
			out[i] = new Point3d[model.length][];

			// iterate trough chains
			for (int j=0; i < model.length; i++) {

				// iterate trough chain coordinates
				int[] chain = model[j];
				float[] chain_coords = ArrayConverters.convertIntsToFloats(chain, (float)1000.0);
				Point3d[] chain_atoms = arrayToPoint3dArray(chain_coords);
				out[i][j] = chain_atoms;
			}
		}

		return out;
	}

	/**
	 * Convert nested integer arrays to nested Point3d arrays, where each integer array  
	 * holds coordinates of PDB chain.
	 *  
	 * @param in the input nested arrays of integers
	 * @return the output nested arrays of Point3d
	 */
	public static int[][][] arrayFloatToInteger(float[][][] input, float multiplier) {

		int[][][] out = new int[input.length][][];

		// iterate trough models 
		for (int i=0; i < input.length; i++) {

			float[][] model = input[i];
			out[i] = new int[model.length][];

			// iterate trough chains
			for (int j=0; i < model.length; i++) {

				// iterate trough chain coordinates
				float[] chain = model[j];
				out[i][j] = ArrayConverters.convertFloatsToInts(chain, multiplier);
			}
		}

		return out;
	}
	/**
	 * Convert a float array to Point3d array, where each Point3d object  
	 * holds 3D coordinates of atom.
	 *  
	 * @param input the input array of integers
	 * @return the output array of Point3d
	 */
	public static Point3d[] arrayToPoint3dArray(float[] input) {

		int n = input.length/3;
		Point3d[] out = new Point3d[n];

		for (int i=0; i < n; i++) {

			float x = input[i];
			float y = input[n+i];
			float z = input[2*n+i];

			Point3d atom = new Point3d(x, y, z);
			out[i] = atom;
		}
		return out;
	}
	
	public static int[] listIntegerToPrimitives(List<Integer> input) {
		
		int[] out = new int[input.size()];
		for (int i=0; i<input.size(); i++) {
			out[i] = input.get(i);
		}
		return out;
	}

	public static double[] listDoubleToPrimitives(List<Double> input) {
		
		double[] out = new double[input.size()];
		for (int i=0; i<input.size(); i++) {
			out[i] = input.get(i);
		}
		return out;
	}
	
	public byte[] shortToByte(short [] input) {
		
	  int index;
	  int iterations = input.length;

	  ByteBuffer bb = ByteBuffer.allocate(input.length * 2);

	  for(index = 0; index != iterations; ++index) {
	    bb.putShort(input[index]);    
	  }

	  return bb.array();       
	}

	public byte[] intToByte(int [] input) {
		
		  int index;
		  int iterations = input.length;

		  ByteBuffer bb = ByteBuffer.allocate(input.length * 4);

		  for(index = 0; index != iterations; ++index) {
		    bb.putInt(input[index]);    
		  }

		  return bb.array();       
		}

	public byte[] floatToByte(float [] input) {
		
		  int index;
		  int iterations = input.length;

		  ByteBuffer bb = ByteBuffer.allocate(input.length * 4);

		  for(index = 0; index != iterations; ++index) {
		    bb.putFloat(input[index]);   
		  }

		  return bb.array();       
		}
	
	public byte[] doubleToByte(double [] input) {
		
		  int index;
		  int iterations = input.length;

		  ByteBuffer bb = ByteBuffer.allocate(input.length * 8);

		  for(index = 0; index != iterations; ++index) {
		    bb.putDouble(input[index]);   
		  }

		  return bb.array();       
		}
}
