package org.rcsb.mmtf.analysis;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.spark.api.java.JavaPairRDD;
import org.rcsb.mmtf.codec.ArrayConverters;
import org.rcsb.mmtf.compression.CompressUnitVector16Bit;
import org.rcsb.mmtf.compression.CompressUnitVector32Bit;
import org.rcsb.mmtf.dataholders.MmtfStructure;
import org.rcsb.mmtf.encoders.IntegerEncode;
import org.rcsb.mmtf.encoders.IntraCoder;
import org.rcsb.mmtf.encoders.IntraDeltaEncode;
import org.rcsb.mmtf.encoders.IntraErrorEncode;
import org.rcsb.mmtf.encoders.LeGallWaveletEncode;
import org.rcsb.mmtf.encoders.UnitVectorEncode;
import org.rcsb.mmtf.mappers.FlatMapIntraEncodingMethods;
import org.rcsb.mmtf.mappers.MapMmtfStructureToCoordinates;
import org.rcsb.mmtf.mappers.MapToByteArray;
import org.rcsb.mmtf.methods.GetGzipSize;
import org.rcsb.mmtf.packing.RecursiveIndexingPacking;
import org.rcsb.mmtf.spark.data.MmtfStructureData;
import org.rcsb.mmtf.utils.ArrayUtils;
import org.rcsb.mmtf.utils.Convertors;
import org.rcsb.mmtf.utils.Writer;

import scala.Tuple2;

public class RunTotalAnalysis implements Serializable {

	/** 
	 * Run org.rcsb.mmtf.intracoders analysis over the entire PDB archive
	 * 
	 */

	private static final long serialVersionUID = -6015826264355327041L;

	public static void main(String[] args ) throws IOException {
		
		// path to a Hadoop sequence file with the PDB structures in MMTF format
		String path = args[0];
		// path to the folder with results
		String root = args[1];
		
		String compression;
		Writer w = new Writer();
		
		long start = System.nanoTime();
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		
		// IntraMolecular encoding strategies
		List<IntraCoder> methods = new ArrayList<IntraCoder>();
		methods.add(new IntegerEncode());
		methods.add(new IntraDeltaEncode());
		methods.add(new IntraErrorEncode());
		methods.add(new LeGallWaveletEncode());
		methods.add(new UnitVectorEncode(new CompressUnitVector16Bit()));
		methods.add(new UnitVectorEncode(new CompressUnitVector32Bit()));
		
		// Different integer encoding modes
		float[] multipliers = {(float) 1000.0, (float) 10.0};

		// Different data sets
		boolean[] caTraceFlags = {false, true};

		JavaPairRDD<String, MmtfStructure> mmtfEncoded = new MmtfStructureData(path).getJavaPairRdd()
						.cache();
		
		// == RUN ANALYSIS ==
		for (boolean caTrace : caTraceFlags) {
			
			String name;
			if (caTrace) {name = "pdb_reduced";}
			else {name = "pdb_full";}
			
			JavaPairRDD<String, float[][][]> coordinates = mmtfEncoded
					.mapToPair(new MapMmtfStructureToCoordinates(caTrace))
					.filter(t -> ArrayUtils.hasDataPoints(t._2, 3))
					.cache();
			
			// original coordinates
			
			JavaPairRDD<String, byte[]> original = coordinates
					.mapToPair(t -> new Tuple2<String, float[]>(t._1, Convertors.nestedArrayToFlat(t._2)))
					.mapToPair(t -> new Tuple2<String, float[]>("floating point;"+t._1, t._2))
					.mapToPair(t -> new Tuple2<String, byte[]>(t._1, Convertors.arrayToByteArray(t._2)))
					.cache();
			
			// == for LOSSLESS and LOSSY integer encoding ==
			for (float multiplier : multipliers) {
				
				if (multiplier == 1000.0) {compression = "lossless";}
				else {compression = "lossy";}
								
				JavaPairRDD<String, byte[]> encoded = coordinates
						.mapToPair(t -> new Tuple2<String, float[]>(t._1, Convertors.nestedArrayToFlat(t._2)))
						.mapToPair(t -> new Tuple2<String, int[]>(t._1, ArrayConverters.convertFloatsToInts(t._2, multiplier)))
						.flatMapToPair(new FlatMapIntraEncodingMethods(methods))
						.mapToPair(new RecursiveIndexingPacking())
						.mapToPair(new MapToByteArray());
				
				// == size ==

				JavaPairRDD<String, byte[]> data = original.union(encoded);

				
				List<Tuple2<String, Long>> size = data
						.mapToPair(new GetGzipSize())
						.collect();
				
				// == REPORT RESULTS ==
				
				String dir = root+"pdb"+"_"+timeStamp+"/";
				if (!(new File(dir).exists()))
					new File(dir).mkdir();
				
				String workDir = dir+compression+"/";
				if (!(new File(workDir).exists()))
					new File(workDir).mkdir();

				// report size of compressed data (gzip)
				String uri = workDir+name+"_size_GZIP.csv";
				w.writeLongToFile(size, uri);
			}
			coordinates.unpersist();
			original.unpersist();
		}
		System.out.println("DONE!");
		System.out.println("Time: " + (System.nanoTime() - start)/1E9 + " sec.");
		System.out.println("Job ID:"+timeStamp);
	}
}
