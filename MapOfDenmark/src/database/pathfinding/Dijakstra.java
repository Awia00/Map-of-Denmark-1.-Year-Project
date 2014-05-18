/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database.pathfinding;

import database.Edge;
import database.Node;
import database.NodeDistToComparator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Class description:
 *
 * @version 0.1 - changed 05-05-2014
 * @authorNewVersion Anders Wind - awis@itu.dk
 *
 * @buildDate 05-05-2014
 * @author Anders Wind - awis@itu.dk
 */
public class Dijakstra {

	private HashMap<Node, HashSet<Edge>> graph; // the adjecent edges to a node
	private HashMap<Node, Edge> edgeTo; // the edge to Node

	private Node fromNode, toNode;
	private PriorityQueue pQueue;
	private List<Node> route;

	public Dijakstra(HashMap<Node, HashSet<Edge>> graph, Node fromNode, Node toNode, boolean heuristic)
	{
		this.graph = graph;
		this.fromNode = fromNode;
		this.toNode = toNode;
		edgeTo = new HashMap<>();
		for (Node node : graph.keySet())
		{
			node.setDistTo(Double.POSITIVE_INFINITY);
			node.setHeuristic(Double.POSITIVE_INFINITY);
		}
		pQueue = new PriorityQueue(graph.size() / 20, new NodeDistToComparator());
		createRoutes(heuristic);
	}

	private void createRoutes(boolean heuristic)
	{
		fromNode.setDistTo(0);
		if(heuristic)
		{
			fromNode.setHeuristic(Math.sqrt(Math.pow((toNode.getxCoord() - fromNode.getxCoord()), 2) + Math.pow(toNode.getyCoord() - fromNode.getyCoord(), 2))/1000);
		}
		else
		{
			fromNode.setHeuristic(Math.sqrt(Math.pow((toNode.getxCoord() - fromNode.getxCoord()), 2) + Math.pow(toNode.getyCoord() - fromNode.getyCoord(), 2))/(130000));
		}
		edgeTo.put(fromNode, null);

		// relax vertices in order of distance from s
		pQueue.add(fromNode);
		while (!pQueue.isEmpty())
		{
			Node v = (Node) pQueue.poll();
			if (v.equals(toNode))
			{
				break;
			}

			for (Edge e : graph.get(v))
			{
				if (heuristic)
				{
					relaxDriveTime(e, v);
//                    System.out.println("drivetime");
				} else
				{
					relaxLength(e, v);
//                    System.out.println("length");
				}
			}
		}
	}

	public Node getFromNode()
	{
		return fromNode;
	}

	private Node getPrevious(Node node)
	{
		Edge e = edgeTo.get(node);
		if (node.equals(e.getFromNode()))
		{
			return e.getToNode();
		} else
		{
			return e.getFromNode();
		}
	}

	private void relaxDriveTime(Edge e, Node prevNode)
	{
		Node nextNode;
		if (prevNode.equals(e.getFromNode()))
		{
			nextNode = e.getToNode();
		} else
		{
			nextNode = e.getFromNode();
		}

		double g_Score = prevNode.getDistTo() + e.getWeight();
		double h_Score = Math.sqrt(Math.pow((toNode.getxCoord() - nextNode.getxCoord()), 2) + Math.pow(toNode.getyCoord() - nextNode.getyCoord(), 2)) / (130000); // 130*1000 
		double f_Score = h_Score + g_Score;

		if (g_Score >= nextNode.getDistTo() && edgeTo.containsKey(nextNode))
		{
			return;
		}
		if (g_Score < nextNode.getDistTo())
		{
			nextNode.setDistTo(g_Score);
			nextNode.setHeuristic(f_Score);
			edgeTo.put(nextNode, e);

			if (pQueue.contains(nextNode))
			{
				pQueue.remove(nextNode);
				pQueue.add(nextNode);
			} else
			{
				pQueue.add(nextNode);
			}
		}

		/*
		 if (nextNode.getDistTo() >= prevNode.getDistTo() + e.getWeight()) {
		 nextNode.setDistTo(prevNode.getDistTo()+e.getWeight() + Math.sqrt(Math.pow((toNode.getxCoord()-nextNode.getxCoord()), 2)+Math.pow(toNode.getyCoord()-nextNode.getyCoord(), 2))/(130*1000));
		 edgeTo.put(nextNode, e);
			
		 if (pQueue.contains(nextNode)){ pQueue.remove(nextNode);pQueue.add(nextNode);}
		 else pQueue.add(nextNode);
		 }
		 */
	}

	private void relaxLength(Edge e, Node prevNode)
	{
		Node nextNode;
		if (prevNode.equals(e.getFromNode()))
		{
			nextNode = e.getToNode();
		} else
		{
			nextNode = e.getFromNode();
		}

		double g_Score = prevNode.getDistTo() + e.getLength();
		double h_Score = Math.sqrt(Math.pow((toNode.getxCoord() - nextNode.getxCoord()), 2) + Math.pow(toNode.getyCoord() - nextNode.getyCoord(), 2))/1000;
		double f_Score = h_Score + g_Score;

		if (g_Score >= nextNode.getDistTo()&& edgeTo.containsKey(nextNode))
		{
			return;
		}
		if (g_Score < nextNode.getDistTo())
		{
			nextNode.setDistTo(g_Score);
			nextNode.setHeuristic(f_Score);
			edgeTo.put(nextNode, e);

			if (pQueue.contains(nextNode))
			{
				pQueue.remove(nextNode);
				pQueue.add(nextNode);
			} else
			{
				pQueue.add(nextNode);
			}
		}

		/*
		 if(edgeTo.containsKey(nextNode)) return;
		
		 if (nextNode.getDistTo() > prevNode.getDistTo() + e.getLength()) {
		 nextNode.setDistTo(prevNode.getDistTo()+e.getLength() + Math.sqrt(Math.pow((toNode.getxCoord()-nextNode.getxCoord()), 2)+Math.pow(toNode.getyCoord()-nextNode.getyCoord(), 2)));
		 edgeTo.put(nextNode, e);
			
		 if (pQueue.contains(nextNode)){ pQueue.remove(nextNode);pQueue.add(nextNode);}
		 else pQueue.add(nextNode);
		 }
		 */
	}

	public boolean hasPathTo(Node to)
	{
		if (edgeTo.containsKey(to))
		{
			return true;
		} else
		{
			return false;
		}
	}

	public List<Node> getRoute(Node toNode)
	{
		if (route == null || route.isEmpty())
		{
			route = new ArrayList<>();
			//for(Node node : edgeTo.keySet())
			{
				//route.add(node);
			}
			getPathRecoursive(toNode);
			System.out.println("edgeTo: " + edgeTo.size());
			System.out.println("route: " + route.size());
			return route;
		}
		else
			return route;
	}

	private void getPathRecoursive(Node toNode)
	{
		if (toNode.equals(fromNode))
		{
			route.add(toNode);
		} else if (!hasPathTo(toNode))
		{
			return;
		} else
		{
			route.add(toNode);
			getPathRecoursive(getPrevious(toNode));
		}
	}

}
