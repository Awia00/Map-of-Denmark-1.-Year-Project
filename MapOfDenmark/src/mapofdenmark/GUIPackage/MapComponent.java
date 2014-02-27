/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mapofdenmark.GUIPackage;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
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
    
    public MapComponent()
    {
		
    }
	
	@Override
    public void paint(Graphics g)
	{
		g.setColor(Color.red);
		g.fillRect(5, 60, 30, 40);
	}
	
	@Override
    public Dimension getPreferredSize()
    {
        return new Dimension(200,200);
    }

    @Override
    public Dimension getMinimumSize()
    {
        return getPreferredSize();
    }
}
