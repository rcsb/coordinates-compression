package org.rcsb.mmtf.ensembles;

/**
 * Connection data store.
 * A connection is defined as each a pair of indices
 * 
 * @author Yana Valasatava
 */
public class Connection<T> {
	
	public T p1, p2;
	public Connection(T p1, T p2) {
		this.p1 = p1;
		this.p2 = p2;
	}
}
