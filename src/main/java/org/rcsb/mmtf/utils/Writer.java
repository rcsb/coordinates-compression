/**
 * 
 */
package org.rcsb.mmtf.utils;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Optional;
import com.google.common.io.Files;

import scala.Tuple2;

/**
 * @author Yana Valasatava
 *
 */
public class Writer implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2732518248017347054L;

	/**
	 * @param inList
	 * @param uri
	 * @throws IOException
	 */
	public void writeLongDoubleToFile(List<Tuple2<String, Tuple2<Long, Optional<Double>>>> inList, String uri) throws IOException {
		
		File f = new File(uri);
		for (Tuple2<String, Tuple2<Long, Optional<Double>>> t : inList) {
			Files.append(t._1+";"+String.format("%d;%.3f", t._2._1, t._2._2.get())+"\n", f, Charset.defaultCharset());
		}
	}

	/**
	 * @param inList
	 * @param uri
	 * @throws IOException
	 */
	public void writeLongToFile(List<Tuple2<String, Long>> inList, String uri) throws IOException {
		
		File f = new File(uri);
		for (Tuple2<String, Long> t : inList) {
			Files.append(t._1+";"+String.format("%d", t._2)+"\n", f, Charset.defaultCharset());
		}
	}

	/**
	 * @param inList
	 * @param uri
	 * @throws IOException
	 */
	public void writeDoubleToFile(List<Tuple2<String, Double>> inList, String uri) throws IOException {
		
		File f = new File(uri);
		for (Tuple2<String, Double> t : inList) {
			Files.append(t._1+";"+String.format("%.3f", t._2)+"\n", f, Charset.defaultCharset());
		}	
	}

	/**
	 * @param out
	 * @param uri
	 * @throws IOException 
	 */
	public void writeListDoublesToFile(List<Tuple2<String, List<Double>>> inList, String uri) throws IOException {
		
		File f = new File(uri);
		for (Tuple2<String, List<Double>> t : inList) {
			List<String> strs = new ArrayList<String>();
			t._2.forEach( val -> strs.add(String.format("%.3f",val)));
			String line = String.join(";", strs);      
			Files.append(t._1+";"+line+"\n", f, Charset.defaultCharset());
		}
	}

	/**
	 * @param inList
	 * @param uri
	 * @throws IOException 
	 */
	public void writeIntegersToFile(List<Tuple2<String, Integer>> inList, String uri) throws IOException {
		File f = new File(uri);
		for (Tuple2<String, Integer> t : inList) {
			Files.append(t._1+";"+String.format("%d", t._2)+"\n", f, Charset.defaultCharset());
		}
	}

	/**
	 * @param inList
	 * @param uri
	 * @throws IOException 
	 */
	public void writeShortToFile(List<Tuple2<String, Short>> inList, String uri) throws IOException {
		File f = new File(uri);
		for (Tuple2<String, Short> t : inList) {
			Files.append(t._1+";"+String.format("%s", Short.toString(t._2))+"\n", f, Charset.defaultCharset());
		}
	}
	
}
