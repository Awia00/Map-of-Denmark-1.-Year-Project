/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mapofdenmark.GUIPackage;

import database.Street;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
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
public class MainFrame extends JFrame implements MouseListener, MouseMotionListener, MouseWheelListener, KeyListener {

	private MapComponent drawMapComponent;
	private Container mainContainer;
	private JLabel mapOfDenmarkLabel;
	private JTextField enterAddressField;
	private JButton searchButton;
	private Dimension screenSize;
	private VisibleArea visibleArea;
	private Street[] streets;

	private Point oldPosition;
	private Point newPosition;

	private boolean mapInFocus;
	private int pressedKeyCode;

	public MainFrame()
	{
		// EVT MODTAGE streets I CONSTRUCTOR.
		initialize();
		addListeners();
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

		// components
		drawMapComponent = new MapComponent(visibleArea, streets);
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

	private void addListeners()
	{
		this.drawMapComponent.addMouseListener(this);
		this.drawMapComponent.addMouseMotionListener(this);
		this.drawMapComponent.addMouseWheelListener(this);
		this.drawMapComponent.addKeyListener(this);
	}

	@Override
	public Dimension getPreferredSize()
	{
		return new Dimension((int) (screenSize.width / 1.5), (int) (screenSize.height / 1.5));
	}

	public static void main(String[] args)
	{
		new MainFrame();
	}

	private Point getDeltaPoint(Point p1, Point p2)
	{
		int deltaX = (int) (p1.getX() - p2.getX());
		int deltaY = (int) (p2.getY() - p1.getY());

		return new Point(deltaX, deltaY);

	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		oldPosition = e.getPoint();
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		if (pressedKeyCode == 17) // ctrl key
		{
			newPosition = e.getPoint();
			drawMapComponent.dragNDropZoom(oldPosition.getX(), oldPosition.getY(), newPosition.getX(), newPosition.getY());
		}
		drawMapComponent.drawRectangle(0, 0, 0, 0, false);
		oldPosition = null;
		newPosition = null;
		repaint();
	}

	@Override
	public void mouseEntered(MouseEvent e)
	{
		//System.out.println("Mouse Entered");
		this.drawMapComponent.requestFocusInWindow();
	}

	@Override
	public void mouseExited(MouseEvent e)
	{

	}

	@Override
	public void mouseDragged(MouseEvent e)
	{
		if (pressedKeyCode == 17) // press ctrl key
		{
			newPosition = e.getPoint();
			drawMapComponent.drawRectangle((int) oldPosition.getX(), (int) oldPosition.getY(), (int) newPosition.getX(), (int) newPosition.getY(), true);
		} else  // just move
		{
			if (newPosition != null){oldPosition = newPosition;}
			newPosition = e.getPoint();
			int x = (int) getDeltaPoint(oldPosition, newPosition).getX();
			int y = (int) getDeltaPoint(oldPosition, newPosition).getY();
			drawMapComponent.moveVisibleArea(x, y);
		}
		repaint();

	}

	@Override
	public void mouseMoved(MouseEvent e)
	{

	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e)
	{
		System.out.println(e.getWheelRotation());
		if (e.getWheelRotation() < 0)
		{
			drawMapComponent.zoomIn(e.getX(), e.getY());
		} else
		{
			drawMapComponent.zoomOut(e.getX(), e.getY());
		}
		repaint();
	}

	@Override
	public void keyTyped(KeyEvent e)
	{

	}

	@Override
	public void keyPressed(KeyEvent e)
	{
		pressedKeyCode = e.getKeyCode();
	}

	@Override
	public void keyReleased(KeyEvent e)
	{
		pressedKeyCode = 0;
	}

}
