
package org.rcsb.mmtf.compression;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;

import org.meteogroup.jbrotli.Brotli.Mode;
import org.meteogroup.jbrotli.Brotli.Parameter;
import org.meteogroup.jbrotli.BrotliStreamCompressor;
import org.meteogroup.jbrotli.libloader.BrotliLibraryLoader;

/**
 * Brotli (https://www.ietf.org/rfc/rfc7932.txt) is a fast compression algorithm developed by Google. 
 * Brotli uses the combination of LZ77 and Huffman coding. 
 * http://www.gstatic.com/b/brotlidocs/brotli-2015-09-22.pdf 
 * 
 * @author Yana Valasatava
 */
public class BrotliCompression implements Serializable {
	
	private static final long serialVersionUID = -7785599485877084717L;

	public static String name = "brotli";
	
	public static byte[] compress (byte[] in) throws IOException {
		
		BrotliLibraryLoader.loadBrotli();
		
		boolean doFlush = true;
		
		Parameter param = new Parameter(Mode.GENERIC, 11, 10, 24);
		BrotliStreamCompressor streamCompressor = new BrotliStreamCompressor(param);
		
		int maxSize = streamCompressor.getMaxInputBufferSize();
		
		byte[] compressed;
		try {
			if (in.length <= maxSize) {
				compressed = streamCompressor.compressArray(in, 0, in.length, doFlush);
			}
			else {
				
				ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );

				int inPosition = 0;
				while (inPosition < in.length) {
					
					if (inPosition+maxSize > in.length) {
						maxSize = in.length - inPosition;
					}
					compressed = streamCompressor.compressArray(in, inPosition, maxSize, doFlush);
					outputStream.write( compressed );
					inPosition += maxSize;
				}
				compressed = outputStream.toByteArray();
			}		
			streamCompressor.close();

			return compressed;
		
		} catch (Exception e) {
			return null;
		}
	}
	
	public static String name() {
		return name;
	}
}
