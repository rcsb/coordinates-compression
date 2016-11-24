package org.rcsb.mmtf.geometric;

import java.io.Serializable;

import javax.vecmath.Matrix4d;
import javax.vecmath.Point3d;

import org.biojava.nbio.structure.symmetry.geometry.SuperPosition;


/**
 * This class minimizes the mean absolute deviation between two pre-aligned sets of 
 * 3D points, a, and b. It transforms the coordinate set a onto the reference coordinate set b
 * by an independent robust fit in each of the three dimensions. This creates a transformation matrix
 * that scales each dimension independently, and hence may distort the geometry.
 * 
 * Usage:
 * 
 * The input consists of 2 Point3d arrays of equal length. The input coordinates
 * are not changed.
 * 
 *    Point3d[] a = ...
 *    Point3d[] b = ...
 *    
 *    // calculate absolute deviation of input
 *    double originalAbsDev = AbsDev3dMinimizer.absoluteDeviation(a, b)
 *    
 *    // minimize the absolute deviation 
 *    AbsDev3dMinimizer minimizer =  new AbsDev3dMinimizer();
 *    minimizer.set(a, b); // note, the input coordinates are *not* modified!
 *    
 *    // get the minimized absolute deviation
 *    double minimisedAbsDev = minimizer.getAbsoluteDevition();
 * 
 *    // get a 4x4 transformation (rotation and translation) matrix
 *    Matrix4d rottrans = minimizer.getTransformationMatrix();
 * 
 *    // Get transformed points (a transformed into the reference b)
 *    Point3d[] yTransformed = minimizer.getTransformedCoordinates();
 * 
 * 
 * @author Peter Rose
 */
public final class AbsDev3dMinimizer implements Serializable {
	private static final long serialVersionUID = 1L;

	private Point3d[] a;
	private Point3d[] b;

	private double absDeviation;
	private Matrix4d transformation;
	private boolean transformationCalculated = false;

	/**
	 * Default constructor
	 */
	public AbsDev3dMinimizer() {
	}

	/**
	 * Sets the two input coordinate arrays. These input arrays must be of
	 * equal length. Input coordinates are not modified.
	 * @param a 3d points of reference coordinate set
	 * @param b 3d points of coordinate set for transformation
	 */
	public void set(Point3d[] a, Point3d[] b) {
		this.a = a;
		this.b = b;
		transformationCalculated = false;
	}

	/**
	 * Returns the mean absolute deviation.
	 * @return absolute mean deviation for transforming y intto x
	 */
	public double getAbsoluteDeviation() {
		if (! transformationCalculated) {
			absFit(a, b);
		}
		return absDeviation;
	}

	/**
	 * Returns a 4x4 transformation matrix that transforms the a coordinates into the b reference coordinates
	 * by minimizing the mean absolute deviation. Note, transformations are not equal in the
	 * x, y, and z dimensions.
	 * @return 4x4 transformation matrix to transform a coordinates into b coordinates
	 */
	public Matrix4d getTransformationMatrix() {
		if (! transformationCalculated) {
			absFit(a, b);
		}
		return transformation; 	
	}

	/**
	 * Returns the transformed (superposed) a coordinates
	 * @return transformed a coordinates
	 */
	public Point3d[] getTransformedCoordinates() {
		if (! transformationCalculated) {
			absFit(a, b);
		}

		Point3d[] ta =  SuperPosition.clonePoint3dArray(a);
		transform(transformation, ta); 

		return ta;
	}

	/**
	 * Returns a 4x4 transformation matrix for a robust fit
	 * that minimizes the mean absolute deviation for each dimension.
	 * @param a 3d points of reference coordinate set
	 * @param b 3d points of coordinate set for transformation
	 */
	private void absFit(Point3d[] a, Point3d[] b) {
		transformation = new Matrix4d();
		absDeviation = 0;
		
		double[] ta = new double[a.length];
		double[] tb = new double[b.length];

		// fit absolute deviation for x-direction
		for (int i = 0; i < a.length; i++) {
			ta[i] = a[i].x;
			tb[i] = b[i].x;
		}
		FitMed fit = new FitMed(ta, tb);
		transformation.m00 = fit.b;
		transformation.m03 = fit.a;
		absDeviation += fit.abdev;

		// fit absolute deviation for y-direction
		for (int i = 0; i < a.length; i++) {
			ta[i] = a[i].y;
			tb[i] = b[i].y;	
		}
		fit = new FitMed(ta,tb);
		transformation.m11 = fit.b;
		transformation.m13 = fit.a;
		absDeviation += fit.abdev;

		// fit absolute deviation for z-direction
		for (int i = 0; i < a.length; i++) {
			ta[i] = a[i].z;
			tb[i] = b[i].z;		
		}
		fit = new FitMed(ta, tb);
		transformation.m22 = fit.b;
		transformation.m23 = fit.a;	
		absDeviation += fit.abdev;
		
		transformation.m33 = 1.0;
		absDeviation /= 3;
		transformationCalculated = true;
	}

	public static void translate(Point3d trans, Point3d[] points) {
		for (Point3d p: points) {
			p.add(trans);
		}
	}

	public static void transform(Matrix4d rotTrans, Point3d[] points) {
		for (Point3d p: points) {
			rotTrans.transform(p);
		}
	}

	public static double absoluteDeviation(Point3d[] a, Point3d[] b) {
		double absDev = 0;

		for (int i = 0; i < a.length; i++) {
			absDev += Math.abs(a[i].x - b[i].x);
			absDev += Math.abs(a[i].y - b[i].y);
			absDev += Math.abs(a[i].z - b[i].z);
		}
		return absDev/(3*a.length);
	}
}

