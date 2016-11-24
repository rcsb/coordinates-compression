/**
 * 
 */
package org.rcsb.mmtf.methods;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.vecmath.Point3d;

import org.biojava.nbio.structure.Structure;
import org.biojava.nbio.structure.StructureException;
import org.biojava.nbio.structure.StructureIO;
import org.junit.Test;
import org.rcsb.mmtf.encoders.IntegerEncode;
import org.rcsb.mmtf.utils.ArrayUtils;
import org.rcsb.mmtf.utils.Convertors;
import org.rcsb.mmtf.utils.StructureUtils;

import scala.Tuple2;

/**
 * @author Yana Valasatava
 *
 */
public class TestIntegerEncode {

	@Test
	public void TestGetEncodedCoordinates() throws IOException {

		ArrayUtils utils = new ArrayUtils();
		
		int lenght = 5;
		Point3d[] v1in = utils.generateVectorPoint3d(lenght);
		Point3d[] v2in = utils.multiplyVectorPoint3dBy(utils.generateVectorPoint3d(lenght), 2);
		
		// create array with known values
		List<Point3d[]> inArray = new ArrayList<Point3d[]>();
		inArray.add(v1in);
		inArray.add(v2in);
		
		List<Integer> v1outX = new ArrayList<Integer>(Arrays.asList(1,2,3,4,5));
		v1outX = utils.multiplyIntegerArrayBy(v1outX, 1000);
		List<Integer> v1outY = utils.multiplyIntegerArrayBy(v1outX, 2);
		List<Integer> v1outZ = utils.multiplyIntegerArrayBy(v1outX, 3);

		List<Integer> v2outX = new ArrayList<Integer>(Arrays.asList(2,4,6,8,10));
		v2outX = utils.multiplyIntegerArrayBy(v2outX, 1000);
		List<Integer> v2outY = utils.multiplyIntegerArrayBy(v2outX, 2);
		List<Integer> v2outZ = utils.multiplyIntegerArrayBy(v2outX, 3);
		
		v1outX.addAll(v2outX);
		int[] v1outXint = Convertors.listIntegerToPrimitives(v1outX);
		
		v1outY.addAll(v2outY);
		int[] v1outYint = Convertors.listIntegerToPrimitives(v1outY);

		v1outZ.addAll(v2outZ);
		int[] v1outZint = Convertors.listIntegerToPrimitives(v1outZ);
		
		int[] out = ArrayUtils.mergeCoordinatesArrays(v1outXint, v1outYint, v1outZint);
		
		IntegerEncode encoder = new IntegerEncode();
		//encoder.run(inArray);
		
//		int[] test = utils.mergeCoordinatesArrays(encoder.getxCoods(), encoder.getyCoods(), encoder.getzCoods());
//		for (int i=0; i < out.length; i++) {
//			assertEquals(out[i], test[i]);
//		}	
	}

//	@Test
//	public void TestOn4OLR() throws IOException, StructureException {
//	
//		String pdbCode = "4OLR";
//		Structure structure = StructureIO.getStructure(pdbCode);
//
//		StructureUtils sU = new StructureUtils(); 
//		Tuple2<String, List<Point3d[]>> t = sU.getCoordinatesAsTuple2(new Tuple2<String, Structure>(structure.getPDBCode(), structure), true);
//		List<Point3d[]> inArray = t._2;
//		
//		List<List<Integer>> outArray = new ArrayList<List<Integer>>();
//		
//		List<Integer> v1outX = new ArrayList<Integer>(Arrays.asList(-2612,-2815,-2415,-2660,-2381));
//		List<Integer> v1outY = new ArrayList<Integer>(Arrays.asList(3164,1380,2878,1275,3560));
//		List<Integer> v1outZ = new ArrayList<Integer>(Arrays.asList(-1791,1542,5004,8449,11465));
//
//		List<Integer> v2outX = new ArrayList<Integer>(Arrays.asList(2161,2475,1980,2284,1913));
//		List<Integer> v2outY = new ArrayList<Integer>(Arrays.asList(3415,1590,2866,1043,3261));
//		List<Integer> v2outZ = new ArrayList<Integer>(Arrays.asList(10790,7488,3961,653,-2436));
//
//		v1outX.addAll(v2outX);
//		outArray.add(v1outX);
//		
//		v1outY.addAll(v2outY);
//		outArray.add(v1outY);
//
//		v1outZ.addAll(v2outZ);
//		outArray.add(v1outZ);
//		IntegerEncode encoder = new IntegerEncode();
//		encoder.run(inArray);
//		//List<List<Integer>> testArray = encoder.getEncodedCoordinates();
//		//assertEquals(outArray, testArray);
//	}
}
