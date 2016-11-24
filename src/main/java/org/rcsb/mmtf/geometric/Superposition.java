/**
 * 
 */
package org.rcsb.mmtf.geometric;

import javax.vecmath.Matrix4d;
import javax.vecmath.Point3d;

/**
 * @author Yana Valasatava
 *
 */
public interface Superposition {
	
	public void run(Point3d[] v1, Point3d[] v2);
	public Matrix4d getTransformationMatrix();
	public Point3d[] getSuperposedCoordanates();

}
