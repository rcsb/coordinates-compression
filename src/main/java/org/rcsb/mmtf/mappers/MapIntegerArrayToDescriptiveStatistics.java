/**
 * 
 */
package org.rcsb.mmtf.mappers;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.spark.api.java.function.PairFunction;

import scala.Tuple2;

/**
 * @author Yana Valasatava
 *
 */
public class MapIntegerArrayToDescriptiveStatistics implements PairFunction<Tuple2<String, int[]>, String, String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3218861555469580523L;

	/* (non-Javadoc)
	 * @see org.apache.spark.api.java.function.PairFunction#call(java.lang.Object)
	 */
	@Override
	public Tuple2<String, String> call(Tuple2<String, int[]> t) throws Exception {
		
		DescriptiveStatistics statistics = new DescriptiveStatistics();
		for (int i : t._2) {
			statistics.addValue((double)i);
		}
		
		double skewness = statistics.getSkewness();
		double kurtosis = statistics.getKurtosis();
		
		double min = statistics.getMin();
		double max = statistics.getMax();
		
		double mean = statistics.getMean();
		double median = org.rcsb.mmtf.statistics.DescriptiveStatistics.getMedian(t._2);
		
		double std = statistics.getStandardDeviation();
		
		double p01 = statistics.getPercentile(0.1);
		double p1 = statistics.getPercentile(1);
		double p5 = statistics.getPercentile(5);

		double p95 = statistics.getPercentile(95);
		double p99 = statistics.getPercentile(99);
		double p999 = statistics.getPercentile(99.9);
		
		String line = String.format("%.3f;%.3f;%.3f;%.3f;%.3f;%.3f;%.3f;%.3f;%.3f;%.3f;%.3f;%.3f;%.3f", skewness, kurtosis, min, max, mean, median, std, p01, p1, p5, p95, p99, p999);
		return new Tuple2<String, String>(t._1, line);
	}


}
