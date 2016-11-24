package org.rcsb.mmtf.encoders;

import java.io.Serializable;

import org.rcsb.mmtf.databeans.CoordinatesBean;
import org.rcsb.mmtf.intercoders.InterDeltaEncoder;
import org.rcsb.mmtf.intracoders.IntraDeltaEncoder;
import org.rcsb.mmtf.utils.ArrayUtils;

public class InterDoubleDeltaEncode implements Serializable, InterCoder {

	/**
	 * 
	 */
	private static final long serialVersionUID = -52047177523934721L;
	
	private int[] xCoords;
	private int[] yCoords;
	private int[] zCoords;
	
	public int[] getxCoods() {
		return xCoords;
	}

	public void setxCoods(int[] xCoods) {
		this.xCoords = xCoods;
	}

	public int[] getyCoods() {
		return yCoords;
	}

	public void setyCoods(int[] yCoods) {
		this.yCoords = yCoods;
	}

	public int[] getzCoods() {
		return zCoords;
	}

	public void setzCoods(int[] zCoods) {
		this.zCoords = zCoods;
	}
	
	String name;
	public InterDoubleDeltaEncode() {
		name = "inter double delta";
	}
	
	public void encode(int[][] coordinates, short[] connections) {

		// number or coordinates vectors
		int m = coordinates.length;
		// number of atoms in each vector
		int n = coordinates[0].length/3;
		
		xCoords = new int[m*n];
		yCoords = new int[m*n];
		zCoords = new int[m*n];
		
		IntraDeltaEncoder encoderIntra = new IntraDeltaEncoder();
		InterDeltaEncoder encoderInter = new InterDeltaEncoder();
		
		int i, j;
		boolean flag = true;
		
		// index in output coordinates array
		int p=0;

		int ind = 0;
		while (ind < connections.length) {

			i = connections[ind];
			j = connections[ind+1];
			ind += 2;
			
			int[] v1 = coordinates[i];
			
			int[] x1 = new int[n];
			int[] y1 = new int[n];
			int[] z1 = new int[n];
			
			System.arraycopy(v1, 0, x1, 0, n);
			System.arraycopy(v1, n, y1, 0, n);
			System.arraycopy(v1, n+n, z1, 0, n);
			
			if (flag) {
				// add coordinates of the first vector as integers encoded with intramolecular encoding
				int[] encX = encoderIntra.run(x1);
				int[] encY = encoderIntra.run(y1);
				int[] encZ = encoderIntra.run(z1);
				for (int l=0; l < n; l++) {
					xCoords[p] = encX[l];
					yCoords[p] = encY[l];
					zCoords[p] = encZ[l];
					p++;
				}
				flag = false;
			}

			int[] v2 = coordinates[j];
			
			int[] x2 = new int[n];
			int[] y2 = new int[n];
			int[] z2 = new int[n];
			
			System.arraycopy(v2, 0, x2, 0, n);
			System.arraycopy(v2, n, y2, 0, n);
			System.arraycopy(v2, n+n, z2, 0, n);
			
			int[] x = encoderInter.run(encoderIntra.run(x1), encoderIntra.run(x2));
			int[] y = encoderInter.run(encoderIntra.run(y1), encoderIntra.run(y2));
			int[] z = encoderInter.run(encoderIntra.run(z1), encoderIntra.run(z2));
			
			for (int l=0; l < n; l++) {
				xCoords[p] = x[l];
				yCoords[p] = y[l];
				zCoords[p] = z[l];
				p++;
			}
		}
	}
	
	@Override
	public int[] run(CoordinatesBean bean) {
		this.encode(bean.getCoordinates(), bean.getConnectivity());
		int[] out = ArrayUtils.mergeCoordinatesArrays(getxCoods(), getxCoods(), getxCoods());
		return out;
	}

	@Override
	public String name() {
		return name;
	}
}
