/**
 * 
 */
package org.rcsb.mmtf.intracoders;

import static org.junit.Assert.assertArrayEquals;

import org.junit.Test;
import org.rcsb.mmtf.intracoders.RecursiveIndexingCoder;

/**
 * Tests the encoding and decoding methods of RecursiveIndexingCoder.
 * 
 * @author Yana Valasatava
 *
 */
public class TestRecursiveIndexingCoder {

	/**
	 * Recursive Indexing encoder test.
	 */
	@Test
	public void recursiveIndexingEncodeTest() {
		
		int[] in = {10, 2, 3, 7, 0, 15, -8, -17};
		int[] outTest =  {7, 3, 2, 3, 7, 0, 0, 7, 7, 1, -8, 0, -8, -8, -1};
		RecursiveIndexingCoder coder = new RecursiveIndexingCoder(7);
		int[] out = coder.encode(in);
		assertArrayEquals(outTest, out);
	}
	
	/**
	 * Recursive Indexing encoder test on empty arrays.
	 */
	@Test
	public void emptyRecursiveIndexingEncodeTest() {
		// Allocate the byte array
		int[] in = {};
		int[] outTest =  {};
		RecursiveIndexingCoder coder = new RecursiveIndexingCoder(7);
		int[] out = coder.encode(in);
		assertArrayEquals(outTest, out);
	}
	
	/**
	 * Recursive Indexing decoder test.
	 */
	@Test
	public void recursiveIndexingDecodeTest() {
		
		int[] in =  {7, 3, 2, 3, 7, 0, 0, 7, 7, 1, -8, 0, -8, -8, -1};
		int[] outTest = {10, 2, 3, 7, 0, 15, -8, -17};
		RecursiveIndexingCoder coder = new RecursiveIndexingCoder(7);
		int[] out = coder.decode(in);
		assertArrayEquals(outTest, out);
	}
	
	/**
	 * Recursive Indexing decoder test.
	 */
	@Test
	public void emptyRecursiveIndexingDecodeTest() {
		
		int[] in =  {};
		int[] outTest = {};
		RecursiveIndexingCoder coder = new RecursiveIndexingCoder(7);
		int[] out = coder.decode(in);
		assertArrayEquals(outTest, out);
	}
}
