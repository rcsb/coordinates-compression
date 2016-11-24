/**
 * 
 */
package org.rcsb.mmtf.packing;

import java.io.ByteArrayOutputStream;
import java.util.List;

import org.apache.spark.api.java.function.PairFunction;
import org.rcsb.mmtf.codec.ArrayConverters;

import scala.Tuple2;

/**
 * @author Yana Valasatava
 *
 */
public class SplitArraysPacking implements PairFunction<Tuple2<String, int[]>, String, byte[]> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4839314460127163383L;

	@Override
	public Tuple2<String, byte[]> call(Tuple2<String, int[]> t) throws Exception {
		
		List<int[]> coords = ArrayConverters.splitIntegers(t._2);
		byte[] bB = ArrayConverters.convertIntegersToFourByte(coords.get(0));
		byte[] bS = ArrayConverters.convertIntegersToTwoBytes(coords.get(1));
		
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
		outputStream.write( bB );
		outputStream.write( bS );

		byte out[] = outputStream.toByteArray( );
				
		return new Tuple2<String, byte[]>(t._1, out);
	}


}
