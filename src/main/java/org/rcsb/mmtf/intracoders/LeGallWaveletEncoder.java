package org.rcsb.mmtf.intracoders;

import java.io.Serializable;

public class LeGallWaveletEncoder implements IntegerTransform, Serializable {
	
	private static final long serialVersionUID = -1368936471810062865L;

	/**
	 * Class that represents DiscreteWaveletTransform on Le Gall filter bank
	 * basis using so-called "lifting" scheme. Also it performs
	 * integer-to-integer wavelet transform. It means that for image
	 * data(integer data) decomposition will produce integer coefficients.
	 *
	 * @author Kirilchuk V.E.
	 * @author Peter Rose (adopted to IntegerTransform)
	 */
	
	@Override
	public int[] run(int[] in) {
		int[] out = new int[in.length];
		System.arraycopy(in, 0,  out,  0,  in.length);

		int n = out.length;
		while (n > 1) {
			decompose(out, n);
			n /= 2;
		}
		return out;
	}

	public int[] reverse(int[] in) {
		int[] out = new int[in.length];
		System.arraycopy(in, 0,  out,  0,  in.length);
		
		int n = 1;
		while (n < out.length) {
			reconstruct(out, n);
			n *= 2;
		}
		
		return out;
	}

	private static void reconstruct(int[] x, int n) {
		int endIndex = n * 2;

		// Unpack
		int[] temp = new int[endIndex];
		for (int i = 0; i < n; i++) {
			temp[i * 2] = x[i];
			temp[i * 2 + 1] = x[i + n];
		}
		System.arraycopy(temp, 0, x, 0, endIndex);

		// Undo update
		for (int i = 2; i < endIndex; i += 2) {
			x[i] -= (x[i - 1] + x[i + 1] + 2) >> 2;
		}
		x[0] -= x[1];

		// Undo predict
		for (int i = 1; i < endIndex - 2; i += 2) {
			x[i] += (x[i - 1] + x[i + 1]) >> 1;
		}
		x[endIndex - 1] += x[endIndex - 2];
	}

	/**
	 * +1 level of decomposition.
	 *
	 * <pre>
	 * For example:
	 * We have vector 1,1,1,1,2,2,2,2 where 1 are approximation and 2 are details.
	 * To decompose one more time we need call
	 * decomposeInplace1Lvl([1,1,1,1,2,2,2,2], 4);
	 * 4 - index where details start and approximations ended.
	 * </pre>
	 *
	 * @param x
	 *            vector with approximation and details.
	 * @param endIndex
	 *            index where details start and approximations ended.
	 */
	private static void decompose(int[] x, int endIndex) {
		// Predict
		for (int i = 1; i < endIndex - 2; i += 2) {
			x[i] -= (x[i - 1] + x[i + 1]) >> 1;
		}
		x[endIndex - 1] -= x[endIndex - 2];

		// Update
		//for (int i = 2; i < endIndex; i += 2) {
		for (int i = 2; i < endIndex-2; i += 2) {
			x[i] += (x[i - 1] + x[i + 1] + 2) >> 2;
		}
		x[0] += x[1];

		// Pack
		int[] temp = new int[endIndex];
		for (int i = 0; i < endIndex; i++) {
			if (i % 2 == 0) {
				temp[i / 2] = x[i];
			} else {
				temp[endIndex / 2 + i / 2] = x[i];
			}
		}

		System.arraycopy(temp, 0, x, 0, endIndex);
	}
}