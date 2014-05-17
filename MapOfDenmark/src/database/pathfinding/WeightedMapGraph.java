/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database.pathfinding;

import database.Edge;
import database.Node;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
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
	private LinkedHashSet<String> directionKeys;

	public WeightedMapGraph(List<Edge> edges)
	{
		graph = new HashMap<>();
		directionKeys = new LinkedHashSet<>();
		for (final Edge edge : edges)
		{
			// checks for bad values
			if (edge.getFromNode().getID() < 0 || edge.getToNode().getID() < 0 || edge.getLength() < 0 || edge.getWeight() < 0
					|| edge.getLength() == Integer.MAX_VALUE || edge.getWeight() == Integer.MAX_VALUE || edge.getRoadName().equals(""))
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

	public void runDij(Node from, Node to, boolean heuristic)
	{
		dij = new Dijakstra(graph, from, to, heuristic);
	}
        
	public boolean hasRoute(Node to)
	{
		if (dij != null)
		{
			return dij.hasPathTo(to);
		}
		return false;
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

	public List<Edge> getDirections(Node to)
	{

		directionKeys.clear();
		Node[] route = calculateRoute(to).toArray(new Node[calculateRoute(to).size()]);
		//HashMap<String, Double> directions = new HashMap<>();
		List<Edge> listToReturn = new ArrayList<>();

		for (int i = 0; i < route.length; i++)
		{
			int next = 0;
			if (!(i + 1 == route.length))
			{
				next = i + 1;
			}
			HashSet<Edge> edges = graph.get(route[i]);
			//System.out.println(edges.size());
			for (Edge edge : edges)
			{
				if ((edge.getFromNode().equals(route[next]) || edge.getToNode().equals(route[next])))
				{
					listToReturn.add(edge);
					directionKeys.add(edge.getRoadName());
				}
			}
		}
		//System.out.println("Directions size " + directions.size());
		return listToReturn;
	}
	
	/*
	//System.out.println("Found edge");
					//System.out.println(edge.getRoadName());
					double dist;
					if (directions.get(edge.getRoadName()) == null)
					{
						dist = 0;
					} else
					{
						dist = directions.get(edge.getRoadName());
					}
//                        dist = directions.get(edge.getRoadName());
					directions.put(edge.getRoadName(), dist + edge.getLength());
					
	*/

	public LinkedHashSet<String> getDirectionKeys()
	{
		return directionKeys;
	}
}
