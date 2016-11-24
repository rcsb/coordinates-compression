/**
 * 
 */
package org.rcsb.mmtf.debug;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * 
 * Gets the size of custom objects
 * 
 * @author Yana Valasatava
 *
 */
public class ObjectSizer implements Serializable {
	
	private static final long serialVersionUID = -1686680150074497830L;

	public long getSize(Object obj) {
		
		byte[] b = serialize(obj);	
		return b.length;	
	}
	
	public static byte[] serialize(Object obj) {
		
	    if (obj == null) {
	        return new byte[0];
	    }
	    try (ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
	         ObjectOutputStream objectOut = new ObjectOutputStream(byteArrayOut)) {
	        objectOut.writeObject(obj);
	        return byteArrayOut.toByteArray();
	    } catch (final IOException e) {
	        e.printStackTrace();
	        return new byte[0];
	    }
	}
}
