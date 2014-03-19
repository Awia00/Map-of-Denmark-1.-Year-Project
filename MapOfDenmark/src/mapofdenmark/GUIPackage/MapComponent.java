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
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
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
	protected VisibleArea visibleArea;
	private int xStartCoord, yStartCoord, xEndCoord, yEndCoord; // for drawing drag N drop zoom
	private boolean drawRectangle = false;

	protected final double zoomInConstant = 0.98;
	protected final double zoomOutConstant = 1.02;

	protected double doneTimer = 0;

	public MapComponent(VisibleArea visibleArea, Street[] streets, List<Edge> edges)
	{
		this.visibleArea = new VisibleArea();
		initialize(streets, edges);
	}

	private void initialize(Street[] streets, List<Edge> edges)
	{
		//List<Edge> edges = db.getEdges();

		// DATABASEN SKAL FJERNES HERFRA STREET[] streets BRUGES I STEDET.
		quadTreeToDraw = new QuadTree(edges, 0, 0, 590000);
		visibleArea.setCoord(-90000, 1000, 50000 * 12, 25000 * 12); // HELE DANMARK
		//visibleArea.setCoord(120000, 80000, 50000, 25000); // ODENSE
	}

	/**
	 *
	 * @param xCoord is negative if move map to the right
	 * @param yCoord is negative if move map to the down
	 */
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
		int fixRounds = 0;
		while (visibleArea.getxLength() * zoomconstant < 1000 && fixRounds != 20)
		{
			zoomconstant += 0.02;
			fixRounds++;
		}
		if (fixRounds >= 20)
		{
			return;
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
		if (visibleArea.getyLength() + 10000 >= quadTreeToDraw.getQuadTreeLength())
		{
			return;
		}
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
		if (visibleArea.getyLength() <= 100)
		{
			return;
		}
		final double mouseMapXCoord = convertMouseXToMap(mouseXCoord);
		final double mouseMapYCoord = convertMouseYToMap(mouseYCoord);
		final double mouseLengthX = mouseMapXCoord - visibleArea.getxCoord();
		final double mouseLengthY = mouseMapYCoord - visibleArea.getyCoord();

		final double xPct = mouseLengthX / visibleArea.getxLength();
		final double yPct = mouseLengthY / visibleArea.getyLength();

		final double xZoomLength = visibleArea.getxLength() * zoomInConstant;
		final double yZoomLength = visibleArea.getyLength() * zoomInConstant;

		final double deltaXLength = visibleArea.getxLength() - xZoomLength;
		final double deltaYLength = visibleArea.getyLength() - yZoomLength;

		final double newStartX = visibleArea.getxCoord() + deltaXLength * xPct;
		final double newStartY = visibleArea.getyCoord() + deltaYLength * yPct;

		visibleArea.setCoord(newStartX, newStartY, xZoomLength, yZoomLength);
		/*
		 visibleArea.setCoordWithoutUpdate(newStartX*doneTimer+oldStartX*(1-doneTimer),
		 newStartY*doneTimer + oldStartY*(1-doneTimer),
		 xZoomLength*doneTimer+oldxLength*(1-doneTimer), yZoomLength*doneTimer+oldyLength*(1-doneTimer));
		 */
	}

	@Override
	public void paint(Graphics g)
	{
		// draw the map white and with a border
		Graphics2D g2 = (Graphics2D) g;
		g.setColor(Color.white);
		g.fillRect(0, 0, getSize().width - 1, getSize().height - 1);

		ArrayList<QuadTree> bottomTrees = QuadTree.getBottomTrees();
		double xlength = visibleArea.getxLength();
		double ylength = visibleArea.getyLength();

		double xVArea = visibleArea.getxCoord();
		double yVArea = visibleArea.getyCoord();

		
		BasicStroke[] highwayStrokes =
		{
			new BasicStroke(2), new BasicStroke(3), new BasicStroke(4), new BasicStroke(5)
		};
		BasicStroke[] secondaryStrokes =
		{
			new BasicStroke(1.5f), new BasicStroke(2), new BasicStroke(3)
		};
		BasicStroke[] normalStrokes =
		{
			new BasicStroke(1.5f), new BasicStroke(2), new BasicStroke(2.5f)
		};
		BasicStroke[] pathStrokes =
		{
			new BasicStroke(1.3f), new BasicStroke(1.8f)
		};

		for (QuadTree quadTree : bottomTrees)
		{
			if (quadTree.isDrawable())
			{
				g.setColor(Color.black);
				if (xlength <= 10000)
				{
					g2.setStroke(highwayStrokes[3]);
				} else if (xlength <= 15000)
				{
					g2.setStroke(highwayStrokes[2]);
				} else if (xlength <= 33000)
				{
					g2.setStroke(highwayStrokes[1]);
				} else
				{
					g2.setStroke(highwayStrokes[0]);
				}
				for (Edge edge : quadTree.getHighwayEdges())
				{
					double x1 = edge.getFromNodeTrue().getxCoord();
					double y1 = edge.getFromNodeTrue().getyCoord();
					double x2 = edge.getToNodeTrue().getxCoord();
					double y2 = edge.getToNodeTrue().getyCoord();

					g.drawLine((int) (((x1 - xVArea) / xlength) * getWidth()), (int) (getSize().height - ((y1 - yVArea) / ylength) * getHeight()), (int) (((x2 - xVArea) / xlength) * getWidth()), (int) (getSize().height - ((y2 - yVArea) / ylength) * getHeight()));
				}
				if (xlength <= 550000)
				{
					g.setColor(Color.orange);
					if (xlength <= 8000)
					{
						g2.setStroke(secondaryStrokes[2]);
					} else if (xlength <= 27000)
					{
						g2.setStroke(secondaryStrokes[1]);
					} else
					{
						g2.setStroke(secondaryStrokes[0]);
					}
					for (Edge edge : quadTree.getSecondaryEdges())
					{
						double x1 = edge.getFromNodeTrue().getxCoord();
						double y1 = edge.getFromNodeTrue().getyCoord();
						double x2 = edge.getToNodeTrue().getxCoord();
						double y2 = edge.getToNodeTrue().getyCoord();
						g.drawLine((int) (((x1 - xVArea) / xlength) * getWidth()), (int) (getSize().height - ((y1 - yVArea) / ylength) * getHeight()), (int) (((x2 - xVArea) / xlength) * getWidth()), (int) (getSize().height - ((y2 - yVArea) / ylength) * getHeight()));
					}
				}
				if (xlength <= 100000)
				{
					g.setColor(Color.gray);
					if (xlength <= 8000)
					{
						g2.setStroke(normalStrokes[2]);
					} else if (xlength <= 27000)
					{
						g2.setStroke(normalStrokes[1]);
					} else
					{
						g2.setStroke(normalStrokes[0]);
					}
					for (Edge edge : quadTree.getNormalEdges())
					{
						double x1 = edge.getFromNodeTrue().getxCoord();
						double y1 = edge.getFromNodeTrue().getyCoord();
						double x2 = edge.getToNodeTrue().getxCoord();
						double y2 = edge.getToNodeTrue().getyCoord();
						g.drawLine((int) (((x1 - xVArea) / xlength) * getWidth()), (int) (getSize().height - ((y1 - yVArea) / ylength) * getHeight()), (int) (((x2 - xVArea) / xlength) * getWidth()), (int) (getSize().height - ((y2 - yVArea) / ylength) * getHeight()));
					}
				}
				if (xlength <= 35000)
				{
					g.setColor(Color.gray);
					if (xlength <= 7000)
					{
						g2.setStroke(normalStrokes[2]);
					} else if (xlength <= 18000)
					{
						g2.setStroke(normalStrokes[1]);
					} else
					{
						g2.setStroke(normalStrokes[0]);
					}
					for (Edge edge : quadTree.getSmallEdges())
					{
						double x1 = edge.getFromNodeTrue().getxCoord();
						double y1 = edge.getFromNodeTrue().getyCoord();
						double x2 = edge.getToNodeTrue().getxCoord();
						double y2 = edge.getToNodeTrue().getyCoord();
						g.drawLine((int) (((x1 - xVArea) / xlength) * getWidth()), (int) (getSize().height - ((y1 - yVArea) / ylength) * getHeight()), (int) (((x2 - xVArea) / xlength) * getWidth()), (int) (getSize().height - ((y2 - yVArea) / ylength) * getHeight()));
					}
				}
				if (xlength <= 20000)
				{
					g.setColor(Color.green);
					if (xlength <= 8000)
					{
						g2.setStroke(pathStrokes[1]);
					}  else
					{
						g2.setStroke(pathStrokes[0]);
					}
					for (Edge edge : quadTree.getPathEdges())
					{
						double x1 = edge.getFromNodeTrue().getxCoord();
						double y1 = edge.getFromNodeTrue().getyCoord();
						double x2 = edge.getToNodeTrue().getxCoord();
						double y2 = edge.getToNodeTrue().getyCoord();
						g.drawLine((int) (((x1 - xVArea) / xlength) * getWidth()), (int) (getSize().height - ((y1 - yVArea) / ylength) * getHeight()), (int) (((x2 - xVArea) / xlength) * getWidth()), (int) (getSize().height - ((y2 - yVArea) / ylength) * getHeight()));
					}
				}
				if (xlength <= 10000)
				{
					for (Edge edge : quadTree.getPlaceNameEdges())
					{
						g.setColor(Color.black);
						g.drawString(edge.getRoadName(), (int) (((edge.getMidNodeTrue().getxCoord() - xVArea) / xlength) * getWidth()), (int) (getSize().height - ((edge.getMidNodeTrue().getyCoord() - yVArea) / ylength) * getHeight()));
					}
				}
				for (Edge edge : quadTree.getFerryEdges())
				{
					g2.setStroke(new BasicStroke(2.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND));
					g.setColor(Color.blue);
					double x1 = edge.getFromNodeTrue().getxCoord();
					double y1 = edge.getFromNodeTrue().getyCoord();
					double x2 = edge.getToNodeTrue().getxCoord();
					double y2 = edge.getToNodeTrue().getyCoord();
					g.drawLine((int) (((x1 - xVArea) / xlength) * getWidth()), (int) (getSize().height - ((y1 - yVArea) / ylength) * getHeight()), (int) (((x2 - xVArea) / xlength) * getWidth()), (int) (getSize().height - ((y2 - yVArea) / ylength) * getHeight()));
				}
			}
			g2.setStroke(new BasicStroke(1));
			g.setColor(Color.black);
			g.drawRect(0, 0, getSize().width - 1, getSize().height - 1);
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
