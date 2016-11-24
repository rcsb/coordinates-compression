/**
 * 
 */
package org.rcsb.mmtf.decoders;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.rcsb.mmtf.decoders.Decoder;

/**
 * @author Yana Valasatava
 *
 */
public class TestDecoder {

	@Test
	public void TestEncodeInteger() {
		
		List<Integer> inArray = Arrays.asList(5067,99965,10757,1593,120222,1);
		List<Double> outArray = Arrays.asList(5.067,99.965,10.757,1.593,120.222,0.001);

		Decoder decoder = new Decoder();
		List<Double> testArray = decoder.decodeIntegers(inArray);
		
		assertEquals(outArray, testArray);
	}
	
	@Test
	public void TestDecodeIntraDelta() {

		List<Integer> inArray1 = Arrays.asList(5,4,1,5,-3,-11);
		List<Integer> outArray = Arrays.asList(5,9,10,15,12,1);
		
		Decoder decoder = new Decoder();
		List<Integer> testArray = decoder.decodeIntraDelta(inArray1);
		assertEquals(outArray, testArray);
	}
	
	@Test
	public void TestDecodeIntraErrors() {
		
		List<Integer> inArray = Arrays.asList(6,2,-5,5,0,9);
		List<Integer> outArray = Arrays.asList(6,8,5,7,9,20);

		Decoder decoder = new Decoder();
		List<Integer> testArray = decoder.decodeIntraErrors(inArray);
		assertEquals(outArray, testArray);
	}
	
	@Test
	public void TestDecodeInterDelta() {

		List<Integer> inArray1 = Arrays.asList(6,8,5,7,9,20);
		List<Integer> inArray2 = Arrays.asList(-1,1,5,8,-1,-18);
		
		List<Integer> outArray = Arrays.asList(5,9,10,15,8,2);
		
		Decoder decoder = new Decoder();
		List<Integer> testArray = decoder.decodeInterDelta(inArray1, inArray2);
		assertEquals(outArray, testArray);
	}
	
	@Test
	public void TestDecodeInterErrors() {
		
		List<Integer> inArray1 = Arrays.asList(6,8,5,7,9,20);
		List<Integer> inArray2 = Arrays.asList(-1,2,4,3,-9,-17);
		
		List<Integer> outArray = Arrays.asList(5,9,10,15,8,2);
		
		Decoder decoder = new Decoder();
		List<Integer> testArray = decoder.decodeInterErrors(inArray1, inArray2);
		assertEquals(outArray, testArray);
	}

}
