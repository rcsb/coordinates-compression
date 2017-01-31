/**
 * 
 */
package org.rcsb.mmtf.ensembles;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Matrix4d;
import javax.vecmath.Point3d;

import org.rcsb.mmtf.databeans.ConnectionBean;
import org.rcsb.mmtf.geometric.LeastSquaresFitting;
import org.rcsb.mmtf.geometric.Superposition;
import org.rcsb.mmtf.geometric.Transformation;
import org.rcsb.mmtf.mappers.StructureToConnectivity;

import scala.Tuple2;

/**
 * @author Yana Valasatava
 *
 */
public class TraverseReference implements Serializable, StructureToConnectivity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2031854063199006041L;
	
	private boolean supFlag;
	public TraverseReference(boolean supFlag) {
		this.supFlag = supFlag;
	}
	
	public String getName() {
		
		String name = "ref";
		if (supFlag)
			name +="_sup";

		return name;
	}
	
	@Override
	public Tuple2<String, ConnectionBean> getConnectionsFromCoordinates(Tuple2<String, Point3d[][]> t) {
		
		ConnectionBean cbean = new ConnectionBean();
		cbean.setSupFlag(supFlag);
		
		List<Connection<Short>> connections = new ArrayList<Connection<Short>>();

		short i = 0;
		Point3d[] coords_i = t._2[i];
		cbean.addCoordinates(coords_i);
		
		Transformation transformation = new Transformation();
		
		for (short j=1; j < t._2.length; j++) {
			
			connections.add(new Connection<Short>(i,j));
			
			Point3d[] coords_j = t._2[j];
			if (supFlag) {
				
				Superposition superposer = new LeastSquaresFitting();
				
				superposer.run(coords_j, coords_i);
				Matrix4d tM = superposer.getTransformationMatrix();
				coords_j = superposer.getSuperposedCoordanates();
				
				double[] q = transformation.getRotationAsQuaternion(tM);
				double[] tr = transformation.getTranslationAsVector(tM);
				cbean.addQuaternion(q);
				cbean.addTranslations(tr);
			}
			cbean.addCoordinates(coords_j);
		}
		cbean.setConnections(connections);	
		return new Tuple2<String, ConnectionBean>(t._1, cbean);
	}
}
