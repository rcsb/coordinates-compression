/**
 * 
 */
package org.rcsb.mmtf.mappers;

import javax.vecmath.Point3d;

import org.apache.spark.api.java.function.PairFunction;
import org.rcsb.mmtf.intracoders.DoubleTransform;

import scala.Tuple2;

/**
 * Takes the structure coordinates as Point3d[][][] and converts it to int[][][]
 * 
 * @author Yana Valasatava
 *
 */
public class MapCoordinatesToIntegers implements PairFunction <Tuple2<String, Point3d[][][]>, String, int[][][]> {

	private static final long serialVersionUID = 1881701632910373998L;
	
	DoubleTransform method;
	public MapCoordinatesToIntegers(DoubleTransform method) {
		this.method = method;
	}
	
	@Override
	public Tuple2<String, int[][][]> call(Tuple2<String, Point3d[][][]> t) throws Exception {

		int m = t._2.length;
		int[][][] transformed = new int[m][][];
		
		for ( int i=0; i < m; i++ ) {
			
			int k = t._2[i].length;
			for ( int j=0; j < k; j++ ) {
				
				Point3d[] v = t._2[i][j];
				int len = v.length;
				
				double[][] vd = new double[3][len];
				for (int l = 0; l < len; l++) {
					vd[0][l] = v[l].x;
					vd[1][l] = v[l].y;
					vd[2][l] = v[l].z;
				}		
				int[] x = method.run(vd[0]);
				int[] y = method.run(vd[1]);
				int[] z = method.run(vd[2]);
				
				transformed[i][j] = new int[3*len];
				for (int l = 0; l < len; l++) {
					transformed[i][j][l] = x[l];
					transformed[i][j][l+len] = y[l];
					transformed[i][j][l+len+len] = z[l];
				}
			}
		}
		return new Tuple2<String, int[][][]>(t._1, transformed);
	}
}