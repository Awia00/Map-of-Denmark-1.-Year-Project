/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package database;

/**
 *
 * @author Anders
 */
public class Edge {
	
	private final Node fromNode;
	private final Node toNode;
	private final int roadType;
	
	// Mangler TYPE
	
	public Edge(Node fromNode, Node toNode, int roadType)
	{
		this.fromNode = fromNode;
		this.toNode = toNode;
		this.roadType = roadType;
	}

	public Node getFromNode()
	{
		return fromNode;
	}

	public Node getToNode()
	{
		return toNode;
	}
}
