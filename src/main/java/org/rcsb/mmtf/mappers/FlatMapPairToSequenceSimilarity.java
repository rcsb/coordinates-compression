/**
 * 
 */
package org.rcsb.mmtf.mappers;

import java.util.List;

import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.broadcast.Broadcast;
import org.biojava.nbio.alignment.Alignments;
import org.biojava.nbio.alignment.SimpleGapPenalty;
import org.biojava.nbio.alignment.Alignments.PairwiseSequenceAlignerType;
import org.biojava.nbio.alignment.template.GapPenalty;
import org.biojava.nbio.alignment.template.PairwiseSequenceAligner;
import org.biojava.nbio.core.alignment.matrices.SubstitutionMatrixHelper;
import org.biojava.nbio.core.alignment.template.SequencePair;
import org.biojava.nbio.core.alignment.template.SubstitutionMatrix;
import org.biojava.nbio.core.sequence.ProteinSequence;
import org.biojava.nbio.core.sequence.compound.AminoAcidCompound;

import scala.Tuple2;

/**
 * @author Yana Valasatava
 *
 */
public class FlatMapPairToSequenceSimilarity implements PairFunction<Tuple2<Integer,Integer>,String,Float> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6632893807317834772L;
	
	private SubstitutionMatrix<AminoAcidCompound> matrix;
	private GapPenalty penalty;
	private int gop;
	private int extend;
	private List<Tuple2<String, String>> data;
	
	public FlatMapPairToSequenceSimilarity(Broadcast<List<Tuple2<String,String>>> input) {
		
		data = input.getValue();

		matrix = SubstitutionMatrixHelper.getBlosum65();
		penalty = new SimpleGapPenalty();
		gop = 8;
		extend = 1;
		penalty.setOpenPenalty(gop);
		penalty.setExtensionPenalty(extend);
	}

	@Override
	public Tuple2<String, Float> call(Tuple2<Integer, Integer> t) throws Exception {
		ProteinSequence ps1 = new ProteinSequence(data.get(t._1)._2);
		ProteinSequence ps2 = new ProteinSequence(data.get(t._2)._2);

		PairwiseSequenceAligner<ProteinSequence, AminoAcidCompound> smithWaterman = Alignments.getPairwiseAligner(ps1, ps2, PairwiseSequenceAlignerType.LOCAL, penalty, matrix);
		// Get the length - as the longest portion
		int length = ps1.getLength();
		if (ps2.getLength() > length) {
			length = ps2.getLength();
		}
		
		SequencePair<ProteinSequence, AminoAcidCompound> getThePair = smithWaterman.getPair();
		System.out.println(getThePair);
		
		return new Tuple2<String, Float>(data.get(t._1)._1+"__"+data.get(t._2)._1, (float) smithWaterman.getSimilarity());
	}

}