/**
 * 
 */
package org.rcsb.mmtf.encoders;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.rcsb.mmtf.intercoders.InterErrorEncoder;
import org.rcsb.mmtf.utils.ArrayUtils;
import org.rcsb.mmtf.utils.Convertors;

/**
 * @author Yana Valasatava
 *
 */
public class TestInterErrorEncoder {

	@Test
	public void TestEncodeInterErrors() {
		
		ArrayUtils util = new ArrayUtils();
		
		List<Integer> inArray1 = Arrays.asList(6,8,5,7,9,20);
		int[] in1 = Convertors.listIntegerToPrimitives(inArray1);
		
		List<Integer> inArray2 = Arrays.asList(5,9,10,15,8,2);
		int[] in2 = Convertors.listIntegerToPrimitives(inArray2);
		
		List<Integer> outArray = Arrays.asList(-1,2,4,3,-9,-17);
		int[] out = Convertors.listIntegerToPrimitives(outArray);
		
		InterErrorEncoder encoder = new InterErrorEncoder();
		int[] test = encoder.run(in1, in2);
		
		for (int i=0; i < out.length; i++) {
			assertEquals(out[i], test[i]);
		}
	}
}
