/**
 * 
 */
package org.rcsb.mmtf.compression;

import static org.junit.Assert.assertTrue;

import javax.vecmath.Point3d;

import org.junit.Test;
import org.rcsb.mmtf.ensembles.Metrics;
import org.rcsb.mmtf.utils.ArrayUtils;

/**
 * @author Yana Valasatava
 *
 */
public class TestsMetrics {
	
	@Test
	public void TestRunRMSD() {
		
		ArrayUtils utils = new ArrayUtils();
		
		int lenght = 3;
		Point3d[] v1 = utils.generateVectorPoint3d(lenght);
		Point3d[] v2 = utils.multiplyVectorPoint3dBy(utils.generateVectorPoint3d(lenght), 2);
		
		double valOut = Math.sqrt(((double) 196)/3);
				
		Metrics m = new Metrics();
		double valTest = m.runRMSD(v1, v2);
		
		assertTrue(valOut==valTest);
	}
}
