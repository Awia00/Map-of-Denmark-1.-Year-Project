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
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import net.miginfocom.swing.MigLayout;

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
	private Container mainContainer, mapContainer;
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
		setExtendedState(MAXIMIZED_BOTH);
		screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		MigLayout migMainLayout = new MigLayout("", "[125!]10[center]", "[]10[top]");
		
		// Components
		//List<Point2D> points = new ArrayList<>();
		/*
                for (int i = 0; i < 600; i++)
		{
			points.add(new Point2D.Double(QuadTree.randomInRange(10.0, 200.0), QuadTree.randomInRange(10.0, 200.0)));
		}*/
                
                DatabaseInterface db = Database.db();
                
                List<Node> points = db.getNodes();
                //List<Edge> edges = db.getEdges();
                List<Point2D> nodes = new ArrayList<>();
                for(Node node : points){
					//System.out.println(node.getxCoord() + " " + node.getyCoord());
                    nodes.add(new Point2D.Double((node.getxCoord() - 6040000)/800 ,(node.getyCoord()- 440000)/1000));
                }
                
		drawMapComponent = new MapComponent(new QuadTree(nodes,0,0,460500/1000)); //892658.21706,6402050.98297,1000000));
		mapOfDenmarkLabel = new JLabel("The Map of Denmark");
		enterAddressField = new JTextField("Enter Address... ");
		searchButton = new JButton("Search");
		
		// Structure
		mainContainer = new JPanel(migMainLayout);
		//drawMapComponent.setSize(new Dimension((int)(getSize().width/1.2), (int)(getSize().height/1.2)));
		
		
		getContentPane().add(mainContainer);
		mainContainer.add(mapOfDenmarkLabel, "cell 1 0");
		mainContainer.add(enterAddressField, "cell 0 1");
		mainContainer.add(drawMapComponent, "cell 1 1,"
				+ "width "+(int)(screenSize.width/2.5)+":"+(int)(screenSize.width-125)+":, "
				+ "height "+(int)(screenSize.height/2.5)+":"+(int)(screenSize.height-25)+":, left");
		
		// Action listeners
		
		
		// rdy up
		revalidate();
		repaint();
		pack();
		setVisible(true);
		
		
    }

	@Override
	public Dimension getPreferredSize()
	{
		return new Dimension((int)(screenSize.width/1.5),(int)(screenSize.height/1.5));
	}
    
	
	
	
	

	public static void main(String[] args)
	{
		new MainFrame();
	}
	
}
