/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package database;

/**
 *
 * @author Christian Grøn
 */

import java.awt.geom.Point2D;


public class Intersection {
    //The number of roads in the intersection.
    private final int roadCount;
    //The roads in the intersection.
    private final Road[] roads;
    //The coordinate of the intersection.
    private final Point2D position;


public Intersection (int roadCount, Point2D position)
{
    this.roadCount = roadCount;
    this.roads = new Road[roadCount];
    //roads mangler at blive initiliseret. Vi skal have en måde at initialisere et uvidst antal Roads i arrayet. Nu er det tomt.
    this.position = position;
}
public double getPositionX()
{
    return position.getX();
}

public double getPositionY()
{
    return position.getY();
}

}