package org.rcsb.mmtf.mappers;

import org.biojava.nbio.structure.Structure;
import org.rcsb.mmtf.databeans.DataBean;

public interface StructureToData {
	
	/**
	 * @param bioJavaStruct
	 * @return
	 */
	DataBean getBeanFromStruct(Structure bioJavaStruct);

}
