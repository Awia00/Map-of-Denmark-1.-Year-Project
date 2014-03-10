/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mapofdenmark.GUIPackage;

import database.Database;
import database.DatabaseInterface;
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
 * @authorNewVersion  Anders Wind - awis@itu.dk
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
		this.visibleArea = visibleArea;
		initialize(streets);
    }
	
	private void initialize(Street[] streets)
	{
		/*
		DatabaseInterface db = Database.db();

		List<Node> points = db.getNodes();
		//List<Edge> edges = db.getEdges();
		List<Point2D> nodes = new ArrayList<>();
		for (Node node : points)
		{
			//System.out.println(node.getxCoord() + " " + node.getyCoord());
			nodes.add(new Point2D.Double((node.getxCoord() - 6020000), (node.getyCoord() - 438000)));
		}
				
		
		// DATABASEN SKAL FJERNES HERFRA STREET[] streets BRUGES I STEDET.
		quadTreeToDraw = new QuadTree(nodes, 0, 0, 460500);
		visibleArea.setCoord(0, 0, 460500, 460500, quadTreeToDraw);
				*/
	}
	
	@Override
    public void paint(Graphics g)
	{
		g.setColor(Color.white);
		g.fillRect(0, 0, getSize().width-1, getSize().height-1);
		g.setColor(Color.red);
		g.drawRect(0, 0, getSize().width-1, getSize().height-1);
		g.setColor(Color.blue);
		for(Point2D point : quadTreeToDraw.getPoints())
		{
			g.fillRect((int)(point.getY()/quadTreeToDraw.length*getWidth()), getSize().height-(int)(point.getX()/quadTreeToDraw.length*getHeight()), 1, 1);
		}
		
		// when drawing: take the coord, substract its value with the startCoord from visible area
		// then divide by the length. that way you get values from 0-1.
	}
}
