/**
 * 
 */
package org.rcsb.mmtf.compression;

/**
 * @author Yana Valasatava
 *
 */
public interface UnitVectorTransform {
	
	public int compress3b(double x, double y, double z);
	public double[] decompress3b(int v);
	public String name();
}
