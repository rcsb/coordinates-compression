/**
 * 
 */
package org.rcsb.mmtf.mappers;

import org.apache.spark.api.java.function.PairFunction;
import org.rcsb.mmtf.statistics.DescriptiveStatistics;

import scala.Tuple2;

/**
 * @author Yana Valasatava
 *
 */
public class MapIntegerArrayToStatistics implements PairFunction<Tuple2<String, int[]>, String, String> {

	private static final long serialVersionUID = -1098731652800947963L;

	@Override
	public Tuple2<String, String> call(Tuple2<String, int[]> t) throws Exception {
		
		int min = 0;
		int max = 0;

		double mean = 0.0;
		double median = 0.0;

		double sh = 0.0;
		double byt = 0.0;
		
		int[] coords = t._2;

		max = DescriptiveStatistics.getMax(coords);
		min = DescriptiveStatistics.getMin(coords);
		mean = DescriptiveStatistics.getMean(coords);
		median = DescriptiveStatistics.getMedian(coords);
		sh = DescriptiveStatistics.getShorts(coords);
		byt = DescriptiveStatistics.getBytes(coords);

		String out = String.format("%d;%d;%.3f;%.3f;%.3f;%.3f", max, min, mean, median, sh, byt);
		return new Tuple2<String, String>(t._1, out);
	}
}
