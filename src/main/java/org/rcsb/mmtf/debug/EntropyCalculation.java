package org.rcsb.mmtf.debug;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;

import org.rcsb.mmtf.compression.GzipCompression;
import org.rcsb.mmtf.statistics.CompressionStatistics;

import java.nio.file.Path;

public class EntropyCalculation {
	
	public static void main(String[] args) throws IOException {
		testArray();
	}
	
	public static void testFile() throws IOException {
		
		Path path = Paths.get("/Users/yana/development/test_file.txt");
		byte[] data = Files.readAllBytes(path);
		
		System.out.println(data.length);
		double entropy = CompressionStatistics.calculateEntropyBytes(data);
		
		System.out.println(entropy);
		
		double s = (entropy*data.length)/8;
		System.out.println(s);
		
		int gzip = GzipCompression.compress(data).length;
		System.out.println(gzip);
	}
	
	public static void testArray() throws IOException {
		
		byte[] data = new byte[100];
		new Random().nextBytes(data);
		System.out.println(data.length);
		
		double entropy = CompressionStatistics.calculateEntropyBytes(data);
		
		double s = (Math.ceil(entropy)*data.length)/8;
		System.out.println(s);
		
		int gzip = GzipCompression.compress(data).length;
		System.out.println(gzip);
		
	}
}
