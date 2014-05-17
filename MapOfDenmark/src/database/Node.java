/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import java.awt.geom.Point2D;

/**
 *
 * @author Anders
 */
public class Node implements Comparable<Node>{

    private final double xCoord, yCoord;
    private final int ID;   
	private double distTo;
	private double heuristic;
    

    public Node(int ID, Point2D coords) {
        this.xCoord = coords.getX();
        this.yCoord = coords.getY();
        this.ID = ID;
    }
	
    public Node(Point2D coords) {
        this.xCoord = coords.getX();
        this.yCoord = coords.getY();
        this.ID = -1;
    }

    public double getxCoord() {
        return xCoord;
    }

    public double getyCoord() {
        return yCoord;
    }

    public int getID() {
        return ID;
    }

	public void setDistTo(double distTo)
	{
		this.distTo = distTo;
	}

	public double getDistTo()
	{
		return distTo;
	}

	public void setHeuristic(double heuristic)
	{
		this.heuristic = heuristic;
	}

	public double getHeuristic()
	{
		return heuristic;
	}
	
    @Override
    public int compareTo(Node o) {
        return this.getID() - o.getID();
    }

	@Override
	public String toString()
	{
		return "ID: " + " x: " + xCoord + " y: " + yCoord;
	}
    
	
}
