/**
 * 
 */
package org.rcsb.mmtf.mappers;

import javax.vecmath.Point3d;

import org.apache.spark.api.java.function.PairFunction;
import org.rcsb.mmtf.utils.Convertors;

import scala.Tuple2;

/**
 * Takes the structure coordinates as int[][][] and converts it to Point3d[][][]
 * 
 * @author Yana Valasatava
 *
 */
public class MapIntegerArrayToPoint3d implements PairFunction<Tuple2<String, int[][][]>, String, Point3d[][][]> {

	private static final long serialVersionUID = 23121237447347339L;

	@Override
	public Tuple2<String, Point3d[][][]> call(Tuple2<String, int[][][]> t) throws Exception {
		
		Point3d[][][] coordinates = Convertors.arrayIntegerToPoint3dArray(t._2);
		return new Tuple2<String, Point3d[][][]>(t._1, coordinates);
	}
}