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
	
	private final int fromNode;
	private final int toNode;
	private final int roadType;
	
	// Mangler TYPE
	
	public Edge(int fromNode, int toNode, int roadType)
	{
		this.fromNode = fromNode;
		this.toNode = toNode;
		this.roadType = roadType;
	}

	public int getFromNode()
	{
		return fromNode;
	}

	public int getToNode()
	{
		return toNode;
	}
}
