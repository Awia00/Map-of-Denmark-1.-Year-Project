/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mapofdenmark.GUIPackage;

/**
 * Class description:
 *
 * @version 0.1 - changed 10-03-2014
 * @authorNewVersion  Anders Wind - awis@itu.dk
 *
 * @buildDate 10-03-2014
 * @author Anders Wind - awis@itu.dk
 */
public class VisibleArea {

	private double xCoord, yCoord, xLength, yLength;
	private final QuadTree quadTree;
	
	public VisibleArea(QuadTree quadTree)
	{
		this.quadTree = quadTree;
		initialize();
	}
	
	private void initialize()
	{	
		xCoord = 0;
		yCoord = 0;
		xLength = 0;
		yLength = 0;
	}
	
	public void setCoord(double xCoord, double yCoord, double xLength, double yLength)
	{
		this.xCoord = xCoord;
		this.yCoord = yCoord;
		this.xLength = xLength;
		this.yLength = yLength;
		//update
		setQuadTreesVisible(quadTree);
	}
	
	public void setVisibleEdgeTypes(int acceptedType)
	{
		// do something.
	}
	
	
	private void setQuadTreesVisible(QuadTree quadTree)
	{
		// iterate over the quadtree untill you get to the lowest level
		if (quadTree.getPoints().isEmpty() == true) // change this to getEdges instead.
		{
			// recursive call
		}
		else
		{
			if((quadTree.getQuadTreeX()+quadTree.length >= xCoord &&  quadTree.getQuadTreeY()+quadTree.length>=yCoord))
			{
				if((quadTree.getQuadTreeX()<= xCoord+xLength && quadTree.getQuadTreeY()<=yCoord+yLength))
				{
				}
			}
		}
		
		//IMPLEMENTATION
		// take our quadTree
		// go through the quadtree until you find the bottom ones.
		// check if their coordinates start or end coordinates are inside of these coordinates
		// set their drawThis field to either true or false.
	}
}
