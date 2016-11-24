/**
 * 
 */
package org.rcsb.mmtf.analysis;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.vecmath.Point3d;

import org.apache.spark.api.java.JavaPairRDD;
import org.rcsb.mmtf.compression.GzipCompression;
import org.rcsb.mmtf.dataholders.MmtfStructure;
import org.rcsb.mmtf.encoders.InterCoder;
import org.rcsb.mmtf.encoders.InterDeltaEncode;
import org.rcsb.mmtf.encoders.InterDoubleDeltaEncode;
import org.rcsb.mmtf.encoders.InterErrorEncode;
import org.rcsb.mmtf.intracoders.IntegerEncoder;
import org.rcsb.mmtf.mappers.FlatMapIntraEncodingToCoordinatesBean;
import org.rcsb.mmtf.mappers.FlatMapTraverseStrategiesToCoordinates;
import org.rcsb.mmtf.mappers.MapArrayToChains;
import org.rcsb.mmtf.mappers.MapArrayToModels;
import org.rcsb.mmtf.mappers.MapConnectivityToCoordinatesBean;
import org.rcsb.mmtf.mappers.MapMmtfStructureToCoordinates;
import org.rcsb.mmtf.mappers.StructureToConnectivity;
import org.rcsb.mmtf.packing.RecursiveIndexingPacking;
import org.rcsb.mmtf.spark.data.MmtfStructureData;
import org.rcsb.mmtf.utils.ArrayUtils;
import org.rcsb.mmtf.utils.Convertors;
import org.rcsb.mmtf.utils.MmtfUtils;
import org.rcsb.mmtf.utils.TraversalUtils;
import org.rcsb.mmtf.utils.Writer;

import scala.Tuple2;

/**
 * @author Yana Valasatava
 */
public class RunEnsamblesAnalysis implements Serializable {

	private static final long serialVersionUID = -7457852254123051730L;
	
	static Writer w = new Writer();

	public static void main(String[] args ) throws IOException {
		
		// path to a Hadoop sequence file with the PDB structures in MMTF format
		String path = args[0];
		// path to the folder with results
		String root = args[1];
		
		String name;
		String compression;
		
		long start = System.nanoTime();
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());

		List<StructureToConnectivity> traverseStrategies = TraversalUtils.getStrategies();

		//  methods holds the methods that perform intermolecular coders and thus the connection information is required
		List<InterCoder> methods = new ArrayList<InterCoder>();
		methods.add(new InterDeltaEncode());
		methods.add(new InterErrorEncode());
		methods.add(new InterDoubleDeltaEncode());
		
		// Different data sets
		String[] sets = {"models", "chains"};
		// Different integer modes
		float[] multipliers = {(float) 1000.0, (float) 10.0};
		// Different data sets
		boolean[] caTraceFlags = {true, false};

		// == GET MMTF STRUCTURES ==
		JavaPairRDD<String, MmtfStructure> mmtf = new MmtfStructureData(path).getJavaPairRdd().cache();
		
		for (String set : sets) {

			JavaPairRDD<String, MmtfStructure> subset;
			if (set.equals("models")) { 
				subset = mmtf.filter(t -> (t._2.getChainsPerModel().length > 1)); 
			}
			else { 
				subset = mmtf.filter(t -> MmtfUtils.hasAllIdenticalSubunits(t._2));
			}
			
			for (boolean caTrace : caTraceFlags) {
				
				JavaPairRDD<String, float[][][]> c = subset
						.mapToPair(new MapMmtfStructureToCoordinates(caTrace))
						.filter(t -> ArrayUtils.hasDataPoints(t._2, 3)).cache();
				
				JavaPairRDD<String, float[][][]> coordinates;
				if (set.equals("models")) { coordinates = c.filter(t -> ArrayUtils.sameModelLength(t._2)); }
				else { coordinates = c.filter(t -> ArrayUtils.sameChainLength(t._2)); }
				
				// == for LOSSLESS and LOSSY integer coders ==
				
				for (float multiplier : multipliers) {
					
					if (multiplier == 1000.0) {compression = "lossless";}
					else {compression = "lossy";}

					JavaPairRDD<String, Point3d[][][]> points = coordinates
							.mapToPair(t -> new Tuple2<String, Point3d[][][]>(t._1, Convertors.arrayFloatToPoint3dArray(t._2)));
					
					JavaPairRDD<String, Point3d[][]> structure;
					if (set.equals("models")) { structure = points.mapToPair(new MapArrayToModels()); }
					else { structure = points.mapToPair(new MapArrayToChains()); }

					JavaPairRDD<String, int[]> encoded = structure
					.flatMapToPair(new FlatMapTraverseStrategiesToCoordinates(traverseStrategies))
					.mapToPair(new MapConnectivityToCoordinatesBean(new IntegerEncoder(multiplier)))
					.flatMapToPair(new FlatMapIntraEncodingToCoordinatesBean(methods))
					.mapToPair(new RecursiveIndexingPacking());
					
					// compressed size
					List<Tuple2<String, Long>> size = encoded
						.mapToPair(t -> new Tuple2<String, byte[]>(t._1, Convertors.arrayToByteArray(t._2, Short.MAX_VALUE)))
						.mapToPair(t -> new Tuple2<String, Long>(t._1, (long) GzipCompression.compress(t._2).length))
						.collect();
						
					// == REPORT RESULTS ==
					
					if (caTrace) { name = set+"_reduced"; }
					else { name = set+"_full"; }
					
					String dir = root+set+"_"+timeStamp+"/";
					if (!(new File(dir).exists()))
						new File(dir).mkdir();
					
					String workDir = dir+compression+"/";
					if (!(new File(workDir).exists()))
						new File(workDir).mkdir();

					// report size of compressed data (gzip)
					String uri = workDir+name+"_size_GZIP.csv";
					w.writeLongToFile(size, uri);
				}
			}
			subset.unpersist();
		}
		mmtf.unpersist();
		System.out.println("DONE!");
		System.out.println("Time: " + (System.nanoTime() - start)/1E9 + " sec.");
	}
}
