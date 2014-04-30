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
public class Edge implements Comparable<Edge> {

    private final int fromNode;
    private final int toNode;
    private final int roadcode;
    private Node fromNodeTrue = null;
    private Node toNodeTrue = null;
   // private Node midNodeTrue = null;
    private double midX = 0;
    private double midY = 0;
    private final int roadType;
    private final String roadName;
    private double weight = 0;
    
    private boolean isInShortestPath;
    // Mangler TYPE
    public Edge(int fromNode, int toNode, int roadType, String roadName, int roadcode, double weight) {
        this.fromNode = fromNode;
        this.toNode = toNode;
        this.roadType = roadType;
        this.roadName = roadName;
        this.roadcode = roadcode;
        this.weight = weight;
    }

    public Edge(Node fromNode, Node toNode, int roadType, String roadName, int roadcode, double weight) {
        this.fromNodeTrue = fromNode;
        this.toNodeTrue = toNode;
        this.roadType = roadType;
        this.roadName = roadName;
        this.roadcode = roadcode;
        this.fromNode = 0;
        this.toNode = 0;
        this.weight = weight;
        setMidNodeTrue();

    }

    
    public void setInShortestPath(boolean b) {
        isInShortestPath = b;
    }

    public boolean isIsInShortestPath() {
        return isInShortestPath;
    }
    
    protected int getFromNode() {
        return fromNode;
    }

    protected int getToNode() {
        return toNode;
    }

    public String getRoadName() {
        return roadName;
    }

    public int getRoadType() {
        return roadType;
    }

    public Node getFromNodeTrue() {
        return fromNodeTrue;
    }

    public Node getToNodeTrue() {
        return toNodeTrue;
    }

    public void setFromNodeTrue(Node fromNodeTrue) {
        this.fromNodeTrue = fromNodeTrue;
    }

    public void setToNodeTrue(Node toNodeTrue) {
        this.toNodeTrue = toNodeTrue;
    }

    public void setMidNodeTrue() {

        midX = (fromNodeTrue.getxCoord() + toNodeTrue.getxCoord()) / 2;
        midY = (fromNodeTrue.getyCoord() + toNodeTrue.getyCoord()) / 2;
    }

    public int getRoadcode() {
        return roadcode;
    }

    public double getMidX() {
        return midX;
    }

    public double getMidY() {
        return midY;
    }
    
    public double getWeight(){
        return weight;
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
        return "\n    E-VEJKODE: " + roadcode + "\n   E-VEJNAVN: " + roadName;
    }
}
