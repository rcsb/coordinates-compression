/**
 * 
 */
package org.rcsb.mmtf.encoders;

/**
 * @author Yana Valasatava
 *
 */
public interface IntraCoder {
	
	public String name();
	public int[] run(int[] t);
}
