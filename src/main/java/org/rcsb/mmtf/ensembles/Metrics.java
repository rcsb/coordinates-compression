package org.rcsb.mmtf.ensembles;

import java.io.IOException;

import javax.vecmath.Point3d;

import org.biojava.nbio.structure.symmetry.geometry.SuperPosition;
import org.rcsb.mmtf.compression.GzipCompression;
import org.rcsb.mmtf.intracoders.RecursiveIndexingCoder;
import org.rcsb.mmtf.utils.ArrayUtils;
import org.rcsb.mmtf.utils.Convertors;

import com.fasterxml.jackson.core.JsonProcessingException;

public class Metrics {
	
	private String metric;

	public String getMetric() {
		return metric;
	}

	public void setMetric(String metric) {
		this.metric = metric;
	}
	
	private String encoding;
	
	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public double getScore(Point3d[] coords1, Point3d[] coords2) throws JsonProcessingException, IOException {
		
		double score = 0.0d;
		if (metric.equals("rmsd")) {
			score = runRMSD(coords1, coords2);
		}
		else if (metric.equals("gzip")) {
			score = runGzippedSize(coords1, coords2);
		}
		return score;
	}
	
	// root-mean-square deviation, RMSD
	public double runRMSD(Point3d[] coords1, Point3d[] coords2) {
		return SuperPosition.rmsd(coords1, coords2);
	}	
	
	// gZipped size
	public double runGzippedSize(Point3d[] coords1, Point3d[] coords2) throws JsonProcessingException, IOException {
		
		int nCoords = coords1.length;
		
		int[] xCoods = new int[nCoords];
		int[] yCoods = new int[nCoords];
		int[] zCoods = new int[nCoords];
		
		double score = 0.0;

		int prevDx = 0;
		int prevDy = 0;
		int prevDz = 0;

		int diffX = 0;
		int diffY = 0;
		int diffZ = 0;
		
		for (int i=0; i < nCoords; i++)
		{	
			double x1 = coords1[i].x;
			double y1 = coords1[i].y;
			double z1 = coords1[i].z;

			double x2 = coords2[i].x;
			double y2 = coords2[i].y;
			double z2 = coords2[i].z;
			
			int dx = (int) (x2*1000.0) - (int) (x1*1000.0);
			int dy = (int) (y2*1000.0) - (int) (y1*1000.0);
			int dz = (int) (z2*1000.0) - (int) (z1*1000.0);
			
			diffX = dx;
			diffY = dy;
			diffZ = dz;
			
			if (encoding.equals("error")) {
				dx -= prevDx;
				dy -= prevDy;
				dz -= prevDz;
			}
			
			xCoods[i] = dx;
			yCoods[i] = dy;
			zCoods[i] = dz;
						
			prevDx = diffX;
			prevDy = diffY;
			prevDz = diffZ;
		}
		
		int[] encoded = ArrayUtils.mergeCoordinatesArrays(xCoods, yCoods, zCoods);
		RecursiveIndexingCoder recInd = new RecursiveIndexingCoder(Short.MAX_VALUE);
		int[] out = recInd.encode(encoded);
		byte data[] = Convertors.arrayToByteArray(out, Short.MAX_VALUE);
		int gzipSize = GzipCompression.compress(data).length;
		score = (double) gzipSize;
		
		return score;
		
	}
	
	public double getChebyshevDistanceVectors(Point3d[] v1, Point3d[] v2) {
		
		double d = 0.0;
		for (int i=0; i < v1.length; i++) {
			
			double dj_x = v1[i].x - v2[i].x;
			double dj_y = v1[i].y - v2[i].y;
			double dj_z = v1[i].z - v2[i].z;

			double maxD = dj_x;
			if ( Math.abs(dj_y) > Math.abs(maxD) ) {
				maxD = dj_y;
			}
			if ( Math.abs(dj_z) > Math.abs(maxD) ) {
				maxD = dj_z;
			}
			
			if (Math.abs(maxD) > Math.abs(d)) {
				d = maxD;
			}
		} 
		return d;
	}
	
	public double getAverageChebyshevVectors(Point3d[] v1, Point3d[] v2) {
		
		if (v1.length != v2.length)
			return 0.0;
		
		double sum_x = 0.0;
		double sum_y = 0.0;
		double sum_z = 0.0;
		
		for (int i=0; i < v1.length; i++) {
			
			double di_x = v1[i].x - v2[i].x;
			sum_x += di_x;
			double di_y = v1[i].y - v2[i].y;
			sum_y += di_y;
			double di_z = v1[i].z - v2[i].z;
			sum_z += di_z;
		}
		
		return (sum_x+sum_y+sum_z)/(3*v1.length);
	}
}