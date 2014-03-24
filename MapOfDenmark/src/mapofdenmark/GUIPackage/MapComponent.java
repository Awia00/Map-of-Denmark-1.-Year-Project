/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mapofdenmark.GUIPackage;

import database.Edge;
import database.Street;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
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

	private QuadTree quadTreeToDraw;
	protected VisibleArea visibleArea;
	private int xStartCoord, yStartCoord, xEndCoord, yEndCoord; // for drawing drag N drop zoom
	private boolean drawRectangle = false;

	protected final double zoomInConstant = 0.98;
	protected final double zoomOutConstant = 1.02;

	protected double doneTimer = 0;

	private ColorScheme colorScheme;

	public MapComponent(VisibleArea visibleArea, Street[] streets, List<Edge> edges)
	{
		this.visibleArea = new VisibleArea();
		initialize(streets, edges);
	}

	private void initialize(Street[] streets, List<Edge> edges)
	{
		quadTreeToDraw = new QuadTree(edges, 0, 0, 590000);
		visibleArea.setCoord(-90000, 1000, 50000 * 12, 25000 * 12);

		// set the initial Color scheme to Standard Color scheme
		this.setColorScheme("sdadadafafa");
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
		g.setColor(this.colorScheme.getBackgroundColor());
		g.fillRect(0, 0, getSize().width - 1, getSize().height - 1);

		ArrayList<QuadTree> bottomTrees = QuadTree.getBottomTrees();
		double xlength = visibleArea.getxLength();
		double ylength = visibleArea.getyLength();

		double xVArea = visibleArea.getxCoord();
		double yVArea = visibleArea.getyCoord();

		double zoomFactorStroke = Math.sqrt(((quadTreeToDraw.getQuadTreeLength()) / (xlength*3)));

		// create strokes
		BasicStroke highWayStrokeBorder = new BasicStroke((float) (Math.max(3, (zoomFactorStroke * 1.2) + 1)), BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND);
		BasicStroke highWayStroke = new BasicStroke((float) (Math.max(2, (zoomFactorStroke * 1.2))), BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND);

		BasicStroke secondaryRoadStrokeBorder = new BasicStroke((float) (Math.max(2, (zoomFactorStroke * 0.9) + 1)), BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND);
		BasicStroke secondaryRoadStroke = new BasicStroke((float) (Math.max(1.3, (zoomFactorStroke *0.9))), BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND);

		BasicStroke normalRoadStrokeBorder = new BasicStroke((float) (Math.max(1.6, (zoomFactorStroke * 0.6) + 1)), BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND);
		BasicStroke normalRoadStroke = new BasicStroke((float) (Math.max(1, (zoomFactorStroke * 0.6))), BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND);

		BasicStroke smallRoadStrokeBorder = new BasicStroke((float) (Math.max(1.3, (zoomFactorStroke*0.3) + 1)), BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND);
		BasicStroke smallRoadStroke = new BasicStroke((float) (Math.max(1, (zoomFactorStroke*0.3))), BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND);

		BasicStroke pathRoadStrokeBorder = new BasicStroke((float) (Math.max(1.2, (zoomFactorStroke*0.1) + 1)), BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND);
		BasicStroke pathRoadStroke = new BasicStroke((float) (Math.max(1, (zoomFactorStroke*0.1))), BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND);

		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_SPEED);
		g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);	
		
		for (QuadTree quadTree : bottomTrees)
		{
			if (quadTree.isDrawable())
			{	
				if (xlength <= 15000)
				{
					for (Edge edge : quadTree.getPathEdges())
					{
						double x1 = edge.getFromNodeTrue().getxCoord();
						double y1 = edge.getFromNodeTrue().getyCoord();
						double x2 = edge.getToNodeTrue().getxCoord();
						double y2 = edge.getToNodeTrue().getyCoord();
						// drawing the border
						g.setColor(colorScheme.getPathwayBorderColor());
						g2.setStroke(pathRoadStrokeBorder);
						g.drawLine((int) (((x1 - xVArea) / xlength) * getWidth()), (int) (getSize().height - ((y1 - yVArea) / ylength) * getHeight()), (int) (((x2 - xVArea) / xlength) * getWidth()), (int) (getSize().height - ((y2 - yVArea) / ylength) * getHeight()));

						// drawing the road
						g.setColor(colorScheme.getPathwayColor());
						g2.setStroke(pathRoadStrokeBorder);
						g.drawLine((int) (((x1 - xVArea) / xlength) * getWidth()), (int) (getSize().height - ((y1 - yVArea) / ylength) * getHeight()), (int) (((x2 - xVArea) / xlength) * getWidth()), (int) (getSize().height - ((y2 - yVArea) / ylength) * getHeight()));
					}
				}
				for (Edge edge : quadTree.getFerryEdges())
				{
					g2.setStroke(new BasicStroke(2.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 0, new float[]
					{
						9
					}, 0));
					g.setColor(this.colorScheme.getFerrywayColor());
					double x1 = edge.getFromNodeTrue().getxCoord();
					double y1 = edge.getFromNodeTrue().getyCoord();
					double x2 = edge.getToNodeTrue().getxCoord();
					double y2 = edge.getToNodeTrue().getyCoord();
					g.drawLine((int) (((x1 - xVArea) / xlength) * getWidth()), (int) (getSize().height - ((y1 - yVArea) / ylength) * getHeight()), (int) (((x2 - xVArea) / xlength) * getWidth()), (int) (getSize().height - ((y2 - yVArea) / ylength) * getHeight()));
				}
				if (xlength <= 30000)
				{
					for (Edge edge : quadTree.getSmallEdges())
					{
						double x1 = edge.getFromNodeTrue().getxCoord();
						double y1 = edge.getFromNodeTrue().getyCoord();
						double x2 = edge.getToNodeTrue().getxCoord();
						double y2 = edge.getToNodeTrue().getyCoord();
						// drawing the border
						g.setColor(colorScheme.getSmallRoadBorderColor());
						g2.setStroke(smallRoadStrokeBorder);
						g.drawLine((int) (((x1 - xVArea) / xlength) * getWidth()), (int) (getSize().height - ((y1 - yVArea) / ylength) * getHeight()), (int) (((x2 - xVArea) / xlength) * getWidth()), (int) (getSize().height - ((y2 - yVArea) / ylength) * getHeight()));

						// drawing the road
						g.setColor(colorScheme.getSmallRoadColor());
						g2.setStroke(smallRoadStrokeBorder);
						g.drawLine((int) (((x1 - xVArea) / xlength) * getWidth()), (int) (getSize().height - ((y1 - yVArea) / ylength) * getHeight()), (int) (((x2 - xVArea) / xlength) * getWidth()), (int) (getSize().height - ((y2 - yVArea) / ylength) * getHeight()));
					}
				}
				if (xlength <= 100000)
				{
					for (Edge edge : quadTree.getNormalEdges())
					{
						double x1 = edge.getFromNodeTrue().getxCoord();
						double y1 = edge.getFromNodeTrue().getyCoord();
						double x2 = edge.getToNodeTrue().getxCoord();
						double y2 = edge.getToNodeTrue().getyCoord();

						// drawing the border
						g.setColor(colorScheme.getNormalRoadBorderColor());
						g2.setStroke(normalRoadStrokeBorder);
						g.drawLine((int) (((x1 - xVArea) / xlength) * getWidth()), (int) (getSize().height - ((y1 - yVArea) / ylength) * getHeight()), (int) (((x2 - xVArea) / xlength) * getWidth()), (int) (getSize().height - ((y2 - yVArea) / ylength) * getHeight()));

						// drawing the road
						g.setColor(colorScheme.getNormalRoadColor());
						g2.setStroke(normalRoadStroke);
						g.drawLine((int) (((x1 - xVArea) / xlength) * getWidth()), (int) (getSize().height - ((y1 - yVArea) / ylength) * getHeight()), (int) (((x2 - xVArea) / xlength) * getWidth()), (int) (getSize().height - ((y2 - yVArea) / ylength) * getHeight()));
					}
				}
				if (xlength <= 550000)
				{
					for (Edge edge : quadTree.getSecondaryEdges())
					{
						double x1 = edge.getFromNodeTrue().getxCoord();
						double y1 = edge.getFromNodeTrue().getyCoord();
						double x2 = edge.getToNodeTrue().getxCoord();
						double y2 = edge.getToNodeTrue().getyCoord();
						// drawing the border
						g.setColor(colorScheme.getSecondaryRoadBorderColor());
						g2.setStroke(secondaryRoadStrokeBorder);
						g.drawLine((int) (((x1 - xVArea) / xlength) * getWidth()), (int) (getSize().height - ((y1 - yVArea) / ylength) * getHeight()), (int) (((x2 - xVArea) / xlength) * getWidth()), (int) (getSize().height - ((y2 - yVArea) / ylength) * getHeight()));

						// drawing the road
						g.setColor(colorScheme.getSecondaryRoadColor());
						g2.setStroke(secondaryRoadStroke);
						g.drawLine((int) (((x1 - xVArea) / xlength) * getWidth()), (int) (getSize().height - ((y1 - yVArea) / ylength) * getHeight()), (int) (((x2 - xVArea) / xlength) * getWidth()), (int) (getSize().height - ((y2 - yVArea) / ylength) * getHeight()));
					}
				}
				for (Edge edge : quadTree.getHighwayEdges())
				{
					double x1 = edge.getFromNodeTrue().getxCoord();
					double y1 = edge.getFromNodeTrue().getyCoord();
					double x2 = edge.getToNodeTrue().getxCoord();
					double y2 = edge.getToNodeTrue().getyCoord();

					// drawing the border
					g.setColor(colorScheme.getHighwayBorderColor());
					g2.setStroke(highWayStrokeBorder);
					g.drawLine((int) (((x1 - xVArea) / xlength) * getWidth()), (int) (getSize().height - ((y1 - yVArea) / ylength) * getHeight()), (int) (((x2 - xVArea) / xlength) * getWidth()), (int) (getSize().height - ((y2 - yVArea) / ylength) * getHeight()));

					// drawing the road
					g.setColor(colorScheme.getHighwayColor());
					g2.setStroke(highWayStroke);
					g.drawLine((int) (((x1 - xVArea) / xlength) * getWidth()), (int) (getSize().height - ((y1 - yVArea) / ylength) * getHeight()), (int) (((x2 - xVArea) / xlength) * getWidth()), (int) (getSize().height - ((y2 - yVArea) / ylength) * getHeight()));
				}
				if (xlength <= 10000)
				{
					for (Edge edge : quadTree.getPlaceNameEdges())
					{
						g.setColor(this.colorScheme.getPlaceNameColor());
						g.setFont(new Font("Verdana", Font.BOLD, 12));
						g.drawString(edge.getRoadName(), (int) (((edge.getMidNodeTrue().getxCoord() - xVArea) / xlength) * getWidth()), (int) (getSize().height - ((edge.getMidNodeTrue().getyCoord() - yVArea) / ylength) * getHeight()));
					}
				}
			}
		}
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
		g2.setStroke(new BasicStroke(1));
		g.setColor(Color.black);
		g.drawRect(0, 0, getSize().width - 1, getSize().height - 1);

		if (drawRectangle)
		{
			g.setColor(Color.black);
			g.drawRect(xStartCoord, yStartCoord, xEndCoord - xStartCoord, yEndCoord - yStartCoord);
			g.drawRect(xStartCoord + 1, yStartCoord + 1, xEndCoord - 2 - xStartCoord, yEndCoord - 2 - yStartCoord);
		}

		// when drawing: take the coord, substract its value with the startCoord from visible area
		// then divide by the length. that way you get values from 0-1.
	}

	protected void setColorScheme(String colorScheme)
	{
		switch (colorScheme)
		{
			case "Standard":
				//set Standard ColorScheme
				this.colorScheme = new ColorScheme("Standard", Color.white, Color.black, new Color(100, 100, 100), Color.orange, new Color(230, 140, 0), Color.gray, new Color(90, 90, 90), Color.gray, new Color(90, 90, 90), new Color(0,230,30), new Color(0, 170, 10), Color.blue, Color.black);
				break;
			case "Night":
				// set Night ColorScheme
				this.colorScheme = new ColorScheme("Night", Color.black, Color.orange, Color.gray, Color.cyan, Color.cyan, Color.magenta, Color.blue, Color.red);
				break;

			case "Funky":
				// set Funky ColorScheme
				this.colorScheme = new ColorScheme("Funky", Color.red, Color.magenta, Color.green, Color.yellow, Color.yellow, Color.magenta, Color.blue, Color.white);
				break;

			default:
				// default to Standard ColorScheme
				setColorScheme("Standard");
				break;
		}
	}
}
