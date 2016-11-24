package org.rcsb.mmtf.mappers;

import java.util.ArrayList;
import java.util.List;

import org.apache.spark.api.java.function.PairFlatMapFunction;
import org.rcsb.mmtf.databeans.CoordinatesBean;
import org.rcsb.mmtf.encoders.InterCoder;

import scala.Tuple2;

/**
 * 
 */

public class FlatMapIntraEncodingToCoordinatesBean implements PairFlatMapFunction <Tuple2<String, CoordinatesBean>, String, int[]>{

	private static final long serialVersionUID = 6943581453238050123L;

	private List<InterCoder> methods;
	public FlatMapIntraEncodingToCoordinatesBean(List<InterCoder> methods) {
		this.methods = methods;
	}
	
	@Override
	public Iterable<Tuple2<String, int[]>> call(Tuple2<String, CoordinatesBean> t) throws Exception {
		
		List<Tuple2<String, int[]>> out = new ArrayList<Tuple2<String, int[]>>();
		
		for(InterCoder method: this.methods){
			
			String key = method.name()+"_"+t._1.replace("_total", "");
			if (t._1.contains("mst_gzip")||t._1.contains("mst_avrgdelta")) {
				String metric = t._2.getMetricUsesEncoding();
				String methodEncoding = method.name();
				if ( !methodEncoding.contains(metric) ) {
					continue;
				}
			}
			int[] encoded = method.run(t._2);
			out.add(new Tuple2<String, int[]>(key, encoded));
		}
		return out;
	}
}
