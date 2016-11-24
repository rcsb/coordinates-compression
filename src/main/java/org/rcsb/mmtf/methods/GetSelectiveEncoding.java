/**
 * 
 */
package org.rcsb.mmtf.methods;

import javax.vecmath.Point3d;

import org.apache.spark.api.java.function.PairFunction;
import org.rcsb.mmtf.codec.ArrayConverters;
import org.rcsb.mmtf.compression.GzipCompression;
import org.rcsb.mmtf.databeans.ConnectionBean;
import org.rcsb.mmtf.databeans.CoordinatesBean;
import org.rcsb.mmtf.dataholders.MmtfStructure;
import org.rcsb.mmtf.decoders.MmtfCustomDecoder;
import org.rcsb.mmtf.encoders.InterCoder;
import org.rcsb.mmtf.intracoders.IntegerTransform;
import org.rcsb.mmtf.intracoders.RecursiveIndexingCoder;
import org.rcsb.mmtf.mappers.StructureToConnectivity;
import org.rcsb.mmtf.utils.ArrayUtils;
import org.rcsb.mmtf.utils.Convertors;
import org.rcsb.mmtf.utils.MmtfUtils;
import org.rcsb.mmtf.utils.StructureUtils;
import org.rcsb.mmtf.utils.TraversalUtils;

import scala.Tuple2;

/**
 * @author Yana Valasatava
 *
 */
public class GetSelectiveEncoding implements PairFunction <Tuple2<String, MmtfStructure>, String, Integer> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -142195730074945767L;

	float multiplier;
	boolean caTrace;
	IntegerTransform encoder1;
	StructureToConnectivity traversing;
	InterCoder encoder2;

	public GetSelectiveEncoding(boolean caTrace, float multiplier, IntegerTransform encoder1, StructureToConnectivity traversing, InterCoder encoder2) {
		this.caTrace = caTrace;
		this.multiplier = multiplier;
		this.encoder1 = encoder1;
		this.traversing = traversing;
		this.encoder2 = encoder2;
	}

	@Override
	public Tuple2<String, Integer> call(Tuple2<String, MmtfStructure> t) throws Exception {

		String encoding="";
		int size=0;
		try {

			float[][][] decoded = MmtfCustomDecoder.decodeCoordinates(t._2, caTrace);
			if ( ! ArrayUtils.hasDataPoints(decoded, 3) ) {
				return new Tuple2<String, Integer> (t._1, 0);
			}
			
			// intra

			float[] decodedFlat = Convertors.nestedArrayToFlat(decoded);
			int[] decodedInt = ArrayConverters.convertFloatsToInts(decodedFlat, multiplier);

			int n = decodedInt.length/3; // number of atoms

			int[] x = new int[n];
			int[] y = new int[n];
			int[] z = new int[n];

			System.arraycopy(decodedInt, 0, x, 0, n);
			System.arraycopy(decodedInt, n, y, 0, n);
			System.arraycopy(decodedInt, n+n, z, 0, n);

			int[] xCoods = encoder1.run(x);
			int[] yCoods = encoder1.run(y);
			int[] zCoods = encoder1.run(z);

			int[] encodedIntra = ArrayUtils.mergeCoordinatesArrays(xCoods, yCoods, zCoods);
			int max = ArrayUtils.getMax(encodedIntra);
			RecursiveIndexingCoder recInd = new RecursiveIndexingCoder(max);
			int[] outIntra = recInd.encode(encodedIntra);

			byte[] outIntraBA = Convertors.arrayToByteArray(outIntra, Short.MAX_VALUE);
			int size1 = GzipCompression.compress(outIntraBA).length;

			size = size1;
			encoding = "intra_delta;structure";
			int size2 = 0;
			String encoding2 = "";

			// inter (if possible)
			if ( MmtfUtils.hasAllChainsIdentical(t._2) ) {	

				if ( !ArrayUtils.sameChainLength(decoded) ) {
					return new Tuple2<String, Integer> (t._1+";"+encoding, size);
				}

				//process chains
				Point3d[][][] points = Convertors.arrayFloatToPoint3dArray(decoded);
				Point3d[][] structure = StructureUtils.arrayToChains(points);

				Tuple2<String, ConnectionBean> dataBean = traversing.getConnectionsFromCoordinates( new Tuple2<String, Point3d[][]>(t._1, structure) );
				CoordinatesBean coordBean = TraversalUtils.connectivityToCoordinatesBean(dataBean._2, multiplier);

				int[] encoded = encoder2.run(coordBean);
				int max2 = ArrayUtils.getMax(encoded);
				RecursiveIndexingCoder recInd2 = new RecursiveIndexingCoder(max2);
				int[] outInter = recInd2.encode(encoded);

				byte[] outInterBA = Convertors.arrayToByteArray(outInter, Short.MAX_VALUE);
				size2 = GzipCompression.compress(outInterBA).length;
				encoding2 = "inter_delta;chains";	
			}
			else if ( t._2.getChainsPerModel().length > 1 ) {

				if ( !ArrayUtils.sameSize(decoded) ) {
					return new Tuple2<String, Integer> (t._1+";"+encoding, size);
				}

				//process models
				Point3d[][][] points = Convertors.arrayFloatToPoint3dArray(decoded);
				Point3d[][] structure = StructureUtils.arrayToModels(points);

				Tuple2<String, ConnectionBean> dataBean = traversing.getConnectionsFromCoordinates( new Tuple2<String, Point3d[][]>(t._1, structure) );
				CoordinatesBean coordBean = TraversalUtils.connectivityToCoordinatesBean(dataBean._2, multiplier);

				int[] encoded = encoder2.run(coordBean);
				int max2 = ArrayUtils.getMax(encoded);
				RecursiveIndexingCoder recInd2 = new RecursiveIndexingCoder(max2);
				int[] outInter = recInd2.encode(encoded);

				byte[] outInterBA = Convertors.arrayToByteArray(outInter, Short.MAX_VALUE);
				size2 = GzipCompression.compress(outInterBA).length;
				encoding2 = "inter_delta;models";
			}

			if ( size2 == 0) {
				return new Tuple2<String, Integer> (t._1+";"+encoding, size);
			}
			if (size2 < size ) {
				size = size2;
				encoding = encoding2;
			}
		} catch (Exception e) {
			System.out.println(t._1);
			e.printStackTrace();
		}
		return new Tuple2<String, Integer> (t._1+";"+encoding, size);
	}


}
