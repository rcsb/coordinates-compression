package org.rcsb.mmtf.databeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Point3d;

import org.rcsb.mmtf.ensembles.Connection;

public class ConnectionBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3140002697318749903L;

	private boolean supFlag;
	private List<Connection<Short>> connections;
	private List<Point3d[]> coordinates;
	
	private String metricUsesEncoding;
	
	public ConnectionBean () {
		coordinates = new ArrayList<Point3d[]>();
	}
	
	public boolean isSup() {
		return supFlag;
	}
	public void setSupFlag(boolean supFlag) {
		this.supFlag = supFlag;
	}
	public List<Connection<Short>> getConnections() {
		return connections;
	}
	public void setConnections(List<Connection<Short>> connections) {
		this.connections = connections;
	}
	public List<Point3d[]> getCoordinates() {
		return coordinates;
	}
	public void setCoordinates(List<Point3d[]> coordinates) {
		this.coordinates = coordinates;
	}
	public String getMetricUsesEncoding() {
		return metricUsesEncoding;
	}
	public void setMetricUsesEncoding(String metricUsesEncoding) {
		this.metricUsesEncoding = metricUsesEncoding;
	}
	public void addCoordinates(Point3d[] coords) {
		this.coordinates.add(coords);
	}
	
	/** The connectivity between sets of coordinates in pairs*/
	private short[] connectivity;

	private List<double[]> translations;
	private List<double[]> quaternions;
	
	public void setTranslations(List<double[]> translations) {
		this.translations = translations;
	}
	
	public List<double[]> getTranslations() {
		return this.translations;
	}
	
	public void addTranslations(double[] t) {
		if (this.translations==null)
			translations = new ArrayList<double[]>();
		this.translations.add(t);
	}
	
	public List<double[]> getQuaternions() {
		return quaternions;
	}

	public void setQuaternions(List<double[]> quaternions) {
		this.quaternions = quaternions;
	}
	
	public void addQuaternion(double[] q) {
		if (this.quaternions==null)
			quaternions = new ArrayList<double[]>();
		this.quaternions.add(q);
	}
	
	public short[] getConnectivity() {
		return connectivity;
	}

	public void setConnectivity(short[] connectivity) {
		this.connectivity = connectivity;
	}
}
