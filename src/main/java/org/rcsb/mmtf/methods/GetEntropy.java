/**
 * 
 */
package org.rcsb.mmtf.methods;

import org.apache.spark.api.java.function.PairFunction;
import org.rcsb.mmtf.statistics.CompressionStatistics;

import scala.Tuple2;

/**
 * @author Yana Valasatava
 *
 */
public class GetEntropy implements PairFunction <Tuple2<String, byte[]>, String, Double> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4255783325488881464L;

	@Override
	public Tuple2<String, Double> call(Tuple2<String, byte[]> t) throws Exception {
		double entropy = CompressionStatistics.calculateEntropyBytes(t._2);	
		return new Tuple2<String, Double>(t._1, entropy);
	}
}
