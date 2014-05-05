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

	private HashMap<Node, HashSet<Edge>> graph;
	private HashMap<Node, Double> distTo;
	
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
        pq.insert(s, distTo[s]);
		pQueue.add(graph.get(fromNode));
        while (!pq.isEmpty()) {
            int v = pq.delMin();
            for (DirectedEdge e : G.adj(v))
			{
                relax(e);
			}
        }

        // check optimality conditions
        assert check(G, s);
	}
	
	private Node getPrevious(Node node)
	{
		return null;
	}
	
	private void relax()
	{
		
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
