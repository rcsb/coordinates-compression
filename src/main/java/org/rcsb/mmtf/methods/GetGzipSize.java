package org.rcsb.mmtf.methods;

import org.apache.spark.api.java.function.PairFunction;
import org.rcsb.mmtf.compression.GzipCompression;

import scala.Tuple2;

public class GetGzipSize implements PairFunction<Tuple2<String, byte[]>, String, Long> {

	private static final long serialVersionUID = -6690677199972705435L;

	@Override
	public Tuple2<String, Long> call(Tuple2<String, byte[]> t) throws Exception {
		System.out.println(t._1);
		return new Tuple2<String, Long>(t._1, (long) GzipCompression.compress(t._2).length);
	}	
}
