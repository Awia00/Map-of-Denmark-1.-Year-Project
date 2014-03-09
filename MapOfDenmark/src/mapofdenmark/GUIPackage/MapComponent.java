/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mapofdenmark.GUIPackage;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Point2D;
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
    
    public MapComponent(QuadTree quadTreeToDraw)
    {
		this.quadTreeToDraw = quadTreeToDraw;
		initialize();
    }
	
	private void initialize()
	{
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
			g.fillRect((int)(point.getY()/quadTreeToDraw.length*getWidth()), getSize().height-(int)(point.getX()/quadTreeToDraw.length*getHeight()), 2, 2);
		}
	}
}
