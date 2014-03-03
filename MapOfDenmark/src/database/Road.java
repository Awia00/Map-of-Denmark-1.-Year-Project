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
    //The name of the road.
    private final String roadName;
    // The type of the road.
    private enum roadType{HIGHWAY, MAINROAD, PATH, OTHER}; 
    // The coordinate for the 1st end of the road.
    private final Point2D position1;
    // The coordinate of the 2nd end of the road.
    private final Point2D position2;
    // The ideal speed limit/ideal speed of the road.
    private final double speed;
    // The estimated driving time of the road.
    private final double driveTime;
    //The direction of the road. 
    //"TF" is one way in the direction To-From.
    //"FT" is one way in the direction From-To.
    //"N" is no driving allowed.        
    private enum roadDirection{TF,FT,N};
    
public Road(String roadName,enum roadType, Point2D position1, Point2D position2, double speed, double driveTime, enum roadDirection)
{
    this.roadName = roadName;
    this.roadtype = roadType;
    this.position1 = position1;
    this.position2 = position2;
    this.speed = speed;
    this.driveTime = driveTime;
    this.roadDirection = roadDirection;
}
public String getRoadName()
{
    return roadName;
}

public enum getRoadType()
{
    return roadType;
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

public double getSpeed()
{
    return speed;
}

public double getDriveTime()
{
    return driveTime;
}

public enum getRoadDirection()
{
    return roadDirection;
}

}