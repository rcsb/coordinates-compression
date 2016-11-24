/**
 * 
 */
package org.rcsb.mmtf.encoders;
import java.io.Serializable;
/**
 * @author Yana Valasatava
 *
 */
public class IntegerEncode implements Serializable, IntraCoder {

	private static final long serialVersionUID = 2440637297705745159L;

	private String name;
	public IntegerEncode() {
		name = "integer";
	}
	
	@Override
	public int[] run(int[] coordinates) {
		return coordinates;
	}

	@Override
	public String name() {
		return name;
	}
}
