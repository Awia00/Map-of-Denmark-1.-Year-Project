/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mapofdenmark.GUIPackage;

import database.Database;
import database.DatabaseInterface;
import database.Edge;
import database.Node;
import database.Street;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;

/**
 * Class description:
 *
 * @version 0.1 - changed 27-02-2014
 * @authorNewVersion Anders Wind - awis@itu.dk
 *
 * @buildDate 27-02-2014
 * @author Anders Wind - awis@itu.dk
 */
public class MapComponent extends JComponent {

	private MapComponent drawMapComponent;
	private QuadTree quadTreeToDraw;
	private VisibleArea visibleArea;

	public MapComponent(VisibleArea visibleArea, Street[] streets)
	{
		this.visibleArea = new VisibleArea();
		initialize(streets);
	}

	private void initialize(Street[] streets)
	{

		DatabaseInterface db = Database.db();

		List<Edge> edges = db.getData();
		//List<Edge> edges = db.getEdges();

		// DATABASEN SKAL FJERNES HERFRA STREET[] streets BRUGES I STEDET.
		quadTreeToDraw = new QuadTree(edges, 0, 0, 590000);
		visibleArea.setCoord(0, 0, 470000, 370000, quadTreeToDraw); // HELE DANMARK
		//visibleArea.setCoord(120000, 80000, 50000, 25000, quadTreeToDraw); // ODENSE
	}

	@Override
	public void paint(Graphics g)
	{
		g.setColor(Color.white);
		g.fillRect(0, 0, getSize().width - 1, getSize().height - 1);
		g.setColor(Color.red);
		g.drawRect(0, 0, getSize().width - 1, getSize().height - 1);
		g.setColor(Color.blue);
		ArrayList<QuadTree> bottomTrees = QuadTree.getBottomTrees();
		for (QuadTree quadTree : bottomTrees)
		{
			if (quadTree.isDrawable())
			{
				for (Edge edge : quadTree.getEdges())
				{
					double xlength = visibleArea.getxLength();
					double ylength = visibleArea.getyLength();
					double xVArea = visibleArea.getxCoord();
					double yVArea = visibleArea.getyCoord();
					double x1 = edge.getFromNodeTrue().getxCoord();
					double y1 = edge.getFromNodeTrue().getyCoord();
					double x2 = edge.getToNodeTrue().getxCoord();
					double y2 = edge.getToNodeTrue().getyCoord();
					g.drawLine((int) (((x1-xVArea) / xlength) * getWidth()), (int) (getSize().height - ((y1-yVArea) / ylength) * getHeight()), (int) (((x2-xVArea) / xlength) * getWidth()), (int) (getSize().height - ((y2-yVArea) / ylength) * getHeight()));
					//g.drawLine((int) (((y1 - 440000) / ylength) * getHeight()), (int) (getSize().width-((x1 - 6000000) / xlength) * getWidth()), (int) (((y2 - 440000) / ylength) * getHeight()), (int) (getSize().width-((x2 - 6000000) / xlength) * getWidth()));					
					//g.fillRect((int)(point.getY()/*), getSize().height-(int)(point.getX()/quadTreeToDraw.getQuadTreeLength()*), 1, 1);
				}
			}
		}

		// when drawing: take the coord, substract its value with the startCoord from visible area
		// then divide by the length. that way you get values from 0-1.
	}
}
