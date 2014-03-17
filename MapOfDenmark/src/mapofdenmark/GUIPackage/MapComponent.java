/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mapofdenmark.GUIPackage;

import database.Database;
import database.DatabaseInterface;
import database.Edge;
import database.RoadTypeEnum;
import database.Street;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Arrays;
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

	private QuadTree quadTreeToDraw;
	private VisibleArea visibleArea;
	private int xStartCoord, yStartCoord, xEndCoord, yEndCoord; // for drawing drag N drop zoom
	private boolean drawRectangle = false;

	private final double zoomInConstant = 0.85;
	private final double zoomOutConstant = 1.1;

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
		visibleArea.setCoord(0, 0, 50000*8, 25000*8); // HELE DANMARK
		//visibleArea.setCoord(120000, 80000, 50000, 25000); // ODENSE
	}

	public void moveVisibleArea(double xCoord, double yCoord)
	{
		double xMapCoord = xCoord / getWidth() * visibleArea.getxLength() * 1.2;
		double yMapCoord = yCoord / getHeight() * visibleArea.getyLength() * 1.2;
		visibleArea.setCoord(visibleArea.getxCoord() + xMapCoord, visibleArea.getyCoord() + yMapCoord, visibleArea.getxLength(), visibleArea.getyLength());
	}

	public void dragNDropZoom(double xStartCoord, double yStartCoord, double xEndCoord, double yEndCoord)
	{
		double mapXStartCoord;
		double mapYStartCoord;
		double mapXEndCoord;
		double mapYEndCoord;

		if (xStartCoord < xEndCoord)
		{
			mapXStartCoord = convertMouseXToMap(xStartCoord);
			mapXEndCoord = convertMouseXToMap(xEndCoord);
		} else
		{
			mapXStartCoord = convertMouseXToMap(xEndCoord);
			mapXEndCoord = convertMouseXToMap(xStartCoord);
		}
		if (yStartCoord > yEndCoord)
		{
			mapYStartCoord = convertMouseYToMap(yStartCoord);
			mapYEndCoord = convertMouseYToMap(yEndCoord);
		} else
		{
			mapYStartCoord = convertMouseYToMap(yEndCoord);
			mapYEndCoord = convertMouseYToMap(yStartCoord);
		}

		double zoomconstant;
		if (mapXEndCoord - mapXStartCoord > mapYEndCoord - mapYStartCoord)
		{
			zoomconstant = (mapXEndCoord - mapXStartCoord) / visibleArea.getxLength();
		} else
		{
			zoomconstant = (mapYEndCoord - mapYStartCoord) / visibleArea.getyLength();
		}
		System.out.println("zoomconstant: " + zoomconstant);
		visibleArea.setCoord(mapXStartCoord, mapYStartCoord, visibleArea.getxLength() * zoomconstant, visibleArea.getyLength() * zoomconstant);
	}

	public void drawRectangle(int xStartCoord, int yStartCoord, int xEndCoord, int yEndCoord, boolean drawRectangle)
	{
		this.drawRectangle = drawRectangle;
		if (xStartCoord < xEndCoord)
		{
			this.xStartCoord = xStartCoord;
			this.xEndCoord = xEndCoord;
		} else
		{
			this.xStartCoord = xEndCoord;
			this.xEndCoord = xStartCoord;
		}
		if (yStartCoord < yEndCoord)
		{
			this.yStartCoord = yStartCoord;
			this.yEndCoord = yEndCoord;
		} else
		{
			this.yStartCoord = yEndCoord;
			this.yEndCoord = yStartCoord;
		}
	}

	private double convertMouseXToMap(double xCoord)
	{
		return visibleArea.getxCoord() + xCoord / getWidth() * visibleArea.getxLength();
	}

	private double convertMouseYToMap(double yCoord)
	{
		return visibleArea.getyCoord() + (getHeight() - yCoord) / getHeight() * visibleArea.getyLength();
	}

	public void zoomOut(double mouseXCoord, double mouseYCoord)
	{
		double mouseMapXCoord = convertMouseXToMap(mouseXCoord);
		double mouseMapYCoord = convertMouseYToMap(mouseYCoord);
		double mouseLengthX = mouseMapXCoord - visibleArea.getxCoord();
		double mouseLengthY = mouseMapYCoord - visibleArea.getyCoord();

		double xPct = mouseLengthX / visibleArea.getxLength();
		double yPct = mouseLengthY / visibleArea.getyLength();

		double xZoomLength = visibleArea.getxLength() * zoomOutConstant;
		double yZoomLength = visibleArea.getyLength() * zoomOutConstant;

		double deltaXLength = visibleArea.getxLength() - xZoomLength;
		double deltaYLength = visibleArea.getyLength() - yZoomLength;

		visibleArea.setCoord(visibleArea.getxCoord() + deltaXLength * xPct, visibleArea.getyCoord() + deltaYLength * yPct, xZoomLength, yZoomLength);
	}

	public void zoomIn(double mouseXCoord, double mouseYCoord)
	{
		double mouseMapXCoord = convertMouseXToMap(mouseXCoord);
		double mouseMapYCoord = convertMouseYToMap(mouseYCoord);
		double mouseLengthX = mouseMapXCoord - visibleArea.getxCoord();
		double mouseLengthY = mouseMapYCoord - visibleArea.getyCoord();

		double xPct = mouseLengthX / visibleArea.getxLength();
		double yPct = mouseLengthY / visibleArea.getyLength();

		double xZoomLength = visibleArea.getxLength() * zoomInConstant;
		double yZoomLength = visibleArea.getyLength() * zoomInConstant;

		double deltaXLength = visibleArea.getxLength() - xZoomLength;
		double deltaYLength = visibleArea.getyLength() - yZoomLength;

		visibleArea.setCoord(visibleArea.getxCoord() + deltaXLength * xPct, visibleArea.getyCoord() + deltaYLength * yPct, xZoomLength, yZoomLength);
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
				outerLoop:
				for (Edge edge : quadTree.getEdges())
				{
					double xlength = visibleArea.getxLength();
					double ylength = visibleArea.getyLength();
					
					double xVArea = visibleArea.getxCoord();
					double yVArea = visibleArea.getyCoord();
					
					for (int roadType : RoadTypeEnum.PLACENAME.getTypes())
					{
						if (roadType == edge.getRoadType())
						{
							if(xlength >= 10000){continue outerLoop;}
							g.setColor(Color.black);
							g.drawString(edge.getRoadName(), (int) (((edge.getMidNodeTrue().getxCoord() - xVArea) / xlength) * getWidth()), (int) (getSize().height - ((edge.getMidNodeTrue().getyCoord() - yVArea) / ylength) * getHeight()));
							continue outerLoop;
						}
					}
					for (int roadType : RoadTypeEnum.HIGHWAY.getTypes())
					{
						if (roadType == edge.getRoadType())
						{
							g.setColor(Color.black);
							break;
						}
					}
					for (int roadType : RoadTypeEnum.FERRYWAY.getTypes())
					{
						if (roadType == edge.getRoadType())
						{
							g.setColor(Color.blue);
							break;
						}
					}
					for (int roadType : RoadTypeEnum.SECONDARYROAD.getTypes())
					{
						if (roadType == edge.getRoadType())
						{
							if(xlength >= 400000){continue outerLoop;}
							g.setColor(Color.ORANGE);
							break;
						}
					}
					for (int roadType : RoadTypeEnum.PATHWAY.getTypes())
					{
						if (roadType == edge.getRoadType())
						{
							if(xlength >= 20000){continue outerLoop;}
							g.setColor(Color.green);
							break;
						}
					}
					for (int roadType : RoadTypeEnum.ROAD.getTypes())
					{
						if (roadType == edge.getRoadType())
						{
							if(xlength >= 100000){continue outerLoop;}
							g.setColor(Color.gray);
							break;
						}
					}
					double x1 = edge.getFromNodeTrue().getxCoord();
					double y1 = edge.getFromNodeTrue().getyCoord();
					double x2 = edge.getToNodeTrue().getxCoord();
					double y2 = edge.getToNodeTrue().getyCoord();
					g.drawLine((int) (((x1 - xVArea) / xlength) * getWidth()), (int) (getSize().height - ((y1 - yVArea) / ylength) * getHeight()), (int) (((x2 - xVArea) / xlength) * getWidth()), (int) (getSize().height - ((y2 - yVArea) / ylength) * getHeight()));
					g.drawLine((int) (((x1 - xVArea) / xlength) * getWidth()), (int) (getSize().height - ((y1 - yVArea) / ylength) * getHeight()), (int) (1 + ((x2 - xVArea) / xlength) * getWidth()), (int) (1 + getSize().height - ((y2 - yVArea) / ylength) * getHeight()));
					g.drawLine((int) (1 + ((x1 - xVArea) / xlength) * getWidth()), (int) (1 + getSize().height - ((y1 - yVArea) / ylength) * getHeight()), (int) (((x2 - xVArea) / xlength) * getWidth()), (int) (getSize().height - ((y2 - yVArea) / ylength) * getHeight()));
				}
			}
		}

		if (drawRectangle)
		{
			g.setColor(Color.black);
			g.drawRect(xStartCoord, yStartCoord, xEndCoord - xStartCoord, yEndCoord - yStartCoord);
			g.drawRect(xStartCoord + 1, yStartCoord + 1, xEndCoord - 2 - xStartCoord, yEndCoord - 2 - yStartCoord);
		}

		// when drawing: take the coord, substract its value with the startCoord from visible area
		// then divide by the length. that way you get values from 0-1.
	}


	@Override
	public void reshape(int x, int y, int w, int h)
	{
		//System.out.println("x: " + x + " y: " + y + " w: " + w + " h: " + h);
		
		//double constant = getWidth()/w;
		//visibleArea.setCoord(visibleArea.getxCoord(), visibleArea.getyCoord(), w*(visibleArea.getxLength()/getWidth())*constant, h*(visibleArea.getyLength()/getHeight())*constant);
		super.reshape(x, y, w, h); //To change body of generated methods, choose Tools | Templates.
	}
	
}
