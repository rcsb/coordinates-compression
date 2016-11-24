/**
 * 
 */
package org.rcsb.mmtf.methods;

import org.apache.spark.api.java.function.PairFunction;
import org.rcsb.mmtf.databeans.EncodedBean;

import scala.Tuple2;

/**
 * @author Yana Valasatava
 *
 */
public class GetConnectLength implements PairFunction<Tuple2<String, EncodedBean>, String, Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6602582683764933024L;

	@Override
	public Tuple2<String, Long> call(Tuple2<String, EncodedBean> t) throws Exception {
		
		if (t._2.getConnectivity() != null) {
			short[] connectivity = t._2.getConnectivity();
			int l = connectivity.length*2;
			return new Tuple2<String, Long>(t._1, (long) l);
		}
		else
			return new Tuple2<String, Long>(t._1, (long) 0);
	}


}
