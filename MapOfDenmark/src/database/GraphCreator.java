/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Stack;

/**
 *
 * @author Anders
 */
public class GraphCreator {

	private final HashMap<Node, LinkedList<Edge>> graph;

	public GraphCreator(Edge[] edges, Node[] nodes)
	{
		graph = new HashMap<>();
		createGraphStructure(edges, nodes);
	}

	private void createGraphStructure(Edge[] edges, Node[] nodes)
	{
		for (Edge edge : edges)
		{
			graph.put(edge.getFromNodeTrue(), new LinkedList<Edge>());
			graph.get(edge.getFromNodeTrue()).add(edge);
		}
	}

	/**
	 * Find the shortest route from a point x to another point y, using Dijkstra
	 * algorithm Dijkstra uses the shortest path to find the next edge to
	 * include.
	 */
	private void runDijkstra()
	{

	}

	/**
	 * Find the shortest route from node1 to node2. BFS works like a queue,
	 * taking the oldest edge and checking all its branches first.
	 *
	 * @param node1
	 * @param node2
	 */
	private void runBFS(Node node1, Node node2)
	{

	}

	/**
	 * Find the shortest route from node1 to node2. DFS works like a Stack,
	 * taking the newest edge and checking all its branches first.
	 *
	 * @param fromNode
	 * @param toNode
	 */
	private void runDFS(Node fromNode, Node toNode)
	{
	}
	
	/**
	 * Find the shortest route from a point x to another point y, using Prims
	 * algorithm Prim uses the edge with the lowest weigth to include the next
	 * edge.
	 */
	private void runPrims()
	{

	}

	/**
	 * Find the shortest route from one point to another.
	 *
	 * @param node1
	 * @param node2
	 */
	public void getShortestRoute(Node fromNode, Node toNode)
	{

	}

	public void printGraph()
	{
		for (Node node : graph.keySet())
		{
			System.out.println(node.toString());
			for (Edge edge : graph.get(node))
			{
				System.out.println(edge.toString());
			}
		}
	}
}