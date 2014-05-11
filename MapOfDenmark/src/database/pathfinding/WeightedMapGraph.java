/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database.pathfinding;

import database.Edge;
import database.Node;
import java.awt.geom.Path2D;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * Class description:
 *
 * @version 0.1 - changed 05-05-2014
 * @authorNewVersion Anders Wind - awis@itu.dk
 *
 * @buildDate 05-05-2014
 * @author Anders Wind - awis@itu.dk
 */
public class WeightedMapGraph {

	private final HashMap<Node, HashSet<Edge>> graph;
	private Dijakstra dij;

	public WeightedMapGraph(List<Edge> edges)
	{
		graph = new HashMap<>();
		for (final Edge edge : edges)
		{
			// checks for bad values
			if (edge.getFromNode().getID() < 0 || edge.getToNode().getID() < 0 || edge.getLength() < 0 || edge.getWeight() < 0
					|| edge.getLength() == Integer.MAX_VALUE || edge.getWeight() == Integer.MAX_VALUE)
			{
				continue;
			}
			if (graph.containsKey(edge.getFromNode()))
			{
				graph.get(edge.getFromNode()).add(edge);
			} else
			{
				graph.put(edge.getFromNode(), new HashSet<Edge>() {
					
					{
						add(edge);
					}
				});
			}
			if (graph.containsKey(edge.getToNode()))
			{
				graph.get(edge.getToNode()).add(edge);
			} else
			{
				graph.put(edge.getToNode(), new HashSet<Edge>() {
					
					{
						add(edge);
					}
				});
			}
		}
	}

	public HashMap<Node, HashSet<Edge>> getGraph()
	{
		return graph;
	}

	public void runDij(Node from, Node to)
	{
			dij = new Dijakstra(graph, from, to);
	}

	public List<Node> calculateRoute(Node to)
	{
		return dij.getRoute(to);
	}

	public Path2D drawablePath2D(Node to)
	{
		List<Node> nodes = dij.getRoute(to);

		Path2D path = new Path2D.Double();
		for (Node n : nodes)
		{
			if (n.equals(nodes.get(0)))
			{
				path.moveTo(n.getxCoord(), n.getyCoord());
			} else
			{
				path.lineTo(n.getxCoord(), n.getyCoord());
			}
			System.out.println(n.getxCoord() + " " + n.getyCoord());
		}
		System.out.println(path.getBounds());
		return path;
	}
}
