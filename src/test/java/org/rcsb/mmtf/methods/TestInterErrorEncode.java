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
import org.rcsb.mmtf.encoders.InterErrorEncode;
import org.rcsb.mmtf.ensembles.Connection;
import org.rcsb.mmtf.utils.ArrayUtils;
import org.rcsb.mmtf.utils.StructureUtils;

import scala.Tuple2;

/**
 * @author Yana Valasatava
 *
 */
public class TestInterErrorEncode {

	@Test
	public void TestGetEncodedCoordinatesReference() throws IOException {

		ArrayUtils utils = new ArrayUtils();
		
		int lenght = 5;
		Point3d[] v1in = utils.generateVectorPoint3d(lenght);
		Point3d[] v2in = utils.multiplyVectorPoint3dBy(utils.generateVectorPoint3d(lenght), 2);
		
		// create array with known values
		List<Point3d[]> inArray = new ArrayList<Point3d[]>();
		inArray.add(v1in);
		inArray.add(v2in);
		
		List<Connection<Short>> connections = new ArrayList<Connection<Short>>();
		connections.add(new Connection<Short>((short) 0, (short) 1));
		
		// apply INTRAmolecular encoding to the first vector
		List<Integer> v1outX = new ArrayList<Integer>(Arrays.asList(1,1,1,1,1));
		v1outX = utils.multiplyIntegerArrayBy(v1outX, 1000);
		List<Integer> v1outY = utils.multiplyIntegerArrayBy(v1outX, 2);
		List<Integer> v1outZ = utils.multiplyIntegerArrayBy(v1outX, 3);
		
		// INTERmolecular encoding for other
		List<Integer> v2outX = new ArrayList<Integer>(Arrays.asList(1,1,1,1,1));
		v2outX = utils.multiplyIntegerArrayBy(v2outX, 1000);
		List<Integer> v2outY = utils.multiplyIntegerArrayBy(v2outX, 2);
		List<Integer> v2outZ = utils.multiplyIntegerArrayBy(v2outX, 3);
		
		List<List<Integer>> outArray = new ArrayList<List<Integer>>();
		
		v1outX.addAll(v2outX);
		outArray.add(v1outX);
		
		v1outY.addAll(v2outY);
		outArray.add(v1outY);

		v1outZ.addAll(v2outZ);
		outArray.add(v1outZ);
		
		InterErrorEncode encoder = new InterErrorEncode();
		//encoder.run(inArray, connections);
		//List<List<Integer>> testArray = encoder.getEncodedCoordinates();
		//assertEquals(outArray, testArray);
	}

	@Test
	public void TestGetEncodedCoordinatesWaterfall() throws IOException {

		ArrayUtils utils = new ArrayUtils();
		
		int lenght = 5;
		Point3d[] v1in = utils.generateVectorPoint3d(lenght);
		Point3d[] v2in = utils.multiplyVectorPoint3dBy(utils.generateVectorPoint3d(lenght), 2);
		Point3d[] v3in = utils.multiplyVectorPoint3dBy(utils.generateVectorPoint3d(lenght), 3);
		
		// create array with known values
		List<Point3d[]> inArray = new ArrayList<Point3d[]>();
		inArray.add(v1in);
		inArray.add(v2in);
		inArray.add(v3in);
		
		List<Connection<Short>> connections = new ArrayList<Connection<Short>>();
		for (short i=0; i < 2; i++) {
			connections.add(new Connection<Short>(i, (short) (i+1)));
		}
		
		List<Integer> v1outX = new ArrayList<Integer>(Arrays.asList(1,1,1,1,1));
		v1outX = utils.multiplyIntegerArrayBy(v1outX, 1000);
		List<Integer> v1outY = utils.multiplyIntegerArrayBy(v1outX, 2);
		List<Integer> v1outZ = utils.multiplyIntegerArrayBy(v1outX, 3);

		List<Integer> v2outX = new ArrayList<Integer>(Arrays.asList(1,1,1,1,1));
		v2outX = utils.multiplyIntegerArrayBy(v2outX, 1000);
		List<Integer> v2outY = utils.multiplyIntegerArrayBy(v2outX, 2);
		List<Integer> v2outZ = utils.multiplyIntegerArrayBy(v2outX, 3);

		List<Integer> v3outX = new ArrayList<Integer>(Arrays.asList(1,1,1,1,1));
		v3outX = utils.multiplyIntegerArrayBy(v3outX, 1000);
		List<Integer> v3outY = utils.multiplyIntegerArrayBy(v3outX, 2);
		List<Integer> v3outZ = utils.multiplyIntegerArrayBy(v3outX, 3);
		
		List<List<Integer>> outArray = new ArrayList<List<Integer>>();
		
		v1outX.addAll(v2outX);
		v1outX.addAll(v3outX);
		outArray.add(v1outX);
		
		v1outY.addAll(v2outY);
		v1outY.addAll(v3outY);
		outArray.add(v1outY);

		v1outZ.addAll(v2outZ);
		v1outZ.addAll(v3outZ);
		outArray.add(v1outZ);
		
		InterErrorEncode encoder = new InterErrorEncode();
		//encoder.run(inArray, connections);
		//List<List<Integer>> testArray = encoder.getEncodedCoordinates();
		//assertEquals(outArray, testArray);
	}

	@Test
	public void TestGetEncodedCoordinatesMST() throws IOException {

		ArrayUtils utils = new ArrayUtils();
		
		int lenght = 5;
		Point3d[] v1in = utils.generateVectorPoint3d(lenght);
		Point3d[] v2in = utils.multiplyVectorPoint3dBy(utils.generateVectorPoint3d(lenght), 2);
		Point3d[] v3in = utils.multiplyVectorPoint3dBy(utils.generateVectorPoint3d(lenght), 3);
		
		// create array with known values
		List<Point3d[]> inArray = new ArrayList<Point3d[]>();
		inArray.add(v1in);
		inArray.add(v2in);
		inArray.add(v3in);
		
		List<Connection<Short>> connections = new ArrayList<Connection<Short>>();
		connections.add(new Connection<Short>((short) 0, (short) 1));
		connections.add(new Connection<Short>((short) 0, (short) 2));
		
		List<Integer> v1outX = new ArrayList<Integer>(Arrays.asList(1,1,1,1,1));
		v1outX = utils.multiplyIntegerArrayBy(v1outX, 1000);
		List<Integer> v1outY = utils.multiplyIntegerArrayBy(v1outX, 2);
		List<Integer> v1outZ = utils.multiplyIntegerArrayBy(v1outX, 3);

		List<Integer> v2outX = new ArrayList<Integer>(Arrays.asList(1,1,1,1,1));
		v2outX = utils.multiplyIntegerArrayBy(v2outX, 1000);
		List<Integer> v2outY = utils.multiplyIntegerArrayBy(v2outX, 2);
		List<Integer> v2outZ = utils.multiplyIntegerArrayBy(v2outX, 3);

		List<Integer> v3outX = new ArrayList<Integer>(Arrays.asList(2,2,2,2,2));
		v3outX = utils.multiplyIntegerArrayBy(v3outX, 1000);
		List<Integer> v3outY = utils.multiplyIntegerArrayBy(v3outX, 2);
		List<Integer> v3outZ = utils.multiplyIntegerArrayBy(v3outX, 3);
		
		List<List<Integer>> outArray = new ArrayList<List<Integer>>();
		
		v1outX.addAll(v2outX);
		v1outX.addAll(v3outX);
		outArray.add(v1outX);
		
		v1outY.addAll(v2outY);
		v1outY.addAll(v3outY);
		outArray.add(v1outY);

		v1outZ.addAll(v2outZ);
		v1outZ.addAll(v3outZ);
		outArray.add(v1outZ);
		
		InterErrorEncode encoder = new InterErrorEncode();
		//encoder.run(inArray, connections);
		//List<List<Integer>> testArray = encoder.getEncodedCoordinates();
		//assertEquals(outArray, testArray);
	}

	@Test
	public void TestReferenceOn1A1U() throws IOException, StructureException {
	
		String pdbCode = "1A1U";
		Structure structure = StructureIO.getStructure(pdbCode);

		StructureUtils sU = new StructureUtils(); 
		Tuple2<String, List<Point3d[]>> coordinates = sU.getCoordinatesAsTuple2(new Tuple2<String, Structure>(structure.getPDBCode(), structure), true);
		if (! sU.sameSize(coordinates._2()))
			System.out.println("not ok");
		
		System.out.println("ok");
	}
}