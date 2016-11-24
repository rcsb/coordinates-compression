/**
 * 
 */
package org.rcsb.mmtf.utils;

import java.nio.ByteBuffer;

/**
 * @author Yana Valasatava
 *
 */
public class ByteArrayConvertor {
	
	private int alloc; 
	private ByteArrayConvertor(int alloc) {
		this.alloc = alloc;
	}
	
	public static ByteArrayConvertor arrayOfInts () {
		return new ByteArrayConvertor(4);	
	}
	public static ByteArrayConvertor arrayOfShorts () {
		return new ByteArrayConvertor(2);	
	}
	public static ByteArrayConvertor arrayOfBytes () {
		return new ByteArrayConvertor(1);	
	}
	
	public byte[] convert(int[] input) {
		
		int index;
		int iterations = input.length;
		
		ByteBuffer bb = ByteBuffer.allocate(0);

		if ( this.alloc == 1 ) {
			bb = ByteBuffer.allocate(input.length);
			for(index = 0; index != iterations; ++index) {
				bb.put((byte) input[index]);   
			}
		}
		else if ( this.alloc == 2 ) {
			bb = ByteBuffer.allocate(input.length * 2);
			for(index = 0; index != iterations; ++index) {
				bb.putShort((short) input[index]);   
			}	
		}
		else {
			bb = ByteBuffer.allocate(input.length * 4);
			for(index = 0; index != iterations; ++index) {
				bb.putInt(input[index]);   
			}	
		}
		return bb.array();
	}
}
