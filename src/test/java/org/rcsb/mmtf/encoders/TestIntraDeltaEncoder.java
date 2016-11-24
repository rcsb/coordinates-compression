/**
 * 
 */
package org.rcsb.mmtf.encoders;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.rcsb.mmtf.intracoders.IntraDeltaEncoder;
import org.rcsb.mmtf.utils.Convertors;

/**
 * @author Yana Valasatava
 *
 */
public class TestIntraDeltaEncoder {

	@Test
	public void TestEncodeIntraDelta() {

		// create arrays with values known to be correct
		List<Integer> inArray = Arrays.asList(5,9,10,15,12,1);
		int[] in = Convertors.listIntegerToPrimitives(inArray);
		
		List<Integer> outArray = Arrays.asList(5,4,1,5,-3,-11);
		int[] out = Convertors.listIntegerToPrimitives(outArray);
		
		// create a test array with method to be tested
		IntraDeltaEncoder encoder = new IntraDeltaEncoder();
		int[] test = encoder.run(in);
		
		for (int i=0; i < out.length; i++) {
			assertEquals(out[i], test[i]);
		}
	}
	
}
