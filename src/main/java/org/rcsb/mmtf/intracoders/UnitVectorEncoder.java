/**
 * 
 */
package org.rcsb.mmtf.intracoders;
import org.rcsb.mmtf.compression.UnitVectorTransform;

import java.io.Serializable;

/**
 * @author Peter Rose, adopted by Yana Valasatava
 *
 */
public class UnitVectorEncoder implements IntegerTransform, Serializable {

	private static final long serialVersionUID = -7307821587641045246L;
	
	private UnitVectorTransform uvt;

	private String name;
	public UnitVectorEncoder(UnitVectorTransform uvt) {
		this.uvt = uvt;
		this.name = uvt.name();
	}
	
	@Override
	public int[] run(int[] input) {
		
		double[] data = new double[input.length];
		for (int i=0; i < input.length; i++) {
			data[i] = ((double) input[i])/1000;
		}
		
		int len = data.length/3;
		int[] out = new int[5*(len-1)+4];
		
		// calculate the average bond length
		double sumlen = (double) 0.0;
		for (int i = 1; i < len; i++) {

			double dx = data[i] - data[i-1];
			double dy = data[i+len] - data[i+len-1];
			double dz = data[i+len+len] - data[i+len+len-1];

			double dSq = dx*dx +dy*dy + dz*dz;
			sumlen = sumlen + Math.sqrt( (double) dSq);
		}

		int length = (int) Math.round((sumlen*1000)/len); // average bond length
		
		out[out.length-4] = input[0];
		out[out.length-3] = input[len];
		out[out.length-2] = input[2*len];
				
		out[out.length-1] = length; // store the average bond length
		
		int j = 0;
		int m = len-1; // number of encoded bonds 
		for (int i = 1; i < len; i++) {
			
			double dx = data[i] - data[i-1]; // calculate a given bond length
			double dy = data[i+len] - data[i+len-1];
			double dz = data[i+len+len] - data[i+len+len-1];
			
			double dSq = dx*dx +dy*dy + dz*dz;
			double blen = Math.sqrt(dSq);

			double ux = dx/blen; // calculate unit vector for the given bond
			double uy = dy/blen;
			double uz = dz/blen;
			
			int val = uvt.compress3b(ux, uy, uz); // compress unit vector to an integer value
			
			int deltaD = length - (int) Math.round(blen*1000); // calculate the difference between the given bond length and the average bond length
			
			double[] d = uvt.decompress3b(val);

			double ddx = d[0] * blen;
			double ddy = d[1] * blen;
			double ddz = d[2] * blen;

			out[j] = val; // store compressed value
			out[j+m]= deltaD; // store the difference between the given bond length and the average bond length 
			out[j+m+m] = (int) Math.round((dx - ddx)*1000); // keep the residual differences 
			out[j+m+m+m] = (int) Math.round((dy - ddy)*1000);
			out[j+m+m+m+m] = (int) Math.round((dz - ddz)*1000);
			j++;
		}
		return out;
	}
		
	public String name() {
		return name;
	}
}