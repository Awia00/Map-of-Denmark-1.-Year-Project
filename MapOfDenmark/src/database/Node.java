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
public class Node {
	
	private final double xCoord,yCoord;
	private final int ID;
	
	public Node(double xCoord, double yCoord, int ID)
	{
		this.xCoord = xCoord;
		this.yCoord = yCoord;
		this.ID = ID;
	}

	public double getxCoord()
	{
		return xCoord;
	}

	public double getyCoord()
	{
		return yCoord;
	}

	public int getID()
	{
		return ID;
	}
}
