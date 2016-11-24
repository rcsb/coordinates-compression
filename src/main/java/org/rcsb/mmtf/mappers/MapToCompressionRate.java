/**
 * 
 */
package org.rcsb.mmtf.mappers;

import org.apache.spark.api.java.function.PairFunction;
import org.rcsb.mmtf.compression.GzipCompression;
import org.rcsb.mmtf.statistics.CompressionStatistics;

import scala.Tuple2;

/**
 * @author Yana Valasatava
 *
 */
public class MapToCompressionRate implements PairFunction <Tuple2<String, byte[]>, String, Double>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4407460421259677318L;

	/* (non-Javadoc)
	 * @see org.apache.spark.api.java.function.PairFunction#call(java.lang.Object)
	 */
	@Override
	public Tuple2<String, Double> call(Tuple2<String, byte[]> t) throws Exception {
		
		int uSize = t._2.length;
		int cSize = GzipCompression.compress(t._2).length;
		double rate = CompressionStatistics.calculateRatio(uSize, cSize);
		
		return new Tuple2<String, Double>(t._1, rate);
	}



}
