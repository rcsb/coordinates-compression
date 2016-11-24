package org.rcsb.mmtf.mappers;

import org.apache.spark.api.java.function.PairFunction;
import org.rcsb.mmtf.utils.Convertors;

import scala.Tuple2;

public class MapNestedIntegerArrayToFlat implements PairFunction <Tuple2<String, int[][][]>, String, int[]> {

	/**
	 * Takes a nested 3-dimensional integer array in[m][k][l] laid out as follow [[x_1...x_l y_1...y_l z_1...z_l]_1 ...[x_1...x_l y_1...y_l z_1...z_l]_k]_1
	 * and converts it to a flat array out[] = [x_1...x_mkl y_1...y_mkl z_1...z_mkl]
	 *  
	 * @param in the input 3-dimensional arrays of integers
	 * @return the output flat 1-dimensional array of integers
	 */
	private static final long serialVersionUID = 6888418451084136764L;
	
	@Override
	public Tuple2<String, int[]> call(Tuple2<String, int[][][]> t) {
		int[][][] in = t._2;
		int[] out = Convertors.nestedArrayToFlat(in);
		return new Tuple2<String, int[]>(t._1, out);
	}
}
