/**
 * 
 */
package org.rcsb.mmtf.intercoders;

/**
 * @author Yana Valasatava
 *
 */
public class InterErrorEncoder {

	public int[] run(int[] in1, int[] in2) {
		
		int[] out = new int[in1.length];
		
		//first element of encoded array contains actual delta between first elements of input arrays
		int p1 = in1[0];
		int p2 = in2[0];
		
		int d = p2 - p1;
		int error = 0;	
		out[0] = d;
		
		for(int i=1; i < in1.length; i++) {
			
			p1 = in1[i];
			p2 = in2[i];
			
			int predicted_p = p1 + d;
			error = p2 - predicted_p;
			
			out[i] = error;
			d = p2 - p1;
		}
		return out;
	}
}
