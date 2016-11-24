/**
 * 
 */
package org.rcsb.mmtf.methods;

import java.util.List;

import javax.vecmath.Point3d;

import org.apache.spark.api.java.function.PairFunction;

import scala.Tuple2;

/**
 * @author Yana Valasatava
 *
 */
public class GetLength implements PairFunction<Tuple2<String, List<Point3d[]>>, String, Integer> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 468250734067908432L;

	@Override
	public Tuple2<String, Integer> call(Tuple2<String, List<Point3d[]>> t) throws Exception {
		return new Tuple2<String, Integer>(t._1, t._2.get(0).length);
	}
}
