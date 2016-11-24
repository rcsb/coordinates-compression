/**
 * 
 */
package org.rcsb.mmtf.mappers;

import java.util.ArrayList;
import java.util.List;

import org.apache.spark.api.java.function.PairFlatMapFunction;
import org.rcsb.mmtf.intracoders.RecursiveIndexingCoder;

import scala.Tuple2;

/**
 * @author Yana Valasatava
 *
 */
public class FlatMapRecursiveIndexingPacking implements PairFlatMapFunction <Tuple2<String, int[]>, String, int[]> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2946126412098827193L;
	
	private int[] v;
	public FlatMapRecursiveIndexingPacking(int[] v) {
		this.v = v;
	}
	/* (non-Javadoc)
	 * @see org.apache.spark.api.java.function.PairFlatMapFunction#call(java.lang.Object)
	 */
	@Override
	public Iterable<Tuple2<String, int[]>> call(Tuple2<String, int[]> t) throws Exception {
		String key;
		List<Tuple2<String, int[]>> out = new ArrayList<Tuple2<String, int[]>>();
		for (int i : v) {
			RecursiveIndexingCoder recInd = new RecursiveIndexingCoder(i);
			int[] encoded = recInd.encode(t._2);
			if (i==Short.MAX_VALUE) {key = "shorts";}
			else {key = "bytes";}
			out.add(new Tuple2<String, int[]>(t._1+"_"+key, encoded));
		}
		return out;
	}
}
