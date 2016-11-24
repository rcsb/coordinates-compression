/**
 * 
 */
package org.rcsb.mmtf.databeans;

import java.io.Serializable;

/**
 * @author Yana Valasatava
 *
 */
public class CoordinatesBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5284929180611073897L;
	
	private boolean supFlag;
	public boolean isSup() {
		return supFlag;
	}
	public void setSupFlag(boolean supFlag) {
		this.supFlag = supFlag;
	}
	
	private int[][] coordinates;
	
	public int[][] getCoordinates() {
		return coordinates;
	}
	public void setCoordinates(int[][] coordinates) {
		this.coordinates = coordinates;
	}
	/** The connectivity between sets of coordinates in pairs*/
	private short[] connectivity;
	private double[] translations;
	private double[] quaternions;
	
	public void setTranslations(double[] translations) {
		this.translations = translations;
	}
	
	public double[] getTranslations() {
		return this.translations;
	}

	public double[] getQuaternions() {
		return quaternions;
	}

	public void setQuaternions(double[] quaternions) {
		this.quaternions = quaternions;
	}
	
	public short[] getConnectivity() {
		return connectivity;
	}

	public void setConnectivity(short[] connectivity) {
		this.connectivity = connectivity;
	}
	
	private String metric; // metric used for MST
	
	public String getMetricUsesEncoding() {
		return metric;
	}
	public void setMetricUsesEncoding(String metric) {
		this.metric = metric;
	}
}
