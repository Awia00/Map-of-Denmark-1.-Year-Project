/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import DataStructure.QuadTree;
import java.util.ArrayList;

/**
 * Class description:
 *
 * @version 0.1 - changed 10-03-2014
 * @authorNewVersion Anders Wind - awis@itu.dk
 *
 * @buildDate 10-03-2014
 * @author Anders Wind - awis@itu.dk
 */
public class VisibleArea {

	private double xCoord, yCoord, xLength, yLength;

	public VisibleArea()
	{
		initialize();
	}

	private void initialize()
	{
		xCoord = 0;
		yCoord = 0;
		xLength = 0;
		yLength = 0;
	}

	/**
	 * Set the coordinates and length of the visibleArea and go through the
	 * trees and set which trees should be visible.
	 *
	 * @param xCoord
	 * @param yCoord
	 * @param xLength
	 * @param yLength
	 */
	public void setCoord(double xCoord, double yCoord, double xLength, double yLength)
	{
		this.xCoord = xCoord;
		this.yCoord = yCoord;
		this.xLength = xLength;
		this.yLength = yLength;
		//update
		setQuadTreesVisible();
	}

	/**
	 * Set the coordinates and length of the visibleArea
	 *
	 * @param xCoord
	 * @param yCoord
	 * @param xLength
	 * @param yLength
	 */
	public void setCoordWithoutUpdate(double xCoord, double yCoord, double xLength, double yLength)
	{
		this.xCoord = xCoord;
		this.yCoord = yCoord;
		this.xLength = xLength;
		this.yLength = yLength;
	}

	/**
	 * Go through the quadtrees and check if they have some coordinates inside
	 * the visible area and if they have then set their drawThis boolean to
	 * true.
	 */
	private void setQuadTreesVisible()
	{
		// Iterate over the quadtree untill you get to the lowest level
		ArrayList<QuadTree> bottomTrees = QuadTree.getBottomTrees();
		for (QuadTree quadTree : bottomTrees)
		{
			if ((quadTree.getQuadTreeX() + quadTree.getQuadTreeLength() >= xCoord && quadTree.getQuadTreeY() + quadTree.getQuadTreeLength() >= yCoord))
			{
				if ((quadTree.getQuadTreeX() <= xCoord + xLength && quadTree.getQuadTreeY() <= yCoord + yLength))
				{
					quadTree.setDrawable(true);
				} else
				{
					quadTree.setDrawable(false);
				}
			} else
			{
				quadTree.setDrawable(false);
			}
		}

		//IMPLEMENTATION
		// take our quadTree
		// go through the quadtree until you find the bottom ones.
		// check if their coordinates start or end coordinates are inside of these coordinates
		// set their drawThis field to either true or false.
	}

	public double getxCoord()
	{
		return xCoord;
	}

	public double getyCoord()
	{
		return yCoord;
	}

	public double getxLength()
	{
		return xLength;
	}

	public double getyLength()
	{
		return yLength;
	}
}
