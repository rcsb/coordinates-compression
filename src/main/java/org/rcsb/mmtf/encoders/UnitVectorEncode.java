package org.rcsb.mmtf.encoders;

import java.io.Serializable;

import org.rcsb.mmtf.compression.UnitVectorTransform;
import org.rcsb.mmtf.intracoders.IntegerTransform;
import org.rcsb.mmtf.intracoders.UnitVectorEncoder;

/**
 * 
 */
public class UnitVectorEncode implements Serializable, IntraCoder {


	private static final long serialVersionUID = -8572952084178488618L;
	
	private String name;
	private IntegerTransform encoder;
	
	public UnitVectorEncode(UnitVectorTransform uvt) {
		encoder = new UnitVectorEncoder(uvt);
		name = uvt.name();
	}

	@Override
	public int[] run(int[] coordinates) {
		return encoder.run(coordinates);
	}

	@Override
	public String name() {
		return name;
	}
}
