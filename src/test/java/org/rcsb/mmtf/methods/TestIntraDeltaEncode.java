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

import org.junit.Test;
import org.rcsb.mmtf.encoders.IntraDeltaEncode;
import org.rcsb.mmtf.utils.ArrayUtils;

/**
 * @author Yana Valasatava
 *
 */
public class TestIntraDeltaEncode {
	
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
		
		List<Integer> v1outX = new ArrayList<Integer>(Arrays.asList(1,1,1,1,1));
		v1outX = utils.multiplyIntegerArrayBy(v1outX, 1000);
		List<Integer> v1outY = utils.multiplyIntegerArrayBy(v1outX, 2);
		List<Integer> v1outZ = utils.multiplyIntegerArrayBy(v1outX, 3);

		List<Integer> v2outX = new ArrayList<Integer>(Arrays.asList(2,2,2,2,2));
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
		
		IntraDeltaEncode encoder = new IntraDeltaEncode();
		//encoder.run(inArray);
		//List<List<Integer>> testArray = encoder.getEncodedCoordinates();
		//assertEquals(outArray, testArray);	
	}
}
