/**
 * 
 */
package org.rcsb.mmtf.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Yana Valasatava
 *
 */
public class SplitArray {
	
	private static int shortSizeP = 32767;
	private static int shortSizeN = -32768;
	private static int byteSizeP = 127;
	private static int byteSizeN = -128;
	
	public byte[][] packToBigAndShort(int[] in) throws IOException{
		
		byte[][] out = new byte[2][];
		if (in.length == 0)
			return out;
		
		int counter = 0;
		
		ByteArrayOutputStream littleOS = new ByteArrayOutputStream();
		DataOutputStream littleDOS = new DataOutputStream(littleOS);
		ByteArrayOutputStream bigOS = new ByteArrayOutputStream();
		DataOutputStream bigDOS = new DataOutputStream(bigOS);
		
		// First number goes in big list
		bigDOS.writeInt(in[0]);
		
		for(int i=1; i<in.length; i++){
			
			if ( (Math.abs(in[i]) >= shortSizeP) || ((Math.abs(in[i]) <= shortSizeN)) ) {
				// Counter added to the big list
				bigDOS.writeInt(counter);
				// Big number added to big list
				bigDOS.writeInt(in[i]);
				// Counter set to zero
				counter = 0;
			}
			else{
				// Little number added to little list
				littleDOS.writeShort(in[i]);
				// Add to the counter
				counter+=1;
			}
		}
		// Finally add the counter to the big list 
		bigDOS.writeInt(counter);

		out[0] = bigOS.toByteArray();
		out[1] = littleOS.toByteArray();
		return out;
	}

	public byte[][] packToBigShortSmallByteArray(List<Integer> in) throws IOException {
		
		byte[][] out = new byte[4][];
		
		ByteArrayOutputStream indOS = new ByteArrayOutputStream();
		DataOutputStream indDOS = new DataOutputStream(indOS);
		
		ByteArrayOutputStream byteOS = new ByteArrayOutputStream();
		DataOutputStream byteDOS = new DataOutputStream(byteOS);

		ByteArrayOutputStream shortOS = new ByteArrayOutputStream();
		DataOutputStream shortDOS = new DataOutputStream(shortOS);
		
		ByteArrayOutputStream bigOS = new ByteArrayOutputStream();
		DataOutputStream bigDOS = new DataOutputStream(bigOS);
		
		int curr = 0;
		int ind = 0;
		
		int counter = 0;
		
		// First element
		if ( (in.get(0) > shortSizeP) || (in.get(0) < shortSizeN) ) {
			
			bigDOS.writeInt(in.get(0));
			counter += 1;
			ind = 0;
		
		}
		
		else if ( ((in.get(0) > byteSizeP ) && ( in.get(0) <= shortSizeP )) || ((in.get(0) < byteSizeN ) && ( in.get(0) >= shortSizeN )) ) {
			
			shortDOS.writeShort(in.get(0));
			counter += 1;
			ind = 1;
		}
		
		else if (( in.get(0) <= byteSizeP ) && ( in.get(0) >= byteSizeN )) {
			
			byteDOS.writeByte(in.get(0));
			counter += 1;
			ind = 2;
		}
		
		// Other elements
		for ( int i=1; i < in.size(); i++ ) {
			
			if ( (in.get(i) > shortSizeP) || (in.get(i) < shortSizeN) ) {
				
				bigDOS.writeInt(in.get(i));
				curr = 0;	
			}
			
			else if ( ((in.get(i) > byteSizeP ) && ( in.get(i) <= shortSizeP )) || ((in.get(i) < byteSizeN ) && ( in.get(i) >= shortSizeN )) ) {	
				
				shortDOS.writeShort(in.get(i));
				curr = 1;
			}
			
			else if (( in.get(i) <= byteSizeP ) && ( in.get(i) >= byteSizeN )) {
				
				byteDOS.writeByte(in.get(i));
				curr = 2;
			}
			
			if ( curr != ind ) {
				
				indDOS.writeInt(ind);
				indDOS.writeInt(counter);
				
				counter = 1;
				ind = curr;	
			}
			else {
				counter += 1;
			}
		}
		
		indDOS.writeInt(ind);
		indDOS.writeInt(counter);
		
		out[0] = bigOS.toByteArray();
		out[1] = shortOS.toByteArray();
		out[2] = byteOS.toByteArray();
		out[3] = indOS.toByteArray();
		
		return out;
		
	}
	
	public ArrayList<Integer> unpackBigShortSmallByteArray(byte[] bigs, byte[] shorts, byte[] bytes, byte[] inds) throws IOException {

		DataInputStream bigStream = new DataInputStream(new ByteArrayInputStream(bigs));
		DataInputStream shortStream = new DataInputStream(new ByteArrayInputStream(shorts));
		DataInputStream byteStream = new DataInputStream(new ByteArrayInputStream(bytes));
		
		DataInputStream indsStream = new DataInputStream(new ByteArrayInputStream(inds));
		
		ArrayList<Integer> out = new ArrayList<Integer>();
		
		for ( int i=0; i < inds.length/8; i++) {
			
			int arrayId = indsStream.readInt();
			int numElems = indsStream.readInt();
			
			if ( arrayId == 0 ) {
				for ( int j=0; j < numElems; j++ ) {
					int elem = (int) bigStream.readInt();
					out.add(elem);
				}
			}
			else if ( arrayId == 1 ) {
				for ( int j=0; j < numElems; j++ ) {
					int elem = (int) shortStream.readShort();
					out.add(elem);	
				}
			}
			else if ( arrayId == 2 ) {
				for ( int j=0; j < numElems; j++ ) {
					int elem = (int) byteStream.readByte();
					out.add(elem);	
				}
			}
		}

		return out;
	}	
}
