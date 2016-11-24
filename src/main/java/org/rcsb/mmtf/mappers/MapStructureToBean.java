package org.rcsb.mmtf.mappers;

import org.apache.spark.api.java.function.Function;
import org.biojava.nbio.structure.Structure;
import org.rcsb.mmtf.databeans.DataBean;

public class MapStructureToBean implements Function<Structure, DataBean>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2502263824396751153L;
	private StructureToData dataTransform;
	
	public MapStructureToBean(StructureToData inputInterface){
		this.dataTransform = inputInterface;
	}

	@Override
	public DataBean call(Structure inStruct) throws Exception {
		// TODO Auto-generated method stub
		return dataTransform.getBeanFromStruct(inStruct);
	}
	
	
	

}
