/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mapofdenmark.GUIPackage;

import java.awt.Color;
import java.awt.Container;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Class description:
 *
 * @version 0.1 - changed 27-02-2014
 * @authorNewVersion  Anders Wind - awis@itu.dk
 *
 * @buildDate 27-02-2014
 * @author Anders Wind - awis@itu.dk
 */
public class MainFrame extends JFrame {
    
    private MapComponent drawMapComponent;
	private Container container;
    
    public MainFrame()
    {
		initialize();        
    }
    
    private void initialize()
    {
		// frame properties
		setTitle("Map of Denmark");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		
		container = new JPanel();
		getContentPane().add(container);
		
		drawMapComponent = new MapComponent();
		container.add(drawMapComponent);
		
		
		// rdy up
		repaint();
		pack();
    }
    
	
	
	public static void main(String[] args)
	{
		new MainFrame();
	}
}
