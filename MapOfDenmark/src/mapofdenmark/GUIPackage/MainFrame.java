/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mapofdenmark.GUIPackage;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

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
	private Container EastContainer, WestContainer, East_SouthContainer, East_NorthContainer;
	private JLabel mapOfDenmarkLabel;
	private JTextField enterAddressField;
	private JButton searchButton;
	private Dimension screenSize;
    
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
		screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setMinimumSize(getMinimumSize());
		
		// Components
		drawMapComponent = new MapComponent();
		
		mapOfDenmarkLabel = new JLabel("The Map of Denmark");
		enterAddressField = new JTextField("Enter Address... ");
		searchButton = new JButton("Search");
		
		// Structure
		EastContainer = new JPanel(new BorderLayout());
		WestContainer = new JPanel();
		getContentPane().add(EastContainer, BorderLayout.EAST);
		getContentPane().add(WestContainer,BorderLayout.WEST);
		
		East_SouthContainer = new JPanel();
		East_NorthContainer = new JPanel();
		EastContainer.add(East_SouthContainer, BorderLayout.SOUTH);
		EastContainer.add(East_NorthContainer, BorderLayout.NORTH);
		
		// Content adding
		East_NorthContainer.add(mapOfDenmarkLabel);
		East_SouthContainer.add(drawMapComponent);
		East_SouthContainer.setBackground(Color.white);
		WestContainer.add(enterAddressField);
		WestContainer.add(searchButton);

		// Action listeners
		
		
		// rdy up
		repaint();
		pack();
    }
    
	
	

	
	@Override
    public Dimension getPreferredSize()
    {
        return new Dimension((int)(screenSize.getWidth()/1.001),(int)(screenSize.getHeight()/1.05));
    }

    @Override
    public Dimension getMinimumSize()
    {
        return new Dimension((int)(screenSize.getWidth()/1.5), (int)(screenSize.getHeight()/1.5));
    }
	
	@Override
	public Dimension getMaximumSize()
	{
		return getPreferredSize();
	}
	
	public static void main(String[] args)
	{
		new MainFrame();
	}
}
