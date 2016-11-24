/**
 * 
 */
package org.rcsb.mmtf.utils;

import java.io.Serializable;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.rcsb.mmtf.mappers.ByteWriteToByteArr;
import org.rcsb.mmtf.mappers.StringByteToTextByteWriter;

import scala.Tuple2;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.SequenceFileOutputFormat;
import org.apache.hadoop.io.BytesWritable;

/**
 * @author Yana Valasatava
 *
 */
public class WriteToHadoopSequenceFile implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7337147130485914675L; 

	private static int NUM_THREADS = 4;
	private static int NUM_TASKS_PER_THREAD = 4;
	
	public static void main(String[] args) {

		WriteToHadoopSequenceFile writer = new WriteToHadoopSequenceFile();
		//String pdbId = "4UHW";
		String pdbId = "2LHK";
		writer.writePDBstructure(pdbId);
	}

	public void writePDBstructure(String pdbId) {

		int nJobs = NUM_THREADS * NUM_TASKS_PER_THREAD;
		
		String path = "/pdb/Total.hadoop.latest";
		
		SparkConf conf = new SparkConf().setMaster("local[*]").setAppName(WriteToHadoopSequenceFile.class.getSimpleName());
		JavaSparkContext sc = new JavaSparkContext(conf);

		JavaPairRDD<Text, BytesWritable> data = sc
				.sequenceFile(path, Text.class, BytesWritable.class, nJobs)
				.mapToPair(new ByteWriteToByteArr())
				.filter(t->t._1.contains(pdbId))
				//.filter(t -> t._1.endsWith("_total"))
				.map(t -> new Tuple2<String, byte[]>(t._1.split("_")[0], t._2))
				.mapToPair(new StringByteToTextByteWriter());
		
		String uri = "/pdb/Total."+pdbId+".hadoop";
		data.saveAsHadoopFile(uri, Text.class, BytesWritable.class, SequenceFileOutputFormat.class);
		
		sc.stop();
		sc.close();
	}
	
	public void writeSubsetFromFile(String source, String uri) {

		int nJobs = NUM_THREADS * NUM_TASKS_PER_THREAD;
		
		String path = "/pdb/Total.hadoop.latest.bzip2201603170924.txt";
		
		SparkConf conf = new SparkConf().setMaster("local[*]").setAppName(WriteToHadoopSequenceFile.class.getSimpleName());
		JavaSparkContext sc = new JavaSparkContext(conf);

		List<String> pdbs = sc.textFile(source).collect();
		
		JavaPairRDD<Text, BytesWritable> data = sc
				.sequenceFile(path, Text.class, BytesWritable.class, nJobs)
				.mapToPair(new ByteWriteToByteArr())
				.filter(t->t._1.contains("3X0I"))
				.filter(t -> t._1.endsWith("_total"))
				.map(t -> new Tuple2<String, byte[]>(t._1.split("_")[0], t._2))
				.filter(t -> pdbs.contains(t._1))
				.mapToPair(new StringByteToTextByteWriter());
			
		data.saveAsHadoopFile(uri, Text.class, BytesWritable.class, SequenceFileOutputFormat.class);
		
		sc.stop();
		sc.close();
	}
}
