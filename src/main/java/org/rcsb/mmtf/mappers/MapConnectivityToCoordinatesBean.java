package org.rcsb.mmtf.mappers;
import org.rcsb.mmtf.databeans.ConnectionBean;
import org.rcsb.mmtf.databeans.CoordinatesBean;
import org.rcsb.mmtf.ensembles.Connection;
import org.rcsb.mmtf.intracoders.DoubleTransform;

import java.util.List;

import javax.vecmath.Point3d;

import org.apache.spark.api.java.function.PairFunction;

import scala.Tuple2;

/**
 * 
 */

public class MapConnectivityToCoordinatesBean implements PairFunction <Tuple2<String, ConnectionBean>, String, CoordinatesBean> {
	
	private static final long serialVersionUID = -5199301551752710410L;
	
	DoubleTransform method;
	public MapConnectivityToCoordinatesBean(DoubleTransform method) {
		this.method = method;
	}
	
	@Override
	public Tuple2<String, CoordinatesBean> call(Tuple2<String, ConnectionBean> t) throws Exception {
		
		CoordinatesBean bean = new CoordinatesBean();
		
		List<Point3d[]> coords = t._2.getCoordinates();
		int m = coords.size();
		
		int[][] transformed = new int[m][];
		
		for ( int i=0; i < m; i++ ) {
			
			Point3d[] v = coords.get(i);
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
			
			transformed[i] = new int[3*len];
			for (int l = 0; l < len; l++) {
				transformed[i][l] = x[l];
				transformed[i][l+len] = y[l];
				transformed[i][l+len+len] = z[l];
			}
		}
		bean.setCoordinates(transformed);
		
		if ( t._2.getMetricUsesEncoding() != null )
			bean.setMetricUsesEncoding(t._2.getMetricUsesEncoding());
		
		if ( t._2.getConnections() != null ) {
			List<Connection<Short>> connections = t._2.getConnections();
			short[] connectivity = new short[2*connections.size()];
			int k = 0;
			for (Connection<Short> c : connections) {
				connectivity[k] = c.p1;
				connectivity[k+1] = c.p2;
				k+=2;
			}
			bean.setConnectivity(connectivity);
		}
		
		if ( t._2.getQuaternions() != null ) {		
			List<double[]> quaternions = t._2.getQuaternions();
			double[] rotations = new double[4*quaternions.size()];
			int r = 0;
			for (double[] qi : quaternions) {
				for (double d : qi) {
					rotations[r] = d;
					r += 1;
				}	 
			}
			List<double[]> transl = t._2.getTranslations();
			double[] translations = new double[3*quaternions.size()];
			int tr = 0;
			for (double[] ti : transl) {
				for (double d : ti) {
					translations[tr] = d;
					tr += 1;
				}	 
			}
		}
		return new Tuple2<String, CoordinatesBean>(t._1, bean);
	}
}
