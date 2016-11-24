package org.rcsb.mmtf.mappers;

import javax.vecmath.Point3d;

import org.apache.spark.api.java.function.PairFunction;

import scala.Tuple2;

public class MapArrayToModels implements PairFunction<Tuple2<String, Point3d[][][]>, String, Point3d[][]> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2900291821613207183L;

	@Override
	public Tuple2<String, Point3d[][]> call(Tuple2<String, Point3d[][][]> t) throws Exception {

		Point3d[][][] structure = t._2;
		Point3d[][] out = new Point3d[structure.length][];
		
		int m=0;
		for (Point3d[][] model : structure) {
			int n=0;
			for (Point3d[] chain : model) {
				n+=chain.length;
			}
			out[m] = new Point3d[n];
			int i=0;
			for (Point3d[] chain : model) {
				for (Point3d atom : chain) {
					out[m][i]=atom;
					i++;
				}
			}
			m++;
		}
		return new Tuple2<String, Point3d[][]>(t._1, out);
	}
}
