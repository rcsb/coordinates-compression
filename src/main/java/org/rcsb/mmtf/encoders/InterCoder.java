/**
 * 
 */
package org.rcsb.mmtf.encoders;

import java.io.IOException;
import org.rcsb.mmtf.databeans.CoordinatesBean;

/**
 * @author Yana Valasatava
 *
 */
public interface InterCoder {

	/**
	 * @param t
	 * @return
	 * @throws IOException
	 */
	int[] run(CoordinatesBean t) throws IOException;

	/**
	 * @return
	 */
	String name();

}
