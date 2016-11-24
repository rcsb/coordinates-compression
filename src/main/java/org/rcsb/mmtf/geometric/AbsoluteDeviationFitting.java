/**
 * 
 */
package org.rcsb.mmtf.geometric;

import java.io.Serializable;

import javax.vecmath.Matrix4d;
import javax.vecmath.Point3d;

/**
 * @author Yana Valasatava
 *
 */
public class AbsoluteDeviationFitting implements Superposition, Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3461696994475398313L;
	private Matrix4d tm;
	private Point3d[] supCoords;
	
	@Override
	public void run(Point3d[] v1, Point3d[] v2) {
		
		AbsDev3dMinimizer minimizer = new AbsDev3dMinimizer();
		minimizer.set(v1,v2);
		
		tm = minimizer.getTransformationMatrix();
		supCoords = minimizer.getTransformedCoordinates();
	}

	@Override
	public Matrix4d getTransformationMatrix() {
		return tm;
	}

	@Override
	public Point3d[] getSuperposedCoordanates() {
		return supCoords;
	}

}
