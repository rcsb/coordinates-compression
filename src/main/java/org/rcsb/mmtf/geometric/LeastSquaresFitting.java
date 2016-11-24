/**
 * 
 */
package org.rcsb.mmtf.geometric;

import java.io.Serializable;

import javax.vecmath.Matrix4d;
import javax.vecmath.Point3d;

import org.biojava.nbio.structure.symmetry.geometry.SuperPosition;

/**
 * @author Yana Valasatava
 *
 */
public class LeastSquaresFitting implements Superposition, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6446827562623522934L;
	private Matrix4d tm;
	private Point3d[] supCoords;

	@Override
	public void run(Point3d[] v1, Point3d[] v2) {
		
		Point3d[] v1clone =  SuperPosition.clonePoint3dArray(v1);
		tm = SuperPosition.superposeWithTranslation(v1clone, v2);
		supCoords = v1clone;
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
