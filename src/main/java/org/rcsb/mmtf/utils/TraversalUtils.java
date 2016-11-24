package org.rcsb.mmtf.utils;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Point3d;

import org.rcsb.mmtf.databeans.ConnectionBean;
import org.rcsb.mmtf.databeans.CoordinatesBean;
import org.rcsb.mmtf.ensembles.Connection;
import org.rcsb.mmtf.ensembles.TraverseMST;
import org.rcsb.mmtf.ensembles.TraverseReference;
import org.rcsb.mmtf.ensembles.TraverseWaterfall;
import org.rcsb.mmtf.intracoders.IntegerEncoder;
import org.rcsb.mmtf.mappers.StructureToConnectivity;

public class TraversalUtils {

	public static List<StructureToConnectivity> getStrategies() {

		List<StructureToConnectivity> connectivity = new ArrayList<StructureToConnectivity>();

		//Boolean[] supFlags = {true, false};
		Boolean[] supFlags = {false};

		String[] metrics = {"avrgdelta", "gzip"};
		
		for ( Boolean supFlag : supFlags) {		
			connectivity.add(new TraverseReference(supFlag));
			connectivity.add(new TraverseWaterfall(supFlag));
			connectivity.add(new TraverseMST(supFlag, "rmsd", ""));
			for (String metric : metrics) {
				connectivity.add(new TraverseMST(supFlag, metric, "delta"));
				connectivity.add(new TraverseMST(supFlag, metric, "error"));
			}
		}	
		return connectivity; 
	}
	
	public static CoordinatesBean connectivityToCoordinatesBean(ConnectionBean cbean, float multiplier) {
		
		CoordinatesBean bean = new CoordinatesBean();
		IntegerEncoder method = new IntegerEncoder(multiplier);
		
		List<Point3d[]> coords = cbean.getCoordinates();
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
		
		if ( cbean.getMetricUsesEncoding() != null )
			bean.setMetricUsesEncoding(cbean.getMetricUsesEncoding());
		
		if ( cbean.getConnections() != null ) {
			List<Connection<Short>> connections = cbean.getConnections();
			short[] connectivity = new short[2*connections.size()];
			int k = 0;
			for (Connection<Short> c : connections) {
				connectivity[k] = c.p1;
				connectivity[k+1] = c.p2;
				k+=2;
			}
			bean.setConnectivity(connectivity);
		}
		
		if ( cbean.getQuaternions() != null ) {		
			List<double[]> quaternions = cbean.getQuaternions();
			double[] rotations = new double[4*quaternions.size()];
			int r = 0;
			for (double[] qi : quaternions) {
				for (double d : qi) {
					rotations[r] = d;
					r += 1;
				}	 
			}
			List<double[]> transl = cbean.getTranslations();
			double[] translations = new double[3*quaternions.size()];
			int tr = 0;
			for (double[] ti : transl) {
				for (double d : ti) {
					translations[tr] = d;
					tr += 1;
				}	 
			}
		}
		return bean;
	}
}
