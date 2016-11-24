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
public class MapEncodedValueToDistribution implements PairFunction<Tuple2<String, int[]>, String, float[]>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7637059152764212634L;

	@Override
	public Tuple2<String, float[]> call(Tuple2<String, int[]> t) throws Exception {
		
		float[] distribution = new float[4];
		
		distribution[0] = 100.0f;
		distribution[1] = DescriptiveStatistics.getShorts(t._2);
		distribution[2] = DescriptiveStatistics.getBytes(t._2);
		distribution[3] = t._2.length;
				
		return new Tuple2<String,float[]>(t._1, distribution);
	}


}
