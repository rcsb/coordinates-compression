/**
 * 
 */
package org.rcsb.mmtf.ensembles;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.vecmath.Matrix4d;
import javax.vecmath.Point3d;

import org.jgrapht.Graph;
import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.alg.PrimMinimumSpanningTree;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.jgrapht.traverse.BreadthFirstIterator;
import org.rcsb.mmtf.databeans.ConnectionBean;
import org.rcsb.mmtf.geometric.LeastSquaresFitting;
import org.rcsb.mmtf.geometric.Superposition;
import org.rcsb.mmtf.geometric.Transformation;
import org.rcsb.mmtf.mappers.StructureToConnectivity;

import com.fasterxml.jackson.core.JsonProcessingException;

import scala.Tuple2;

/**
 * @author Yana Valasatava
 *
 */
public class TraverseMST implements Serializable, StructureToConnectivity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2773967698530375001L;
	
	Boolean supFlag;
	String metric;
	String encoding;
	Superposition superposer;
	
	public TraverseMST(boolean supFlag, String metric, String encoding) {
		
		this.supFlag = supFlag;
		this.metric = metric;
		this.encoding = encoding;
		
		//this.superposer = new AbsoluteDeviationFitting();
		this.superposer = new LeastSquaresFitting();
	}

	public String getName() {
		
		String name = "mst_";
		name += metric;
		if (supFlag)
			name +="_sup";
		return name;
	}
	
	@Override
	public Tuple2<String, ConnectionBean> getConnectionsFromCoordinates(Tuple2<String, Point3d[][]> t) {
		
		ConnectionBean t2 = null;
		try {
			t2 = traverseMinimumSpanningTree(t._2, encoding);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return new Tuple2<String, ConnectionBean>(t._1, t2);
	}

	public ConnectionBean traverseMinimumSpanningTree(Point3d[][] coordinates, String encoding) throws JsonProcessingException, IOException {

		ConnectionBean cbean = new ConnectionBean();
		cbean.setSupFlag(supFlag);
		cbean.setMetricUsesEncoding(encoding);


		List<Connection<Short>> connections = getMinimumSpanningTreePath(coordinates, encoding);
		
		Transformation transformation = new Transformation();

		boolean flag = true;
		for (Connection<Short> c : connections) {

			Point3d[] coords_i = coordinates[(int) c.p1];
			if (flag) {
				cbean.addCoordinates(coords_i);
				flag = false;
			}
			Point3d[] coords_j = coordinates[c.p2];
			if (supFlag) {

				superposer.run(coords_j, coords_i);
				
				Matrix4d tM = superposer.getTransformationMatrix();
				coords_j = superposer.getSuperposedCoordanates();
				
				double[] q = transformation.getRotationAsQuaternion(tM);
				double[] t = transformation.getTranslationAsVector(tM);
				cbean.addQuaternion(q);
				cbean.addTranslations(t);
			}

			cbean.addCoordinates(coords_j);
		}

		cbean.setConnections(connections);

		return cbean;
	}
		
	public  List<Connection<Short>> getMinimumSpanningTreePath(Point3d[][] coordinates, String encoding) throws JsonProcessingException, IOException {
	
		Metrics m = new Metrics();
		m.setMetric(metric);
		m.setEncoding(encoding);
		
		ArrayList<ArrayList<Integer>> trackSegments = new ArrayList<ArrayList<Integer>>();
		
		ArrayList<Connection<Short>> connections = new ArrayList<Connection<Short>>();
		
		// no connectivity for single item in list
		if (coordinates.length == 1)
			return null;
		
		// connect two items in list
		else if (coordinates.length == 2)
			connections.add(new Connection<Short>((short) 0, (short) 1));	
 		
		else {
 			
			// initialize a weighted org.rcsb.mmtf.graph
			SimpleWeightedGraph<Integer, DefaultWeightedEdge> graph = new SimpleWeightedGraph<Integer, DefaultWeightedEdge>(DefaultWeightedEdge.class);
			
			// calculate weights between model based on a selected metric

			for (int i = 0; i < coordinates.length-1; i++) {
				for (int j = i+1; j < coordinates.length; j++) {

					Point3d[] coords_i = coordinates[i];
					Point3d[] coords_j = coordinates[j];

					if (supFlag == true) {
						
						superposer.run(coords_j, coords_i);
						coords_j = superposer.getSuperposedCoordanates();
					}
					
					double score = m.getScore(coords_j, coords_i);

					graph.addVertex(i);
					graph.addVertex(j);
					DefaultWeightedEdge e = graph.addEdge(i, j);
					graph.setEdgeWeight(e, score);
					
				}
			}

			//=======================================
			// A new org.rcsb.mmtf.graph is created out of minimum spanning tree and all models are added to a pool
			//=======================================
			
			// get the edges of a Minimum Spanning Tree generated for a weighted org.rcsb.mmtf.graph
			PrimMinimumSpanningTree<Integer, DefaultWeightedEdge> tree = new PrimMinimumSpanningTree<Integer, DefaultWeightedEdge>(graph);
			Set<DefaultWeightedEdge> mspEdges = tree.getMinimumSpanningTreeEdgeSet();
			
			// build a new org.rcsb.mmtf.graph from the MST edges
			Graph<Integer, DefaultEdge> mspEdgesGraph = new SimpleGraph<Integer, DefaultEdge>(DefaultEdge.class);
						
			Object[] edges = mspEdges.toArray();
			for (int k = 0; k < edges.length; k++) {
	
				DefaultWeightedEdge e = (DefaultWeightedEdge) edges[k];
	
				int sourceV = graph.getEdgeSource(e);
				int targetV = graph.getEdgeTarget(e);

				mspEdgesGraph.addVertex(sourceV);
				mspEdgesGraph.addVertex(targetV);

				mspEdgesGraph.addEdge(sourceV, targetV);
				
			}

			// Pool of vertices
			ArrayList<Integer> spotPool = new ArrayList<Integer>();
			for (int m_id=0; m_id < coordinates.length; m_id++) {spotPool.add(m_id);}
			
			// identify the most distant points in a org.rcsb.mmtf.graph: startVertex and endVertex
			
			// GraphIterator<Integer, DefaultEdge> iterator = new DepthFirstIterator<Integer, DefaultEdge>(mspEdgesGraph);
			BreadthFirstIterator<Integer, DefaultEdge> iteratorBack = new BreadthFirstIterator<Integer, DefaultEdge>(mspEdgesGraph, spotPool.get(0));
			int endVertex = 0;
			while (iteratorBack.hasNext()) {
				endVertex = iteratorBack.next();
			}
	
			BreadthFirstIterator<Integer, DefaultEdge> iteratorForth = new BreadthFirstIterator<Integer, DefaultEdge>(mspEdgesGraph, endVertex);
			int startVertex = 0;
			while (iteratorForth.hasNext()) {
				startVertex = iteratorForth.next();
			}
			
			// get the shortest path between starting and ending vertices
			
			DijkstraShortestPath<Integer, DefaultEdge> pathFinder = new DijkstraShortestPath<Integer, DefaultEdge>(mspEdgesGraph, startVertex, endVertex);
			List<DefaultEdge> path = pathFinder.getPathEdgeList();
	
			ArrayList<Integer> trackSegment = new ArrayList<Integer>();
			
			//======= get root ========
			
			int source, target;
			
			int source1 = mspEdgesGraph.getEdgeSource(path.get(0));
			int target1 = mspEdgesGraph.getEdgeTarget(path.get(0));
			
			int source2 = mspEdgesGraph.getEdgeSource(path.get(1));
			int target2 = mspEdgesGraph.getEdgeTarget(path.get(1));
	
			if (target1 == source2 || target1 == target2) {
				source = source1;
				target = target1;
			}
			else {
				target = source1;
				source = target1;
			}
			
			connections.add(new Connection<Short>((short) source,(short) target));
			
			trackSegment.add(source);
			spotPool.remove(spotPool.indexOf(source));
			trackSegment.add(target);
			spotPool.remove(spotPool.indexOf(target));
	
			for (int p=1; p< path.size(); p++) {
	
				int sourceVnext = mspEdgesGraph.getEdgeSource(path.get(p));
				int targetVnext = mspEdgesGraph.getEdgeTarget(path.get(p));
	
				if (sourceVnext != target) {
					target = sourceVnext;
					source = targetVnext;
				}
				else {
					target = targetVnext;
					source = sourceVnext;
				}
				
				connections.add(new Connection<Short>((short) source,(short) target));
				trackSegment.add(target);
				spotPool.remove(spotPool.indexOf(target));
			}
	
			trackSegments.add(trackSegment);
			
			ArrayList<Integer> leafs = new ArrayList<Integer>();
			
			// ======= get leafs ========
			
			for (Integer l : spotPool){
				Set<DefaultEdge> egs = mspEdgesGraph.edgesOf(l);
				if (egs.size() == 1) {
					leafs.add(l);
				}
			}
			
			// ======= get branches ========
			
			for (int leaf : leafs) {
				
				int min = Integer.MAX_VALUE;
				int anchor = -1;
				
				for (ArrayList<Integer> track : trackSegments) {
					
					for (int t : track) {
						
						DijkstraShortestPath<Integer, DefaultEdge> pathFinderBranch = new DijkstraShortestPath<Integer, DefaultEdge>(mspEdgesGraph, leaf, t);
						List<DefaultEdge> pathBranch = pathFinderBranch.getPathEdgeList();
						
						if (pathBranch.size() < min) {
							min = pathBranch.size();
							anchor = t;
						}	
					}
				}
				
				if (anchor != -1) {
					
					ArrayList<Integer> branch = new ArrayList<Integer>();
					
					DijkstraShortestPath<Integer, DefaultEdge> f = new DijkstraShortestPath<Integer, DefaultEdge>(mspEdgesGraph, anchor, leaf);
					List<DefaultEdge> b = f.getPathEdgeList();
					
					if (b.size() == 1) {
						connections.add(new Connection<Short>((short) anchor, (short) leaf));
						branch.add(anchor);
						branch.add(leaf);
					}
					else {
						branch.add(anchor);
	
						int start = mspEdgesGraph.getEdgeSource(b.get(0));
						int end = mspEdgesGraph.getEdgeTarget(b.get(0));
						
						if (anchor == start) {
							connections.add(new Connection<Short>((short) anchor, (short) end));
							branch.add(end);
						}
						else {
							connections.add(new Connection<Short>((short) anchor, (short) start));
							branch.add(start);
							end = start;
						}
						
						for (int q=1; q < b.size(); q++) {
							
							int s = mspEdgesGraph.getEdgeSource(b.get(q));
							int e = mspEdgesGraph.getEdgeTarget(b.get(q));
							
							int prev = end;
							if (s == end) {
								branch.add(e);
								end = e;
							}
							else {
								branch.add(s);
								end = s;
							}
							connections.add(new Connection<Short>((short) prev, (short) end));
						}
						
					}
					trackSegments.add(branch);
				}	
			}
 		}
		
		return connections;
	}
}
