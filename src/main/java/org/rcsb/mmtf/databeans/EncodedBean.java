/**
 * 
 */
package org.rcsb.mmtf.databeans;

import java.io.Serializable;

/**
 * @author Yana Valasatava
 *
 */
public class EncodedBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5284929180611073897L;
	
	/** The x coord big. 4 byte integers in pairs. */
	private byte[] xCoordBig;

	/** The y coord big. 4 byte integers in pairs. */
	private byte[] yCoordBig;

	/** The z coord big. 4 byte integers in pairs. */
	private byte[] zCoordBig;

	/** The x coord small. 2 byte integers. */
	private byte[] xCoordSmall;

	/** The y coord small. 2 byte integers.*/
	private byte[] yCoordSmall;

	/** The z coord small. 2 byte integers.*/
	private byte[] zCoordSmall;
	
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

	public byte[] getxCoordBig() {
		return xCoordBig;
	}

	public void setxCoordBig(byte[] xCoordBig) {
		this.xCoordBig = xCoordBig;
	}

	public byte[] getyCoordBig() {
		return yCoordBig;
	}

	public void setyCoordBig(byte[] yCoordBig) {
		this.yCoordBig = yCoordBig;
	}

	public byte[] getzCoordBig() {
		return zCoordBig;
	}

	public void setzCoordBig(byte[] zCoordBig) {
		this.zCoordBig = zCoordBig;
	}

	public byte[] getxCoordSmall() {
		return xCoordSmall;
	}

	public void setxCoordSmall(byte[] xCoordSmall) {
		this.xCoordSmall = xCoordSmall;
	}

	public byte[] getyCoordSmall() {
		return yCoordSmall;
	}

	public void setyCoordSmall(byte[] yCoordSmall) {
		this.yCoordSmall = yCoordSmall;
	}

	public byte[] getzCoordSmall() {
		return zCoordSmall;
	}

	public void setzCoordSmall(byte[] zCoordSmall) {
		this.zCoordSmall = zCoordSmall;
	}	
}
