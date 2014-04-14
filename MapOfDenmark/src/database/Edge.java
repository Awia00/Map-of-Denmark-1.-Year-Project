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
public class Edge implements Comparable<Edge>{

    private final int fromNode;
    private final int toNode;
    private final int roadcode;
    private Node fromNodeTrue = null;
    private Node toNodeTrue = null;
    private Node midNodeTrue = null;
    private final int roadType;
    private final String roadName;

    // Mangler TYPE
    public Edge(int fromNode, int toNode, int roadType, String roadName, int roadcode) {
        this.fromNode = fromNode;
        this.toNode = toNode;
        this.roadType = roadType;
        this.roadName = roadName;
        this.roadcode = roadcode;
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

    public Node getMidNodeTrue() {
        return midNodeTrue;
    }

    public void setFromNodeTrue(Node fromNodeTrue) {
        this.fromNodeTrue = fromNodeTrue;
    }

    public void setToNodeTrue(Node toNodeTrue) {
        this.toNodeTrue = toNodeTrue;
    }
    private Node calcMidNode(){
        Node midNode;
        
        double midX;
        double midY;
        
        midX = (fromNodeTrue.getxCoord() + toNodeTrue.getxCoord()) / 2;
        midY = (fromNodeTrue.getyCoord() + toNodeTrue.getyCoord()) / 2;
        midNode = new Node(new Point2D.Double(midX,midY));
        
        
        return midNode;
                
    }

    public void setMidNodeTrue() {
        this.midNodeTrue = calcMidNode();
    }

    public int getRoadcode() {
        return roadcode;
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
    public String toString(){
        return "\n    E-VEJKODE: " + roadcode + "\n   E-VEJNAVN: " + roadName;
    }
}
