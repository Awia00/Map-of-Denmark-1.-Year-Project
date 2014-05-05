/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package database.pathfinding;

import database.Edge;
import database.Node;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Class description:
 *
 * @version 0.1 - changed 05-05-2014
 * @authorNewVersion  Anders Wind - awis@itu.dk
 *
 * @buildDate 05-05-2014
 * @author Anders Wind - awis@itu.dk
 */
public class Dijakstra {

	private HashMap<Node, HashSet<Edge>> graph; // the adjecent edges to a node
	private HashMap<Node, Double> distTo; // the distance to the Node
	private HashMap<Node, Edge> edgeTo; // the edge to Node
	
	private Node fromNode;
	private PriorityQueue pQueue;
	private List<Node> route;

	public Dijakstra(HashMap<Node, HashSet<Edge>> graph, Node fromNode, Comparator<Edge> comparator)
	{
		this.graph = graph;
		this.fromNode = fromNode;
		distTo = new HashMap<>();
		for(Node node : graph.keySet())
		{
			distTo.put(node, Double.POSITIVE_INFINITY);
		}
		pQueue = new PriorityQueue(100, comparator);
		createRoutes();
	}
	
	private void createRoutes()
	{
		distTo.put(fromNode, 0.0);

        // relax vertices in order of distance from s
		pQueue.add(fromNode);
        while (!pQueue.isEmpty()) {
            Node v = (Node) pQueue.poll();
            for (Edge e : graph.get(v))
			{
                relaxDriveTime(e, v);
			}
        }
	}
	
	private Node getPrevious(Node node)
	{
		return null;
	}
	
	private void relaxDriveTime(Edge e, Node node)
	{
		Node w;
		if(node.equals(e.getFromNode())){ w = e.getToNode();}
		else
		{w = e.getFromNode();} 
        if (distTo.get(w) > distTo.get(node) + e.getWeight()) {
            distTo.put(w,distTo.get(node) + e.getWeight());
			edgeTo.put(w, e);
			
			if (pQueue.contains(w)){ pQueue.remove(w);pQueue.add(w);}
            else pQueue.add(w);
		}
	}
	
	private void relaxLength(Edge e, Node node)
	{
		Node w;
		if(node.equals(e.getFromNode())){ w = e.getToNode();}
		else
		{w = e.getFromNode();} 
        if (distTo.get(w) > distTo.get(node) + e.getLength()) {
            distTo.put(w,distTo.get(node) + e.getLength());
			edgeTo.put(w, e);
			if (pQueue.contains(w)){ pQueue.remove(w);pQueue.add(w);}
            else pQueue.add(w);
		}
	}
	
	public boolean hasPathTo(Node to)
	{
		return true;
	}
	
	public List<Node> getRoute(Node toNode)
	{
		return null;
	}
	
	private void getPathRecoursive(Node from, Node to)
	{
		
	}
	
}
