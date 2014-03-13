/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mapofdenmark.GUIPackage;

import database.Street;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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
 * @authorNewVersion Anders Wind - awis@itu.dk
 *
 * @buildDate 27-02-2014
 * @author Anders Wind - awis@itu.dk
 */
public class MainFrame extends JFrame implements MouseListener{

	private MapComponent drawMapComponent;
	private Container EastContainer, WestContainer, East_SouthContainer, East_NorthContainer;
	private Container mainContainer, mapContainer;
	private JLabel mapOfDenmarkLabel;
	private JTextField enterAddressField;
	private JButton searchButton;
	private Dimension screenSize;
	private VisibleArea visibleArea;
	private Street[] streets;

	public MainFrame()
	{
		// EVT MODTAGE streets I CONSTRUCTOR.
		initialize();
                this.drawMapComponent.addMouseListener(this);
	}

	private void initialize()
	{
		// frame properties
		setTitle("Map of Denmark");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setExtendedState(MAXIMIZED_BOTH);
		requestFocus();
		screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		MigLayout migMainLayout = new MigLayout("", "[125!]10[center]", "[]10[top]");
                
		// 
		drawMapComponent = new MapComponent(visibleArea,streets);
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
				+ "width " + (int) (screenSize.width / 2.5) + ":" + (int) (screenSize.width - 125) + ":, "
				+ "height " + (int) (screenSize.height / 2.5) + ":" + (int) (screenSize.height - 25) + ":, left");

		// Action listeners
		// rdy up
		revalidate();
		repaint();
		pack();
		setVisible(true);

	}
	
//	private void addListeners()
//	{
//		drawMapComponent.addMouseListener(new MouseListener() {
//
//			@Override
//			public void mouseClicked(MouseEvent e)
//			{
//				System.out.println("CLICKED");
//			}
//
//			@Override
//			public void mousePressed(MouseEvent e)
//			{
//				System.out.println("PRESSED");
//			}
//
//			@Override
//			public void mouseReleased(MouseEvent e)
//			{
//				System.out.println("RELEASED");
//			}
//
//			@Override
//			public void mouseEntered(MouseEvent e)
//			{
//				System.out.println("ENTERED");
//			}
//
//			@Override
//			public void mouseExited(MouseEvent e)
//			{
//				System.out.println("EXITED");
//			}
//		});
//	}

	@Override
	public Dimension getPreferredSize()
	{
		return new Dimension((int) (screenSize.width / 1.5), (int) (screenSize.height / 1.5));
	}

	public static void main(String[] args)
	{
		new MainFrame();
	}

    @Override
    public void mouseClicked(MouseEvent e) {
        System.out.println("Mouse Clicked at " + e.getX() + ", " + e.getY());
    }

    @Override
    public void mousePressed(MouseEvent e) {
       
    }

    @Override
    public void mouseReleased(MouseEvent e) {
       
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        System.out.println("Mouse Entered");
    }

    @Override
    public void mouseExited(MouseEvent e) {
       
    }

}
