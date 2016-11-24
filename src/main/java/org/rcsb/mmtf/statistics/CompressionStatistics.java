package org.rcsb.mmtf.statistics;

import java.util.HashMap;
import java.util.Map;

public class CompressionStatistics {
	
	/**
	* Calculates the compression ratio (the amount of data required as a percentage of the size of the original data) 
	* as follows: Compression Ratio = (1/(Uncompressed Size)/(Compressed Size))*100%
	* 
	* @param uSize uncompressed Size.
	* @param cSize compressed Size.
	*
	* @return the compression ratio value (in % of uncompressed data)
	**/	
	public static double calculateRatio(int uSize, int cSize) {
		return 100 * ((double) cSize)/((double) uSize);
	}

	/**
	* Calculates savings as follow: savings % = 100 * (1 - (Compressed Size)/(Uncompressed Size))
	* more is better
	* @param uSize uncompressed Size.
	* @param cSize compressed Size.
	*
	* @return the value of saving (in %)
	**/
	public static double calculateSavings(int uSize, int cSize) {	
		return 100 * (1 - ( (double) cSize)/( (double) uSize));
	}
	
	/**
	* Counts how often each byte value appears in a range of bytes.
	*
	* @param data The input buffer.
	* @param start Index into the buffer where the counting starts.
	* @param length Number of bytes to count.
	*
	* @return Array with 256 entries that say how often each byte value appeared
	* in the requested input buffer range.
	**/
	private static int[] countByteDistribution(byte[] data, int start, int length)
	{
		final int[] countedData = new int[256];
		
		for (int i=start;i<start + length;i++) {
			
			countedData[data[i] & 0xFF]++;
		}	
		return countedData;
	}
	
	/**
	* Calculates the log2 of a value.
	**/
	private static double log2(double d)
	{
		return Math.log(d)/Math.log(2.0);
	}
	
	/**
	* Calculates the entropy of a sub-array.
	*
	* @param data The input data.
	* @param start Index into the input data buffer where the entropy calculation begins.
	* @param length Number of bytes to consider during entropy calculation.
	*
	* @return Entropy of the sub-array.
	**/
	public static double calculateEntropy(byte[] data, int start, int length)
	{
		double entropy = 0;
		
		final int[] countedData = countByteDistribution(data, start, length);
		
		for (int i=0;i<256;i++)
		{
			final double p_x = 1.0 * countedData[i] / length;
			
			if (p_x > 0)
			{
				entropy += -p_x * log2(p_x);
			}
		}	
		return entropy;
	}

	/**
	* Calculates the entropy of an array.
	* @param data The input sequence of bytes.
	* @return Entropy of the array.
	**/
	public static double calculateEntropyBytes(byte[] data)
	{
		double entropy = 0;
		final int[] countedData = countByteDistribution(data, 0, data.length);
		
		for (int i=0;i<256;i++)
		{
			final double p_x = 1.0 * countedData[i] / data.length;
			
			if (p_x > 0)
			{
				entropy += -p_x * log2(p_x);
			}
		}
		return entropy;
	}
	
	/**
	* Calculates the entropy of a byte array.
	* @param data The input sequence of bytes.
	* @return Entropy value.
	**/
	public static double calculateEntropyOnBytes(byte[] values) {
		
		Map<Byte, Integer> map = new HashMap<Byte, Integer>();
		
		// Calculate the entropy
		double result=0;

		// count the occurrences of each value
		for (byte value : values) {
			if (!map.containsKey(value)) {
				map.put(value, 0);
			}
			map.put(value, map.get(value) + 1);
		}
		result = 0.0;
		for (byte value : map.keySet()) {
			double frequency = (double) map.get(value) / values.length;
			result -= frequency * log2(frequency);
		}

		return result;
	}
	
	/**
	* Calculates the entropy of an array.
	* @param data The input sequence of integers.
	* @return Entropy value.
	**/
	public static double calculateEntropyOnIntegers(int[] values) {
		
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		
		// count the occurrences of each value
		for (int value : values) {
			if (!map.containsKey(value)) {
				map.put(value, 0);
			}
			map.put(value, map.get(value) + 1);
		}
		// Calculate the entropy
		double result = 0.0;
		for (int value : map.keySet()) {
			double frequency = (double) map.get(value) / values.length;
			result -= frequency * log2(frequency);
		}
		return result;
	}

	/**
	* Calculates the entropy of an array.
	* @param data The input sequence of floats.
	* @return Entropy value.
	**/
	public static double calculateEntropyOnFloats(float[] values) {
		
		Map<Float, Integer> map = new HashMap<Float, Integer>();
		
		// count the occurrences of each value
		for (float value : values) {
			if (!map.containsKey(value)) {
				map.put(value, 0);
			}
			map.put(value, map.get(value) + 1);
		}
		// Calculate the entropy
		double result = 0.0;
		for (float value : map.keySet()) {
			double frequency = (double) map.get(value) / values.length;
			result -= frequency * log2(frequency);
		}
		return result;
	}
}
