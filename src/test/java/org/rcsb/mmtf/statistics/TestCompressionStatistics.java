package org.rcsb.mmtf.statistics;

import static org.junit.Assert.*;

import org.junit.Test;


public class TestCompressionStatistics {
	
	@Test
	public void calculateEntropyOnIntegers() {
		
		int[] input = {1000, 2, 6, 2, -6};	
		double test = -((double) 1/5 * (Math.log((double) 1/5)/Math.log(2.0)) + 
						(double) 2/5 * (Math.log((double) 2/5)/Math.log(2.0)) +
						(double) 1/5 * (Math.log((double) 1/5)/Math.log(2.0)) +
						(double) 1/5 * (Math.log((double) 1/5)/Math.log(2.0)));
		
		double out = CompressionStatistics.calculateEntropyOnIntegers(input);
		
		assertEquals(test, out, 0.000000000000001);
	}
}
