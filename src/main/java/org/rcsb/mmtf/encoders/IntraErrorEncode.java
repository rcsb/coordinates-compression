/**
 * 
 */
package org.rcsb.mmtf.encoders;

import java.io.Serializable;

import org.rcsb.mmtf.intracoders.IntegerTransform;
import org.rcsb.mmtf.intracoders.IntraErrorEncoder;
import org.rcsb.mmtf.utils.ArrayUtils;

/**
 * @author Yana Valasatava
 *
 */
public class IntraErrorEncode implements Serializable, IntraCoder {

	/**
	 * Runs intramolecular 
	 * 
	 */
	private static final long serialVersionUID = 5737488560196637140L;
	
	private String name;
	private IntegerTransform encoder;
	
	public IntraErrorEncode() {
		name = "intra error";
		encoder = new IntraErrorEncoder();
	}
	
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
