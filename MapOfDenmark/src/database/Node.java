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

    public Node(int ID, Point2D coords) {
        this.xCoord = coords.getX();
        this.yCoord = coords.getY();
        this.ID = ID;
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

    @Override
    public int compareTo(Node o) {
        
        return this.getID() - o.getID();
    }
    
}
