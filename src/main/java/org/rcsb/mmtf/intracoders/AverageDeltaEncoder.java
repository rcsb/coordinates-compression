package org.rcsb.mmtf.intracoders;

public class AverageDeltaEncoder {

	public int[] run(int[] in) {
		
		int d = 0;
		for (int i=1; i < in.length; i++) {
			d += (in[i]-in[i-1]);
		}
		int avrgD = (int) Math.ceil((double)d/in.length);

		int[] out = new int[in.length+1];
		out[0] = avrgD;
		for (int i=1; i < in.length; i++) {
			out[i+1] = avrgD-(in[i]-in[i-1]);
		}
		return out;
	}
}
