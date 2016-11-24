/**
 * 
 */
package org.rcsb.mmtf.geometric;

import static org.junit.Assert.assertEquals;

import javax.vecmath.Matrix4d;
import javax.vecmath.Point3d;
import javax.vecmath.Quat4d;
import javax.vecmath.Vector3d;

import org.biojava.nbio.structure.symmetry.geometry.SuperPosition;
import org.junit.Test;
import org.rcsb.mmtf.geometric.Transformation;
import org.rcsb.mmtf.utils.ArrayUtils;

/**
 * @author Yana Valasatava
 *
 */
public class TestTransformation {
	
	@Test
	public void TestRunRMSD() {
		
		ArrayUtils utils = new ArrayUtils();
		
		int lenght = 3;
		Point3d[] v1 = utils.generateVectorPoint3d(lenght);
		Point3d[] v2 = utils.multiplyVectorPoint3dBy(utils.generateVectorPoint3d(lenght), 2);
		
		Matrix4d tM = SuperPosition.superposeWithTranslation(v1, v2);
		
		Transformation tr = new Transformation();
		double[] q = tr.getRotationAsQuaternion(tM);
		double[] t = tr.getTranslationAsVector(tM);
		
		Quat4d qq = new Quat4d(q[1],q[2],q[3],q[0]);
		Vector3d tt = new Vector3d(t[0],t[1],t[2]);
		
		Matrix4d newM = new Matrix4d(qq, tt, 1.0);
		
		//assertEquals(tM, newM);
	}
}
