/**
 * 
 */
package org.rcsb.mmtf.mappers;

import javax.vecmath.Point3d;

import org.rcsb.mmtf.databeans.ConnectionBean;
import scala.Tuple2;

/**
 * @author Yana Valasatava
 *
 */
public interface StructureToConnectivity {
	public String getName();
	public Tuple2<String, ConnectionBean> getConnectionsFromCoordinates(Tuple2<String, Point3d[][]> t);
}
