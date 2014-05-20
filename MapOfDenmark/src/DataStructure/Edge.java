/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataStructure;

import java.awt.geom.Point2D;

/**
 *
 * @author Anders
 */
public class Edge implements Comparable<Edge> {

    private Node fromNode = null;
    private Node toNode = null;
   // private Node midNodeTrue = null;
    private double midX = 0;
    private double midY = 0;
    private final int roadType;
    private final String roadName;
    private double weightTime = Double.MAX_VALUE;
    private double length = Double.MAX_VALUE;
    // KRAK Constructor
    public Edge(Node fromNode, Node toNode, int roadType, String roadName, int roadcode, double weightTime, double length) {
        this.fromNode = fromNode;
        this.toNode = toNode;
        this.roadType = roadType;
        this.roadName = roadName;
        this.weightTime = weightTime/1000;
        this.length = length/1000;
        
    }
    //OSM Constructor
    public Edge(Node fromNode, Node toNode, int roadType, String roadName, int roadcode, int velocity) {
        this.fromNode = fromNode;
        this.toNode = toNode;
        this.roadType = roadType;
        this.roadName = roadName;
        setMidNode();
		
		if(velocity>0)
		{
			setLength();
			length /= 1000;
			this.weightTime = this.length / (velocity);
		}
    }
	
	public Edge(String roadName)
	{
		roadType = 0;
		this.roadName = roadName;
	}
    
   public void setLength(){
       if(length == Double.MAX_VALUE) 
		   length = Math.sqrt( Math.pow(fromNode.getxCoord()-toNode.getxCoord(),2) + Math.pow(fromNode.getyCoord()-toNode.getyCoord(),2) );
   }
    
   public double getLength(){
       return length;
   }
    
    public String getRoadName() {
        return roadName;
    }

    public int getRoadType() {
        return roadType;
    }

    public Node getFromNode() {
        return fromNode;
    }

    public Node getToNode() {
        return toNode;
    }

    public void setFromNode(Node fromNode) {
        this.fromNode = fromNode;
    }

    public void setToNode(Node toNode) {
        this.toNode = toNode;
    }

    public void setMidNode() {

        midX = (fromNode.getxCoord() + toNode.getxCoord()) / 2;
        midY = (fromNode.getyCoord() + toNode.getyCoord()) / 2;
    }

    public double getMidX() {
        return midX;
    }

    public double getMidY() {
        return midY;
    }
    
    public double getWeight(){
        return weightTime;
    }
    /*
     @Override
     public int compareTo(Edge o) {
     return this.getRoadcode() - o.getRoadcode();
     }
     * */

    @Override
    public int compareTo(Edge o) {
        return this.getRoadName().compareTo(o.getRoadName());
    }

    @Override
    public String toString() {
        return "\n   E-VEJNAVN: " + roadName;
    }
}
