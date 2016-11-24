/**
 * 
 */
package org.rcsb.mmtf.compression;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Matrix4d;
import javax.vecmath.Point3d;

import org.biojava.nbio.structure.symmetry.geometry.SuperPosition;
import org.junit.Test;
import org.rcsb.mmtf.ensembles.Connection;
import org.rcsb.mmtf.ensembles.Traverse;
import org.rcsb.mmtf.utils.ArrayUtils;

import com.fasterxml.jackson.core.JsonProcessingException;

import scala.Tuple2;

/**
 * @author Yana Valasatava
 *
 */
public class TestTraverse {
	
	@Test
	public void TestTraverseReferenceWoSup(){
		
		ArrayUtils utils = new ArrayUtils();

		int lenght = 3;
		Point3d[] v1 = utils.generateVectorPoint3d(lenght);
		Point3d[] v2 = utils.generateVectorPoint3d(lenght);
		
		Matrix4d mx = new Matrix4d(2,0,0,0,  0,2,0,0,  0,0,2,0,  0,0,0,2);
		SuperPosition.transform(mx, v2);
		
		// test input
		List<Point3d[]> inArray = new ArrayList<Point3d[]>();	
		inArray.add(v1);
		inArray.add(v2);
		
		Traverse t = new Traverse(false, "", "", "");
		Tuple2<List<Point3d[]>, List<Connection<Short>>> out = t.traverseReference(inArray);
		List<Point3d[]> testArray = out._1;
		
		assertEquals(inArray, testArray);
	}

	@Test
	public void TestTraverseWaterfallWoSup(){
		
		ArrayUtils utils = new ArrayUtils();

		int lenght = 3;
		Point3d[] v1 = utils.generateVectorPoint3d(lenght);
		Point3d[] v2 = utils.generateVectorPoint3d(lenght);
		
		Matrix4d mx = new Matrix4d(2,0,0,0,  0,2,0,0,  0,0,2,0,  0,0,0,2);
		SuperPosition.transform(mx, v2);

		// test input
		List<Point3d[]> inArray = new ArrayList<Point3d[]>();
		inArray.add(v1);
		inArray.add(v2);
		
		Traverse t = new Traverse(false, "", "", "");
		Tuple2<List<Point3d[]>, List<Connection<Short>>> out = t.traverseWaterfall(inArray);
		List<Point3d[]> testArray = out._1;
		
		assertEquals(inArray, testArray);
	}

	@Test
	public void TestTraverseMSTWoSup() throws JsonProcessingException, IOException{
		
		ArrayUtils utils = new ArrayUtils();

		int lenght = 3;
		Point3d[] v1 = utils.generateVectorPoint3d(lenght);
		Point3d[] v2 = utils.generateVectorPoint3d(lenght);
		
		Matrix4d mx = new Matrix4d(2,0,0,0,  0,2,0,0,  0,0,2,0,  0,0,0,2);
		SuperPosition.transform(mx, v2);

		// test input
		List<Point3d[]> inArray = new ArrayList<Point3d[]>();
		inArray.add(v1);
		inArray.add(v2);
		
		Traverse t = new Traverse(false, "mst", "rmsd", "inter");
		Tuple2<List<Point3d[]>, List<Connection<Short>>> out = t.traverseMinimumSpanningTree(inArray);
		List<Point3d[]> testArray = out._1;
		
		assertEquals(inArray, testArray);
	}
	
	@Test
	public void TestTraverseReferenceWSup(){
		
		ArrayUtils utils = new ArrayUtils();

		Point3d[] v1 = utils.generateUnitVectorPoint3d();
		Point3d[] v2 = utils.generateUnitVectorPoint3d();

		// test output
		List<Point3d[]> outArray = new ArrayList<Point3d[]>();
		outArray.add(v1);
		outArray.add(v2);

		Point3d[] v3 = new Point3d[2];
		v3[0] = new Point3d(1.0, 1.0, 0.0);
		v3[1] = new Point3d(2.0, 2.0, -1.0);

		// test input
		List<Point3d[]> inArray = new ArrayList<Point3d[]>();
		inArray.add(v1);
		inArray.add(v3);
		
		Traverse t = new Traverse(true, "", "", "");
		Tuple2<List<Point3d[]>, List<Connection<Short>>> out = t.traverseReference(inArray);
		List<Point3d[]> testArray = out._1;

		for (Point3d[] v : outArray)
			utils.roundVectorPoint3d(v, 3);
		for (Point3d[] v : testArray)
			utils.roundVectorPoint3d(v, 3);
		
		//assertEquals(outArray, testArray);
	}
}
