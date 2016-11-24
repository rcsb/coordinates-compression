/**
 * 
 */
package org.rcsb.mmtf.intracoders;

import java.io.Serializable;

/**
 * @author Yana Valasatava
 *
 */
public class IntraErrorEncoder implements IntegerTransform, Serializable {
	
	private static final long serialVersionUID = 4396862402167974464L;

	@Override
	public int[] run(int[] in) {
		
		int[] out = new int[in.length];
		
		// output array store 1st element as it is in input array, 2nd element as delta between 2nd and 1st elements in input array, and other elements as errors to predicted values
		
		int p1 = in[0];
		int p2 = in[1];
		int d = p2-p1;
		
		out[0] = p1;
		out[1] = d;
		
		for (int i=2; i<in.length; i++) {
			
			int pi = in[i];
			int predicted_p = p2 + d;
			int error = pi - predicted_p;
			out[i] = error;
			
			d=pi-p2;
			p2=pi;
		}
		return out;
	}
}
