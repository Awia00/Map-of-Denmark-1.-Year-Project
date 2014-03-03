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

public class Road {
    
    private final String roadName;
    private final Point2D position1;
    private final Point2D position2;
public Road(String roadName, Point2D position1, Point2D position2)
{
    this.roadName = roadName;
    this.position1 = position1;
    this.position2 = position2;
}
public String getRoadName()
{
    return roadName;
}

public double getPosition1X()
{
    return position1.getX();
}

public double getPosition1Y()
{
    return position1.getY();
}

public double getPosition2X()
{
    return position2.getX();
}

public double getPosition2Y()
{
    return position2.getY();
}

}