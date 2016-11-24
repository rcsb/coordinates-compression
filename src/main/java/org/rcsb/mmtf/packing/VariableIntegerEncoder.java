package org.rcsb.mmtf.packing;

import java.io.ByteArrayOutputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;

public class VariableIntegerEncoder {
	
	public byte[] run(int[] data) throws IOException {
		
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		DataOutput dataout = new DataOutputStream(os);
		
		for (int i : data) {
			VariableWidthIntegerPacking.writeRawVarint32(dataout, i);
		}
		return os.toByteArray();
	}
}
