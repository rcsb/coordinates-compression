package org.rcsb.mmtf.mappers;

import org.apache.spark.api.java.function.PairFunction;
import org.rcsb.mmtf.utils.Convertors;

import scala.Tuple2;

public class MapToByteArray implements PairFunction <Tuple2<String, int[]>, String, byte[]> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3856198140237484693L;

	@Override
	public Tuple2<String, byte[]> call(Tuple2<String, int[]> t) {
		if (t._1.contains("32-bit")) {return new Tuple2<String, byte[]>(t._1, Convertors.arrayToByteArray(t._2, Integer.MAX_VALUE));}
		else {return new Tuple2<String, byte[]>(t._1, Convertors.arrayToByteArray(t._2, Short.MAX_VALUE));}
	}
}