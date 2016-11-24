package org.rcsb.mmtf.intracoders;

import java.io.Serializable;

public class ZigZagEncoder implements IntegerTransform, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3553968172211004283L;
	
    int encodeInt32(int n) {
        return (n << 1) ^ (n >> 31);
    }
    
	@Override
	public int[] run(int[] data) {		
		int[] out = new int[data.length];
		for (int i = 0; i < data.length; ++i) {
			out[i] = encodeInt32(data[i]);
		}
		return out;
	}
}
