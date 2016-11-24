package org.rcsb.mmtf.intracoders;

import java.util.ArrayList;
import java.util.List;

/**
 * Recursive indexing is a simple method for representing a list of values by a moderate one so 
 * that values of a moderate list do not exceed a certain limit.
 *   
 * It the number to be represented lies between negative (NL) and positive (PL) limits, we represent it by the number itself.
 * If the number is bigger than PL (or smaller than NL), the number is represented by PL (or NL), the value of PL (or NL) is
 * subtracted from the original number, and the process is repeated till the number fits in the limits. 
 * 
 * @author Yana Valasatava
 */
public class RecursiveIndexingCoder {

	static int NL;
	static int PL;

	public RecursiveIndexingCoder(int max) {	
		// Sets a positive and negative limits for a reduced alphabet
		NL = -(max+1);
		PL = max;
	}

	/**
	 * Encodes an input array of integers following a Recursive Indexing strategy 
	 * (uses a reduced alphabet with fixed limits). 
	 * To encode a number N that exceed an upper limit of the reduced alphabet (Smax), 
	 * the encoder keeps reducing this number by subtracting (adding if N is negative) Smax from N,
	 * stopping when the number lies in the limits of a reduced alphabet.  
	 * 
	 * @param in the array of integer values to be encoded
	 * @return the encoded array
	 */

	public int[] encode(int[] in) {
		
		int div;
		int quotient;
		int remainder;
		
		List<Integer> tmp = new ArrayList<Integer>();
		
		for ( int i=0; i < in.length; i++ ) {	
			
			int num = in[i];
			if (num >= 0) { div = PL; }
			else { div = NL; }
			
			quotient = num / div;
			remainder = num % div;
			while (quotient > 0) {
				for (int j=0; j < quotient; j++) {
					tmp.add(div);
				}
				num = remainder;
				quotient = num / div;
			}
			tmp.add(num);
		}
		
		int[] out = new int[tmp.size()];
		for ( int j=0; j<tmp.size(); j++ ) {
			out[j]=tmp.get(j);
		}
		return out;	
	}
	/**
	 * Goes through an input array of integers and calculates the number of elements in encoded array
	 * 
	 * @param in the array of integer values to be decoded
	 * @return the number of elements to initialize the encoded array
	 */
	public static int calculateAllocationSize(int[] in) {
		
		int div;
		int quotient;
		int remainder;
		
		int n = 0;
		for (int num : in) {
			
			if (num >= 0) { div = PL; }
			else { div = NL; }
			
			while (true) {
				quotient = num / div;
				remainder = num % div;
				n += quotient;
				num = remainder;
				if ( quotient == 0 ) { n++; break; }
			}
		}
		return n;
	}
	
	/**
	 * Decodes an input array of integers following a Recursive Indexing strategy.
	 * To decode the array, the coder keeps adding all the elements of the index, 
	 * stopping when the index value is between (inclusive of ends) 
	 * the limits of the reduced alphabet.
	 * 
	 * @param in the array of integer values to be decoded
	 * @return the decoded array
	 */
	public int[] decode(int[] in) {

		int[] original = new int[in.length];

		int encodedInd = 0;
		int decodedInd = 0;

		while (encodedInd < in.length) {

			int decodedVal = 0;
			while (in[encodedInd] == PL || in[encodedInd] == NL) {
				decodedVal += in[encodedInd];
				encodedInd ++;
			}
			decodedVal += in[encodedInd];
			encodedInd ++;

			original[decodedInd] = decodedVal;
			decodedInd ++;
		}
		int[] out = new int[decodedInd];
		System.arraycopy(original, 0, out, 0, decodedInd);
		return out;
	}
}
