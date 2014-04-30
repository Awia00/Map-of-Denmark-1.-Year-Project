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
    private final long ID;
    
    private boolean isInShortestPath;

    public Node(long ID, Point2D coords) {
        this.xCoord = coords.getX();
        this.yCoord = coords.getY();
        this.ID = ID;
    }
    
	public Node(int id, Point2D coords) {
        this.xCoord = coords.getX();
        this.yCoord = coords.getY();
        this.ID =  (long)id;
    }
	
    public Node( Point2D coords) {
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

    public long getID() {
        return ID;
    }
    
    public void setInShortestPath(boolean b) {
        isInShortestPath = b;
    }

    public boolean isIsInShortestPath() {
        return isInShortestPath;
    }

    @Override
    public int compareTo(Node o) {
        return  (int)(long)(this.getID() - o.getID());
    }

	@Override
	public String toString()
	{
		return "ID: " + " x: " + xCoord + " y: " + yCoord;
	}
    
	
}
