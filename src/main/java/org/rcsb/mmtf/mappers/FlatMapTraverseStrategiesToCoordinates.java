/**
 * 
 */
package org.rcsb.mmtf.mappers;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Point3d;

import org.apache.spark.api.java.function.PairFlatMapFunction;
import org.rcsb.mmtf.databeans.ConnectionBean;

import scala.Tuple2;

/**  A list of coordinates is traversed using each of given StructureToConnectivity methods  
 * @param t A Tuple2 that holds PDB id and a list of vectors with Point3d objects for atoms coordinates
 * @return A new Tuple2 with PDB id and a ConnectionBean object that holds (superimposed) coordinates, 
 * connections and transformations when superposition is applied, metrics used for MST
 * 
 * @author Yana Valasatava
 */

public class FlatMapTraverseStrategiesToCoordinates implements PairFlatMapFunction<Tuple2<String, Point3d[][]>, String, ConnectionBean> {


	private static final long serialVersionUID = -2551466211076099522L;
	private List<StructureToConnectivity> methods;

	public FlatMapTraverseStrategiesToCoordinates(List<StructureToConnectivity> methods) {
		this.methods = methods;
	}

	@Override
	public Iterable<Tuple2<String, ConnectionBean>> call(Tuple2<String, Point3d[][]> t) throws Exception {
		
		List<Tuple2<String, ConnectionBean>> outList = new ArrayList<Tuple2<String, ConnectionBean>>();
		
		for (StructureToConnectivity method: this.methods) {
			
			try {
				String key = method.getName()+";"+t._1;
				Tuple2<String, ConnectionBean> t2 = method.getConnectionsFromCoordinates(t);
				outList.add(new Tuple2<String, ConnectionBean>(key, t2._2));
			} catch (Exception e) {
				System.out.println(t._1);
				e.printStackTrace();
			}
		}
		return outList;
	}
}
