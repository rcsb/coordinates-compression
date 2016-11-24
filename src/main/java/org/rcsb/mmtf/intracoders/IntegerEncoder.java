/**
 * 
 */
package org.rcsb.mmtf.intracoders;

import java.io.Serializable;

/**
 * @author Yana Valasatava
 *
 */
public class IntegerEncoder implements DoubleTransform, Serializable {
	
	private static final long serialVersionUID = -6009462688287727826L;
	
	private double mult;
	public IntegerEncoder(double mult) {
		this.mult = mult;
	}
	
	@Override
 	public int[] run(double[] in) {
		
 		int n = in.length;
		int[] out = new int[n];
		for (int i=0; i < n; i++) {
			out[i] = (int) Math.round(in[i] * mult);
		}
		return out;
	}
}
