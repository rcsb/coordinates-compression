/**
 * 
 */
package org.rcsb.mmtf.mappers;

import org.apache.spark.api.java.function.PairFunction;
import org.rcsb.mmtf.dataholders.MmtfStructure;
import org.rcsb.mmtf.decoders.MmtfCustomDecoder;

import scala.Tuple2;

/**
 * @author Yana Valasatava
 *
 */
public class MapMmtfBeanToCoordinateSets implements PairFunction<Tuple2<String, MmtfStructure>, String, float[][][]>  {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4069314402747705347L;

	private boolean caTrace;
	public MapMmtfBeanToCoordinateSets(boolean caTrace) {
		this.setCaTrace(caTrace);
	}

	@Override
	public Tuple2<String, float[][][]> call(Tuple2<String, MmtfStructure> t) throws Exception {	
		return new Tuple2<String, float[][][]>(t._1, MmtfCustomDecoder.decodeCoordinates(t._2, caTrace));
	}

	public boolean isCaTrace() {
		return caTrace;
	}

	public void setCaTrace(boolean caTrace) {
		this.caTrace = caTrace;
	}
}
