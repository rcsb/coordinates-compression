package org.rcsb.mmtf.geometric;

import java.util.Arrays;

/**
 * fit a line minimizing absolute deviation
 * 
 * Copyright (C) Numerical Recipes Software 1986-2007
 * Java translation Copyright (C) Huang Wen Hui 2012
 *
 * @author hwh
 * @author Peter Rose (simplified version without dependencies)
 *
 */
public class FitMed {
	public final static double DBL_EPSILON = 2.2204460492503131E-16;

	int ndata;
	public double a, b, abdev;
	public double aLsq, bLsq, lsqDev;
	double[] x, y;
	
	public static void main(String[] args) {
		double[] v = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
		double[] w = {1, 2, 3, 4, 5, 6, 7, 8, 12, 15};
		
		System.out.println("inital abdev: " + absoluteDeviation(v,  w));
		FitMed f = new FitMed(v, w);
		System.out.println("a: " + f.a);
		System.out.println("b: " + f.b);
		System.out.println("abdev: " + f.abdev);
		
		System.out.println(Arrays.toString(w));
		System.out.println(Arrays.toString(f.predict(v)));
	}

	public FitMed(final double[] xx, final double[] yy) {
		ndata = xx.length;
		x = xx;
		y = yy;

		int j;
		double b1,b2,del,f,f1,f2,sigb,temp;
		double sx=0.0,sy=0.0,sxy=0.0,sxx=0.0,chisq=0.0;
		for (j=0;j<ndata;j++) {
			sx += x[j];
			sy += y[j];
			sxy += x[j]*y[j];
			sxx += x[j] * x[j];
		}
		del=ndata*sxx-sx*sx;
		a=(sxx*sy-sx*sxy)/del;
		b=(ndata*sxy-sx*sy)/del;
		aLsq = a;
		bLsq = b;
		for (j=0;j<ndata;j++) {
			temp=y[j]-(a+b*x[j]);
			chisq += (temp*temp);
		}

		sigb=Math.sqrt(chisq/del);
		lsqDev = sigb;
		b1=b;
		f1=rofunc(b1);
		if (sigb > 0.0) {
			b2=b+SIGN(3.0*sigb,f1);
			f2=rofunc(b2);
			if (b2 == b1) {
				abdev /= ndata;
				return;
			}
			while (f1*f2 > 0.0) {
				b=b2+1.6*(b2-b1);
				b1=b2;
				f1=f2;
				b2=b;
				f2=rofunc(b2);
			}
			sigb=0.01*sigb;
			while (Math.abs(b2-b1) > sigb) {
				b=b1+0.5*(b2-b1);
				if (b == b1 || b == b2) break;
				f=rofunc(b);
				if (f*f1 >= 0.0) {
					f1=f;
					b1=b;
				} else {
					f2=f;
					b2=b;
				}
			}
		}
		abdev /= ndata;
	}

	public double rofunc(final double b) {
		final double EPS=DBL_EPSILON;
		int j;
		double d,sum=0.0;
		double[] arr = new double[ndata];
		for (j=0;j<ndata;j++) arr[j]=y[j]-b*x[j];

		    if ((ndata & 1) == 1) {
			      a=select((ndata-1)>>1,arr);
			    } else {
				      j=ndata >> 1;
				      a=0.5*(select(j-1,arr)+select(j,arr));
		    }

		abdev=0.0;
		for (j=0;j<ndata;j++) {
			d=y[j]-(b*x[j]+a);
			abdev += Math.abs(d);
			if (y[j] != 0.0) d /= Math.abs(y[j]);
			if (Math.abs(d) > EPS) sum += (d >= 0.0 ? x[j] : -x[j]);
		}
		return sum;
	}

	public static double select(final int k, final double[] arr) {
		int i,ir,j,l,mid,n=arr.length;
		double a;
		l=0;
		ir=n-1;
		for (;;) {
			if (ir <= l+1) {
				if (ir == l+1 && arr[ir] < arr[l]){
					swap(arr,l,ir);
				}
				return arr[k];
			} else {
				mid=(l+ir) >> 1;
				swap(arr,mid,l+1);
				if (arr[l] > arr[ir]){
					swap(arr, l, ir);
				}
				if (arr[l+1] > arr[ir]){
					swap(arr, l+1, ir);
				}
				if (arr[l] > arr[l+1]){
					swap(arr,l,l+1);
				}
				i=l+1;
				j=ir;
				a=arr[l+1];
				for (;;) {
					do i++; while (arr[i] < a);
					do j--; while (arr[j] > a);
					if (j < i) break;
					swap(arr,i,j);
				}
				arr[l+1]=arr[j];
				arr[j]=a;
				if (j >= k) ir=j-1;
				if (j <= k) l=i;
			}
		}
	}
	
	public double predict(double value) {
		return a + b*value;
	}
	
	public double[] predict(double[] values) {
		double[] p = new double[values.length];
		for (int i = 0; i < values.length; i++) {
			p[i] = predict(values[i]);
		}
		return p;
	}
	
	public double predictLsq(double value) {
		return aLsq + bLsq*value;
	}
	
	public double[] predictLsq(double[] values) {
		double[] p = new double[values.length];
		for (int i = 0; i < values.length; i++) {
			p[i] = predictLsq(values[i]);
		}
		return p;
	}
	
	public static double absoluteDeviation(double x[], double y[]) {
		double delta = 0;
		for (int i = 0; i < x.length; i++) {
			delta += Math.abs(x[i]-y[i]);
		}
		return delta;
	}
	
	public static double SIGN(final double a, final double b)
	{return b >= 0 ? (a >= 0 ? a : -a) : (a >= 0 ? -a : a);}

	/**
	 * Swaps x[a] with x[b].
	 */
	public static void swap(double x[], int a, int b) {
		double t = x[a];
		x[a] = x[b];
		x[b] = t;
	}

}
