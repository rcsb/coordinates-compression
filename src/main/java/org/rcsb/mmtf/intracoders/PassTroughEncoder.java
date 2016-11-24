/**
 * 
 */
package org.rcsb.mmtf.intracoders;
import java.io.Serializable;

/**
 * @author Yana Valasatava
 *
 */
public class PassTroughEncoder implements IntegerTransform, Serializable {
	
	private static final long serialVersionUID = 3288976931115854494L;

	@Override
	public int[] run(int[] in) {
		return in;	
	}
}
