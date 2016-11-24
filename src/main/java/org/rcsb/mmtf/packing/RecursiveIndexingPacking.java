package org.rcsb.mmtf.packing;

import org.apache.spark.api.java.function.PairFunction;
import org.rcsb.mmtf.intracoders.RecursiveIndexingCoder;

import scala.Tuple2;

public class RecursiveIndexingPacking implements PairFunction <Tuple2<String, int[]>, String, int[]> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4945771749191828954L;
	
	@Override
	public Tuple2<String, int[]> call(Tuple2<String, int[]> t) {

		if (t._1.contains("32-bit")) {return new Tuple2<String, int[]>(t._1, t._2);}
		RecursiveIndexingCoder recInd = new RecursiveIndexingCoder(Short.MAX_VALUE);
		int[] out = recInd.encode(t._2);
		return new Tuple2<String, int[]>(t._1, out);
	}
}