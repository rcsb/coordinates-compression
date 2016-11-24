package org.rcsb.mmtf.compression;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.zip.GZIPOutputStream;

public class GzipCompression implements Serializable {

	private static final long serialVersionUID = -8439555600858801737L;
	
	public static String name = "gzip";
	
	public static byte[] compress(byte[] inArr) throws IOException{

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        GZIPOutputStream gzipOS = new GZIPOutputStream(bos);
        gzipOS.write(inArr, 0, inArr.length);
        gzipOS.close();
        
        return bos.toByteArray();
	}
	
	public static String name() {
		return name;
	}
}
