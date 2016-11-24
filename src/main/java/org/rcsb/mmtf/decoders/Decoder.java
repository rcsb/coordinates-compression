/**
 * 
 */
package org.rcsb.mmtf.decoders;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Yana Valasatava
 *
 */
public class Decoder {
	
	// decode integers into doubles
	
	public List<Double> decodeIntegers(List<Integer> inArray) {
		
		List<Double> outArray = new ArrayList<Double>();
		double denom = 1000.0;
		
		for (Integer num : inArray) {
			double d = num * 1.0 / denom;
			outArray.add(d);
		}
		
		return outArray;
	}
	
	// decode INTRAmolecular deltas
	
	public List<Integer> decodeIntraDelta(List<Integer> inArray) {
		
		List<Integer> outArray = new ArrayList<Integer>();
		int d = inArray.get(0);
		outArray.add(d);
		
		for (int i=1; i<inArray.size(); i++) {
			int p = inArray.get(i)+d;
			outArray.add(p);
			d = p;
		}
		return outArray;
	}

	public List<Integer> decodeIntraErrors(List<Integer> inArray) {
		
		List<Integer> outArray = new ArrayList<Integer>();
		
		int p1 = inArray.get(0);
		int d = inArray.get(1);
		int p2 = p1+d;
		
		outArray.add(p1);
		outArray.add(p2);
		
		for(int i=2; i<inArray.size(); i++) {
			
			int error = inArray.get(i);
			int p = p2 + d + error;
			
			outArray.add(p);
			
			d = p - p2;
			p2 = p;
			
		}
		
		return outArray;
	}
	
	// decode INTERmolecular deltas
	
	public List<Integer> decodeInterDelta(List<Integer> inArray1, List<Integer> inArray2) {
		
		List<Integer> outArray = new ArrayList<Integer>();
		
		for(int i=0; i<inArray1.size(); i++) {
			outArray.add(inArray1.get(i)+inArray2.get(i));
		}
		
		return outArray;
	}
	
	public List<Integer> decodeInterErrors(List<Integer> inArray1, List<Integer> inArray2) {
		
		// inArray2 contains errors between outArray and values predicted from inArray1
		
		List<Integer> outArray = new ArrayList<Integer>();
		
		int p1 = inArray1.get(0);
		int d = inArray2.get(0);
		
		int p2 = p1+d;
		outArray.add(p2);
		
		for(int i=1; i<inArray1.size(); i++) {
			
			int error = inArray2.get(i);
			int pi = inArray1.get(i);
			
			p2 = error + pi + d;
			outArray.add(p2);
			
			d=p2-pi;
		}
		return outArray;
	}


}
