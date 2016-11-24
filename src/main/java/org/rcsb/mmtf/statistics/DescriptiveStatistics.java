package org.rcsb.mmtf.statistics;

import java.io.Serializable;
import java.util.Arrays;

public class DescriptiveStatistics implements Serializable {

	private static final long serialVersionUID = 1956596736797461641L;
	
	private static int shortSizeP = 32767;
	private static int shortSizeN = -32768;
	private static int byteSizeP = 127;
	private static int byteSizeN = -128;
	
	public static int getPercentale(int[] input, double p){
		
		int[] copy = new int[input.length];
		System.arraycopy( input, 0, copy, 0, input.length );
		Arrays.sort(copy);
		
		return 0;
	}
	
	// Method for getting the maximum value
	public static int getMax(int[] inputArray){ 
		int maxValue = inputArray[0]; 
		for(int i=1; i < inputArray.length;i++){ 
			if(inputArray[i] > maxValue){ 
				maxValue = inputArray[i]; 
			} 
		} 
		return maxValue; 
	}

	// Method for getting the absolute maximum value
	public static int getMaxAbs(int[] inputArray){ 
		int maxValue = Math.abs(inputArray[0]); 
		for(int i=1; i<inputArray.length; i++){ 
			if(Math.abs(inputArray[i]) > maxValue){ 
				maxValue = Math.abs(inputArray[i]); 
			} 
		} 
		return maxValue; 
	}
	
	// Method for getting the minimum value
	public static int getMin(int[] inputArray){ 
		int minValue = inputArray[0]; 
		for(int i=1; i < inputArray.length; i++){ 
			if(inputArray[i] < minValue){ 
				minValue = inputArray[i]; 
			} 
		} 
		return minValue; 
	}

	// Method for getting the absolute minimum value
	public static int getMinAbs(int[] inputArray){ 
		int minValue = Math.abs(inputArray[0]); 
		for(int i=1;i<inputArray.length;i++){ 
			if(Math.abs(inputArray[i]) < minValue){ 
				minValue = Math.abs(inputArray[i]); 
			} 
		} 
		return minValue; 
	}
	// Method for getting the mean value
	public static double getMean(int[] inputArray){ 
		long sum = 0; 
		for(int i=0;i<inputArray.length;i++){ 
			sum += inputArray[i];
		}
		double mean = ((double) sum)/inputArray.length;
		return mean;
	}
	
	public static double getMedian(int[] inputArray){ 
		int[] in = new int[inputArray.length];
		for (int i=0; i < inputArray.length; i++) {
			in[i] = inputArray[i];
		}
		Arrays.sort(in);
		double median;
		if (in.length % 2 == 0)
		    median = ((double)in[in.length/2] + (double)in[in.length/2 - 1])/2;
		else
		    median = (double) in[in.length/2];
		return median;
	}
	
	public static float getShorts(int[] in) {
		long count = 0;
		for (int i : in) {
			if ( (i >= shortSizeN) && (i <= shortSizeP) )
				count++;
		}
		return ((float) 100*count/in.length);
	}

	public static float getBytes(int[] in) {
		long count = 0;
		for (int i : in) {
			if ( (i >= byteSizeN) && (i <= byteSizeP) )
				count++;
		}
		return ((float) 100*count/in.length);
	}
}
