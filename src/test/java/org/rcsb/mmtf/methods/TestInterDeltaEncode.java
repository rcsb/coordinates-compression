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
import org.rcsb.mmtf.encoders.InterDeltaEncode;
import org.rcsb.mmtf.ensembles.Connection;
import org.rcsb.mmtf.utils.ArrayUtils;
import org.rcsb.mmtf.utils.StructureUtils;

import scala.Tuple2;

/**
 * @author Yana Valasatava
 *
 */
public class TestInterDeltaEncode {

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
		List<Integer> v2outX = new ArrayList<Integer>(Arrays.asList(1,2,3,4,5));
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
		
		InterDeltaEncode encoder = new InterDeltaEncode();
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

		List<Integer> v2outX = new ArrayList<Integer>(Arrays.asList(1,2,3,4,5));
		v2outX = utils.multiplyIntegerArrayBy(v2outX, 1000);
		List<Integer> v2outY = utils.multiplyIntegerArrayBy(v2outX, 2);
		List<Integer> v2outZ = utils.multiplyIntegerArrayBy(v2outX, 3);

		List<Integer> v3outX = new ArrayList<Integer>(Arrays.asList(1,2,3,4,5));
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
		
		InterDeltaEncode encoder = new InterDeltaEncode();
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

		List<Integer> v2outX = new ArrayList<Integer>(Arrays.asList(1,2,3,4,5));
		v2outX = utils.multiplyIntegerArrayBy(v2outX, 1000);
		List<Integer> v2outY = utils.multiplyIntegerArrayBy(v2outX, 2);
		List<Integer> v2outZ = utils.multiplyIntegerArrayBy(v2outX, 3);

		List<Integer> v3outX = new ArrayList<Integer>(Arrays.asList(2,4,6,8,10));
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
		
		InterDeltaEncode encoder = new InterDeltaEncode();
		//encoder.run(inArray, connections);
		//List<List<Integer>> testArray = encoder.getEncodedCoordinates();
		//assertEquals(outArray, testArray);
	}
	
	@Test
	public void TestOn4OLR_woSup() throws IOException, StructureException {
	
		String pdbCode = "4OLR";
		Structure structure = StructureIO.getStructure(pdbCode);

		StructureUtils sU = new StructureUtils(); 
		Tuple2<String, List<Point3d[]>> t = sU.getCoordinatesAsTuple2(new Tuple2<String, Structure>(structure.getPDBCode(), structure), true);
		List<Point3d[]> inArray = t._2;
		
		List<List<Integer>> outArray = new ArrayList<List<Integer>>();
		
		List<Integer> v1outX = new ArrayList<Integer>(Arrays.asList(-2612,-203,400,-245,279));
		List<Integer> v1outY = new ArrayList<Integer>(Arrays.asList(3164,-1784,1498,-1603,2285));
		List<Integer> v1outZ = new ArrayList<Integer>(Arrays.asList(-1791,3333,3462,3445,3016));

		List<Integer> v2outX = new ArrayList<Integer>(Arrays.asList(4773,5290,4395,4944,4294));
		List<Integer> v2outY = new ArrayList<Integer>(Arrays.asList(251,210,-12,-232,-299));
		List<Integer> v2outZ = new ArrayList<Integer>(Arrays.asList(12581,5946,-1043,-7796,-13901));
		
		v1outX.addAll(v2outX);
		outArray.add(v1outX);
		
		v1outY.addAll(v2outY);
		outArray.add(v1outY);

		v1outZ.addAll(v2outZ);
		outArray.add(v1outZ);

		List<Connection<Short>> connections = new ArrayList<Connection<Short>>();
		connections.add(new Connection<Short>((short) 0, (short) 1));
		
		InterDeltaEncode encoder = new InterDeltaEncode();
		//encoder.run(inArray, connections);
		//List<List<Integer>> testArray = encoder.getEncodedCoordinates();
		//assertEquals(outArray, testArray);
	}
	
	
}
