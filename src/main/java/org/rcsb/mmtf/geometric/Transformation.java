/**
 * 
 */
package org.rcsb.mmtf.geometric;

import javax.vecmath.Matrix4d;

/**
 * @author Yana Valasatava
 *
 */
public class Transformation {
    
	double w;
	double x;
	double y;
	double z;
	
	public void matrixToQuaternion(Matrix4d m) {
		
		double tr = m.m00 + m.m11 + m.m22;

		if(tr > 0f) {
			double s = Math.sqrt(1f + tr) * 2f;
			w = 0.25f*s;
			x = (m.m21-m.m12)/s;
			y = (m.m02-m.m20)/s;
			z = (m.m10-m.m01)/s;
		} else if ( (m.m00 > m.m11) && (m.m00 > m.m22) ) {
			double s = Math.sqrt(1f+m.m00-m.m11-m.m22) * 2f;
			w = (m.m21-m.m12)/s;
			x = 0.25f*s;
			y = (m.m01+m.m10)/s;
			z = (m.m02+m.m20)/s;
		} else if(m.m11 > m.m22) {
			double s = Math.sqrt(1f+m.m11-m.m00-m.m22) * 2f;
			w = (m.m02-m.m20)/s;
			x = (m.m01+m.m10)/s;
			y = 0.25f*s;
			z = (m.m12+m.m21)/s;
		} else {
			double s = Math.sqrt(1f+m.m22-m.m00-m.m11) * 2f;
			w = (m.m10-m.m01)/s;
			x = (m.m02+m.m20)/s;
			y = (m.m12+m.m21)/s;
			z = 0.25f*s;
		}
	}
	
	public double[] getRotationAsQuaternion(Matrix4d m) {
		
		matrixToQuaternion(m);
		
		double[] q = new double[4];
		q[0]=this.w;
		q[1]=this.x;
		q[2]=this.y;
		q[3]=this.z;
		
		return q;
	}
	
	public double[] getTranslationAsVector(Matrix4d m) {
		
		double[] t = new double[3];
		
		t[0] = m.m03;
		t[1] = m.m13;
		t[2] = m.m23;
		
		return t;
	}
}