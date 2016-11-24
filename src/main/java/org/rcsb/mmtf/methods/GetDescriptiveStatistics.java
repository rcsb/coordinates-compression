/**
 * 
 */
package org.rcsb.mmtf.methods;

import org.apache.commons.math.stat.descriptive.DescriptiveStatistics;
import org.apache.spark.api.java.function.PairFunction;

import scala.Tuple2;

/**
 * @author Yana Valasatava
 *
 */
public class GetDescriptiveStatistics implements PairFunction<Tuple2<String, int[]>, String, String> {

	private static final long serialVersionUID = 7586344976126736687L;

	@Override
	public Tuple2<String, String> call(Tuple2<String, int[]> t) throws Exception {
		
		DescriptiveStatistics stat = new DescriptiveStatistics();
		
		int[] coords = t._2;
		for (int i=0; i < coords.length; i++) {
			stat.addValue(coords[i]);
		}
		
		double max = stat.getMax();
		double min = stat.getMin();
		double mean = stat.getMean();
		double median = org.rcsb.mmtf.statistics.DescriptiveStatistics.getMedian(t._2);
		double skewness = stat.getSkewness();
		double kurtosis = stat.getKurtosis();
		
		double shorts = org.rcsb.mmtf.statistics.DescriptiveStatistics.getShorts(t._2)*100;
		double bytes = org.rcsb.mmtf.statistics.DescriptiveStatistics.getBytes(t._2)*100;
		
		String line = String.format("%.3f;%.3f;%.3f;%.3f;%.3f;%.3f;%.3f;%.3f", min, max, mean, median, skewness, kurtosis, shorts, bytes);
		return new Tuple2<String, String>(t._1, line);
	}
}
