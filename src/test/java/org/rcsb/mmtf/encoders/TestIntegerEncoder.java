/**
 * 
 */
package org.rcsb.mmtf.encoders;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.rcsb.mmtf.intracoders.IntegerEncoder;
import org.rcsb.mmtf.utils.Convertors;

/**
 * @author Yana Valasatava
 *
 */
public class TestIntegerEncoder {

	@Test
	public void run() {
		
		List<Double> inArray = Arrays.asList(5.067,99.965,10.757,1.593,120.222,0.001);
		double[] in = Convertors.listDoubleToPrimitives(inArray);
		
		List<Integer> outArray = Arrays.asList(5067,99965,10757,1593,120222,1);
		int[] out = Convertors.listIntegerToPrimitives(outArray);
		
		IntegerEncoder encoder = new IntegerEncoder(1000.0);
		int[] test = encoder.run(in);
		
		for (int i=0; i < out.length; i++) {
			assertEquals(out[i], test[i]);
		}
	}
}
