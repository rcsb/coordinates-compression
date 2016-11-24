package org.rcsb.mmtf.intracoders;

import static org.junit.Assert.assertArrayEquals;

import org.junit.Test;
import org.rcsb.mmtf.compression.CompressUnitVector32Bit;

public class TestUnitVectorEncoder {
	
	public static int[] encode3PointsIn32Bit() {
		
		CompressUnitVector32Bit uvt = new CompressUnitVector32Bit();
		
		double[] atom1 = {10.390, 20.427, 13.210};
		double[] atom2 = {10.590, 18.948, 13.104};
		double[] atom3 = {12.056, 18.656, 12.771};
		
		double dx1 = atom2[0] - atom1[0];
		double dy1 = atom2[1] - atom1[1];
		double dz1 = atom2[2] - atom1[2];
		
		double b1 = Math.sqrt(dx1*dx1 +dy1*dy1 + dz1*dz1);
		int bond1 = (int) Math.round(b1*1000);
				
		double dx2 = atom3[0] - atom2[0];
		double dy2 = atom3[1] - atom2[1];
		double dz2 = atom3[2] - atom2[2];
		
		double b2 = Math.sqrt(dx2*dx2 +dy2*dy2 + dz2*dz2);
		int bond2 = (int) Math.round(b2*1000);
		
		double l = (b1+b2)/2;
		int length = (int) Math.round(l*1000);
		
		double ux1 = dx1/b1;
		double uy1 = dy1/b1;
		double uz1 = dz1/b1;
		
		double ux2 = dx2/b2;
		double uy2 = dy2/b2;
		double uz2 = dz2/b2;
		
		int val1 = uvt.compress3b(ux1, uy1, uz1);
		int val2 = uvt.compress3b(ux2, uy2, uz2);
		
		double[] p1 = uvt.decompress3b(val1);
		
		double ddx1 = p1[0] * b1;
		double ddy1 = p1[1] * b1;
		double ddz1 = p1[2] * b1;
		
		int rx1 = (int) Math.round((dx1 - ddx1)*1000);
		int ry1 = (int) Math.round((dy1 - ddy1)*1000);
		int rz1 = (int) Math.round((dz1 - ddz1)*1000);
		
		double[] p2 = uvt.decompress3b(val2);
		
		double ddx2 = p2[0] * b2;
		double ddy2 = p2[1] * b2;
		double ddz2 = p2[2] * b2;
		
		int rx2 = (int) Math.round((dx2 - ddx2)*1000);
		int ry2 = (int) Math.round((dy2 - ddy2)*1000);
		int rz2 = (int) Math.round((dz2 - ddz2)*1000);
		
		int db1 = bond1 - length;
		int db2 = bond2 - length;
		
		int intX = (int) Math.round(atom1[0]*1000);
		int intY = (int) Math.round(atom1[1]*1000);
		int intZ = (int) Math.round(atom1[2]*1000);
		
		int[] out = {intX, intY, intZ, val1, val2, db1, db2, rx1, rx2, ry1, ry2, rz1, rz2, length};
		
		return out;
	}
	
	@Test
	public void testCompress32BitDecoder() {
		
		int[] test = {10390, 20427, 13210,10590, 18948, 13104, 12056, 18656, 12771};
		
		CompressUnitVector32Bit uvt = new CompressUnitVector32Bit();
		
		int[] out = encode3PointsIn32Bit();
		
		int n = (out.length - 4)/5;
		
		int m = 3*n+3;
		int[] decoded = new int[m];
		
		int arvgB = out[out.length - 1];
		
		int xi = out[0];
		int yi = out[1];
		int zi = out[2];
		
		decoded[0] = xi;
		decoded[1] = yi;
		decoded[2] = zi;
		
		int j=3;
		for (int i=0; i < n; i++) {
			
			int val = out[3+i];
			double[] u = uvt.decompress3b(val);
			
			int bondD = out[3+i+n];
			int bond = bondD+arvgB;
					
			double dx = u[0]*bond;
			double dy = u[1]*bond;
			double dz = u[2]*bond;
			
			int xj = (int) (xi+Math.round(dx));
			int yj = (int) (yi+Math.round(dy));
			int zj = (int) (zi+Math.round(dz));
			
			decoded[j+i]=xj;
			j++;
			decoded[j+i]=yj;
			j++;
			decoded[j+i]=zj;
			
			xi=xj;
			yi=yj;
			zi=zj;
		}
		assertArrayEquals(test, decoded);
	}
}
