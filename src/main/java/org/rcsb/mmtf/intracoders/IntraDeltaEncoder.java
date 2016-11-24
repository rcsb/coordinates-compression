/**
 * 
 */
package org.rcsb.mmtf.intracoders;
import java.io.Serializable;

/**
 * @author Yana Valasatava
 *
 */
public class IntraDeltaEncoder implements IntegerTransform, Serializable {
	
	private static final long serialVersionUID = 3288976931115854494L;

	@Override
	public int[] run(int[] in) {
		
		int[] out = new int[in.length];
		
		out[0] = in[0];
		int prev = in[0];
	    for (int i = 1; i < in.length; i++) {
			out[i] = in[i] - prev;
			prev = in[i];
	    }
		return out;	
	}
}
