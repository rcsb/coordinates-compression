package org.rcsb.mmtf.mappers;

import java.util.ArrayList;
import java.util.List;

import org.apache.spark.api.java.function.PairFlatMapFunction;
import org.rcsb.mmtf.compression.BrotliCompression;
import org.rcsb.mmtf.compression.GzipCompression;

import scala.Tuple2;

public class FlatMapEncodingToCompression implements PairFlatMapFunction <Tuple2<String, byte[]>, String, byte[]> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 130490321237982977L;
	
	@Override
	public Iterable<Tuple2<String, byte[]>> call(Tuple2<String, byte[]> t) throws Exception {
		
		List<Tuple2<String, byte[]>> out = new ArrayList<Tuple2<String, byte[]>>();

		out.add(new Tuple2<String, byte[]>(t._1+";"+GzipCompression.name, GzipCompression.compress(t._2)));
		out.add(new Tuple2<String, byte[]>(t._1+";"+BrotliCompression.name, BrotliCompression.compress(t._2)));
		
		return out;
	}

}
