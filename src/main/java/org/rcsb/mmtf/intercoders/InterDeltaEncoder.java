/**
 * 
 */
package org.rcsb.mmtf.intercoders;

/**
 * @author Yana Valasatava
 *
 */
public class InterDeltaEncoder implements IntegerCrossTransform {
	
	@Override
	public int[] run(int[] in1, int[] in2) {
		
		int[] out = new int[in1.length];
		
		if (in1.length != in2.length) {
			return null;
		}
		for (int i=0; i < in1.length; i++) {
			out[i] = in2[i] - in1[i];
		}
		return out;
	}
}
