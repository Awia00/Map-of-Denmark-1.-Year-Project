/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mapofdenmark.GUIPackage;

import database.Database;
import database.DatabaseInterface;
import database.Edge;
import database.Street;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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
public class MapComponent extends JComponent{

	private QuadTree quadTreeToDraw;
	private VisibleArea visibleArea;
	
	private final double zoomInConstant = 0.9;
	private final double zoomOutConstant = 2;

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
		//visibleArea.setCoord(0, 0, 470000, 370000); // HELE DANMARK
		visibleArea.setCoord(120000, 80000, 50000, 25000); // ODENSE
	}

	public void moveVisibleArea(double xCoord, double yCoord)
	{
		double xMapCoord = xCoord/getWidth()*visibleArea.getxLength()*1.2;
		double yMapCoord = yCoord/getHeight()*visibleArea.getyLength()*1.2;
		visibleArea.setCoord(visibleArea.getxCoord()+xMapCoord, visibleArea.getyCoord()+yMapCoord, visibleArea.getxLength(), visibleArea.getyLength());
	}
	
	public void zoomOut(double mouseXCoord, double mouseYCoord)
	{
		double centerXCoord = visibleArea.getxCoord() + mouseXCoord/getWidth()*visibleArea.getxLength();
		double centerYCoord = visibleArea.getyCoord() + mouseYCoord/getHeight()*visibleArea.getyLength();
		double xlengthZoom = visibleArea.getxLength() * zoomOutConstant;
		double ylengthZoom = visibleArea.getyLength() * zoomOutConstant;
		double v2x = centerXCoord - xlengthZoom/2;
		double v2y = centerYCoord - ylengthZoom/2;
		visibleArea.setCoord(v2x, v2y, xlengthZoom, ylengthZoom);
		
	}
	
	public void zoomIn(double mouseXCoord, double mouseYCoord)
	{
            	double centerXCoord = visibleArea.getxCoord() + mouseXCoord/getWidth()*visibleArea.getxLength();
		double centerYCoord = visibleArea.getyCoord() + mouseYCoord/getHeight()*visibleArea.getyLength();

		double xlengthZoom = visibleArea.getxLength() * zoomInConstant;
		double ylengthZoom = visibleArea.getyLength() * zoomInConstant;
		double v2x = centerXCoord - xlengthZoom/2;
		double v2y = centerYCoord - ylengthZoom/2;
		visibleArea.setCoord(v2x, v2y, xlengthZoom, ylengthZoom);
	}
	
	@Override
	public void paint(Graphics g)
	{
		g.setColor(Color.white);
		g.fillRect(0, 0, getSize().width - 1, getSize().height - 1);
		g.setColor(Color.black);
		g.drawRect(0, 0, getSize().width - 1, getSize().height - 1);
		ArrayList<QuadTree> bottomTrees = QuadTree.getBottomTrees();
		for (QuadTree quadTree : bottomTrees)
		{
			if (quadTree.isDrawable())
			{
				for (Edge edge : quadTree.getEdges())
				{
					if(edge.getRoadType() == 1 || edge.getRoadType() == 2 || edge.getRoadType() == 3 || edge.getRoadType() == 21|| edge.getRoadType() == 22|| edge.getRoadType() == 23 || edge.getRoadType() == 31 || edge.getRoadType() == 32 || edge.getRoadType() == 41 || edge.getRoadType() == 42){g.setColor(Color.black);}
					else if(edge.getRoadType() == 4 || edge.getRoadType() == 5 || edge.getRoadType() == 24 || edge.getRoadType() == 25){g.setColor(Color.red);}
					else if(edge.getRoadType() == 8 || edge.getRoadType() == 10 || edge.getRoadType() == 11 || edge.getRoadType() == 28){g.setColor(Color.green);}
					else{g.setColor(Color.gray);}
					
					double xlength = visibleArea.getxLength();
					double ylength = visibleArea.getyLength();
					double xVArea = visibleArea.getxCoord();
					double yVArea = visibleArea.getyCoord();
					double x1 = edge.getFromNodeTrue().getxCoord();
					double y1 = edge.getFromNodeTrue().getyCoord();
					double x2 = edge.getToNodeTrue().getxCoord();
					double y2 = edge.getToNodeTrue().getyCoord();
					g.drawLine((int) (((x1-xVArea) / xlength) * getWidth()), (int) (getSize().height - ((y1-yVArea) / ylength) * getHeight()), (int) (((x2-xVArea) / xlength) * getWidth()), (int) (getSize().height - ((y2-yVArea) / ylength) * getHeight()));
					g.drawLine((int) (((x1-xVArea) / xlength) * getWidth()), (int) (getSize().height - ((y1-yVArea) / ylength) * getHeight()), (int) (1+((x2-xVArea) / xlength) * getWidth()), (int) (1+getSize().height - ((y2-yVArea) / ylength) * getHeight()));
					g.drawLine((int) (1+((x1-xVArea) / xlength) * getWidth()), (int) (1+getSize().height - ((y1-yVArea) / ylength) * getHeight()), (int) (((x2-xVArea) / xlength) * getWidth()), (int) (getSize().height - ((y2-yVArea) / ylength) * getHeight()));
				}
			}
		}

		// when drawing: take the coord, substract its value with the startCoord from visible area
		// then divide by the length. that way you get values from 0-1.
	}

}
