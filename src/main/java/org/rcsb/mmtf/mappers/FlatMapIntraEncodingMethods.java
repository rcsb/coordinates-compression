/**
 * 
 */
package org.rcsb.mmtf.mappers;

import java.util.ArrayList;
import java.util.List;

import org.apache.spark.api.java.function.PairFlatMapFunction;
import org.rcsb.mmtf.encoders.IntraCoder;

import scala.Tuple2;

/**
 * @author Yana Valasatava
 *
 */
public class FlatMapIntraEncodingMethods implements PairFlatMapFunction<Tuple2<String, int[]>, String, int[]>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4830250364278093816L;
	
	private List<IntraCoder> methods;
	public FlatMapIntraEncodingMethods(List<IntraCoder> methods) {
		this.methods = methods;
	}
	
	@Override
	public Iterable<Tuple2<String, int[]>> call(Tuple2<String, int[]> t) throws Exception {
		
		List<Tuple2<String, int[]>> out = new ArrayList<Tuple2<String, int[]>>();
		for(IntraCoder method : this.methods) {
			int[] encoded = method.run(t._2);
			out.add(new Tuple2<String, int[]>(method.name()+";"+t._1, encoded));
		}
		return out;
	}
}
