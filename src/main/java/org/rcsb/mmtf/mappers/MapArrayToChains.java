package org.rcsb.mmtf.mappers;

import javax.vecmath.Point3d;

import org.apache.spark.api.java.function.PairFunction;

import scala.Tuple2;

public class MapArrayToChains implements PairFunction<Tuple2<String, Point3d[][][]>, String, Point3d[][]> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2900291821613207183L;

	@Override
	public Tuple2<String, Point3d[][]> call(Tuple2<String, Point3d[][][]> t) throws Exception {

		Point3d[][][] structure = t._2;
		int n=0;
		for (Point3d[][] model : structure) {
			n+=model.length;
		}	
		Point3d[][] out = new Point3d[n][];
		
		int m=0;
		for (Point3d[][] model : structure) {
			for (Point3d[] chain : model) {
				int k=0;
				out[m] = new Point3d[chain.length];
				for (Point3d atom : chain) {
					out[m][k]=atom;
					k++;
				}
				m++;
			}
		}
		return new Tuple2<String, Point3d[][]>(t._1, out);
	}
}
