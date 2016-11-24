/**
 * 
 */
package org.rcsb.mmtf.encoders;
import java.io.Serializable;

import org.rcsb.mmtf.intracoders.IntegerTransform;
import org.rcsb.mmtf.intracoders.IntraDeltaEncoder;
import org.rcsb.mmtf.utils.ArrayUtils;

/**
 * 
 * @author Yana Valasatava
 */
public class IntraDeltaEncode implements Serializable, IntraCoder {
	
	private static final long serialVersionUID = -278487811334002582L;
	
	private String name;
	private IntegerTransform encoder;
	
	public IntraDeltaEncode() {
		name = "intra delta";
		encoder = new IntraDeltaEncoder();
	}
	
	/**
	 * 
	 * @param coordinates the array of integer values to be encoded
	 */
	@Override
	public int[] run(int[] in) {
		
		int n = in.length/3; // number of atoms
		
		int[] x = new int[n];
		int[] y = new int[n];
		int[] z = new int[n];
		
		System.arraycopy(in, 0, x, 0, n);
		System.arraycopy(in, n, y, 0, n);
		System.arraycopy(in, n+n, z, 0, n);
		
		int[] xCoods = encoder.run(x);
		int[] yCoods = encoder.run(y);
		int[] zCoods = encoder.run(z);
		
		int[] out = ArrayUtils.mergeCoordinatesArrays(xCoods, yCoods, zCoods);
		return out;
	}

	
	@Override
	public String name() {
		return name;
	}
}
