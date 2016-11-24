/**
 * 
 */
package org.rcsb.mmtf.encoders;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.rcsb.mmtf.intracoders.IntraErrorEncoder;
import org.rcsb.mmtf.utils.ArrayUtils;
import org.rcsb.mmtf.utils.Convertors;

/**
 * @author Yana Valasatava
 *
 */
public class TestIntraErrorEncoder {

	@Test
	public void run() {
		
		ArrayUtils util = new ArrayUtils();
		
		List<Integer> inArray = Arrays.asList(6,8,5,7,9,20);
		int[] in = Convertors.listIntegerToPrimitives(inArray);
		
		List<Integer> outArray = Arrays.asList(6,2,-5,5,0,9);
		int[] out = Convertors.listIntegerToPrimitives(outArray);
		
		IntraErrorEncoder encoder = new IntraErrorEncoder();
		int[] test = encoder.run(in);
		
		for (int i=0; i < out.length; i++) {
			assertEquals(out[i], test[i]);
		}
	}
	
}
