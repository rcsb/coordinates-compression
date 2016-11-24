package org.rcsb.mmtf.intracoders;

public class MedianAdaptivePredictionEncoder {
	
	public int[] run(int[] in) {
		
		int[] out = new int[in.length];
		out[0] = in[0];
		for (int i=1; i < in.length; i++) {
			out[i] = (in[i+1]-in[i-1])-in[i];
		}
		return out;
	}
}
