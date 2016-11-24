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
public class GetTransformationLength implements PairFunction<Tuple2<String, EncodedBean>, String, Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3306842747679613602L;

	@Override
	public Tuple2<String, Long> call(Tuple2<String, EncodedBean> t) throws Exception {
		if (t._2.getQuaternions() != null) {
			double[] rotations = t._2.getQuaternions();
			int lr = rotations.length*4;
			double[] translations = t._2.getTranslations();
			int lt = translations.length*3;
			return new Tuple2<String, Long>(t._1, (long) (lr+lt));
		}
		else
			return new Tuple2<String, Long>(t._1, (long) 0);
	}
}
