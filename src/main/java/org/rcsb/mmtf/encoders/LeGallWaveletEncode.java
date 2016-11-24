package org.rcsb.mmtf.encoders;

import java.io.Serializable;

import org.rcsb.mmtf.intracoders.IntegerTransform;
import org.rcsb.mmtf.intracoders.IntraDeltaEncoder;
import org.rcsb.mmtf.intracoders.LeGallWaveletEncoder;
import org.rcsb.mmtf.utils.ArrayUtils;

/**
 * 
 */

public class LeGallWaveletEncode implements Serializable, IntraCoder {

	private static final long serialVersionUID = -8572952084178488618L;
	
	String name;
	private IntegerTransform encoder;
	private IntegerTransform intra;
	
	public LeGallWaveletEncode() {
		name = "Le Gall wavelet";
		encoder = new LeGallWaveletEncoder();
		intra = new IntraDeltaEncoder();
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
		
		int[] xCoods = encoder.run(intra.run(x));
		int[] yCoods = encoder.run(intra.run(y));
		int[] zCoods = encoder.run(intra.run(z));
		
		int[] out = ArrayUtils.mergeCoordinatesArrays(xCoods, yCoods, zCoods);
		return out;
	}
	
	@Override
	public String name() {
		return name;
	}
}
