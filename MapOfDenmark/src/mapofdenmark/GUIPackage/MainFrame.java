/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mapofdenmark.GUIPackage;

import database.Edge;
import database.Node;
import database.Street;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;
import net.miginfocom.swing.MigLayout;
import org.nocrala.tools.gis.data.esri.shapefile.shape.shapes.PolygonShape;

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
	private Container mainContainer, sideContainer;
	private NavigatonBar navigationBar;
	private JLabel mapOfDenmarkLabel;
	protected JLabel closestRoadLabel;
	private JTextField enterAddressField;
	private JButton searchButton;
	private Dimension screenSize;

	private Point oldPosition;
	private Point newPosition;
        
        private Node fromNode = null;
        private Node toNode = null;

	private boolean mapInFocus;
	private int pressedKeyCode;
	protected double timerDoneIn = 0;
	protected double timerDoneOut = 0;
	Timer timer = new Timer();
	Timer mouseStillTimer = new Timer();

	public MainFrame(QuadTree quadTree, List<PolygonShape> landShapePolygons, List<PolygonShape> landUsePolygons)
	{
		// EVT MODTAGE streets I CONSTRUCTOR.
		initialize(quadTree, landShapePolygons, landUsePolygons);
		addListeners();
	}

	private void initialize(QuadTree quadTree, List<PolygonShape> landShapePolygons, List<PolygonShape> landUsePolygons)
	{
		try
		{
			setIconImage(ImageIO.read(new File("assets/Icon48.png")));

		} catch (IOException ex)
		{
			Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
		}
		// frame properties
		setTitle("Map of Denmark");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setExtendedState(MAXIMIZED_BOTH);
		requestFocus();
		screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		MigLayout migMainLayout = new MigLayout("", "[180!]10[center]", "[]10[top]");

		// components
		drawMapComponent = new MapComponent(quadTree,landShapePolygons,landUsePolygons);
		//mapOfDenmarkLabel = new JLabel("The Map of Denmark");
		closestRoadLabel = new AAJLabel("Closest road");
		enterAddressField = new JTextField("Enter Address... ");
		searchButton = new JButton("Search");

		// Structure
//		sideContainer = new JPanel(new MigLayout());
//		sideContainer.add(enterAddressField, "wrap");
//		sideContainer.add(closestRoadLabel, "wrap");
		navigationBar = new NavigatonBar(drawMapComponent);

		mainContainer = new JPanel(migMainLayout);

		getContentPane().add(mainContainer);
		//mainContainer.add(mapOfDenmarkLabel, "cell 1 0");
		mainContainer.add(navigationBar, "cell 0 1");
		mainContainer.add(drawMapComponent, "cell 1 1,"
				+ "width " + (int) (screenSize.width / 2.5) + ":" + (int) (screenSize.width - 125) + ":, "
				+ "height " + (int) (screenSize.height / 2.5) + ":" + (int) (screenSize.height - 25) + ":, left");

		//menubar
		JMenuBar menubar = new JMenuBar();
		this.setJMenuBar(menubar);

		// create the Color scheme menu
		JMenu colorSchemeMenu = new JMenu("Color scheme");
		menubar.add(colorSchemeMenu);
		colorSchemeMenu.setFont(FontLoader.getFontWithSize("Roboto-Light", 14f));

		// create the Stardard menu item
		JMenuItem standardItem = new JMenuItem("Standard");
		standardItem.setFont(FontLoader.getFontWithSize("Roboto-Light", 14f));
		standardItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				drawMapComponent.setColorScheme("Standard");
			}
		});

		// create the Night menu item
		JMenuItem nightItem = new JMenuItem("Night");
		nightItem.setFont(FontLoader.getFontWithSize("Roboto-Light", 14f));
		nightItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				drawMapComponent.setColorScheme("Night");
			}
		});

		// create the Coloblind menu item 
		JMenuItem colorblindItem = new JMenuItem("Colorblind");
		colorblindItem.setFont(FontLoader.getFontWithSize("Roboto-Light", 14f));
		colorblindItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				drawMapComponent.setColorScheme("Colorblind");
			}
		});
		// add the menu items to the Color scheme menu
		colorSchemeMenu.add(standardItem);
		colorSchemeMenu.add(nightItem);
		colorSchemeMenu.add(colorblindItem);

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

	/**
	 * Call the smooth zoom method in mapComponent according to which values
	 * wheelRotation has. The method is called about 10 times over the course of
	 * approximately 10 times. If there is a zoom in method already being
	 * called, cancel that.
	 *
	 * @param mouseX
	 * @param mouseY
	 * @param wheelRotation if it is positive then zoom in, if negative zoom
	 * out.
	 */
	private void callSmoothZoom(double mouseX, double mouseY, int wheelRotation)
	{
		final double coordX = mouseX;
		final double coordY = mouseY;
		if (wheelRotation < 0)
		{
			if (timerDoneOut != 0)
			{
				timerDoneOut = 0;
				timer.cancel();
				timer.purge();
				timer = new Timer();
			}
			timerDoneIn = 0;
			TimerTask task = new TimerTask() {
				@Override
				public void run()
				{
					timerDoneIn += 0.05;
					drawMapComponent.zoomIn(coordX, coordY);
					callRepaint();
					if (timerDoneIn >= 1.2)
					{
						timer.cancel();
						timerDoneIn = 0;
						timer.purge();
						timer = new Timer();
					}
				}
			};
			timer.scheduleAtFixedRate(task, 10, 10);
		} else
		{
			if (timerDoneIn != 0)
			{
				timerDoneIn = 0;
				timer.cancel();
				timer.purge();
				timer = new Timer();
			}
			timerDoneOut = 0;
			TimerTask task = new TimerTask() {
				@Override
				public void run()
				{
					timerDoneOut += 0.05;
					drawMapComponent.zoomOut(coordX, coordY);
					callRepaint();
					if (timerDoneOut >= 1.2)
					{
						timer.cancel();
						timerDoneOut = 0;
						timer.purge();
						timer = new Timer();
					}
				}
			};
			timer.scheduleAtFixedRate(task, 10, 10);
		}
		repaint();
	}

	/**
	 * Get the delta distance from one point to another. This is used in the
	 * paning function.
	 *
	 * @param p1
	 * @param p2
	 * @return the delta distance from p1 to p2.
	 */
	private Point getDeltaPoint(Point p1, Point p2)
	{
		int deltaX = (int) (p1.getX() - p2.getX());
		int deltaY = (int) (p2.getY() - p1.getY());

		return new Point(deltaX, deltaY);

	}

	@Override
	public Dimension getPreferredSize()
	{
		return new Dimension((int) (screenSize.width / 1.5), (int) (screenSize.height / 1.5));
	}

	/**
	 * If the alt key is pressed then the method is used for developing, and is
	 * not something the user will notice. If the alt key is not pressed and the
	 * user double clicks, then either zoom in or zoom out will be called
	 * depending on which mouse-key was double pressed.
	 *
	 *
	 * @param e
	 */
	@Override
	public void mouseClicked(MouseEvent e)
	{
		if (e.getClickCount() >= 2)
		{
			if (e.getButton() == MouseEvent.BUTTON3)
			{
				callSmoothZoom(e.getX(), e.getY(), 1);
                                
                                Edge edge = drawMapComponent.findClosestRoad(e.getX(), e.getY());
                                toNode = edge.getFromNode();
                                edge.setInShortestPath(true);
                                System.out.println(toNode);
			} else if (e.getButton() == MouseEvent.BUTTON1)
			{
				callSmoothZoom(e.getX(), e.getY(), -1);
                                
                                Edge edge = drawMapComponent.findClosestRoad(e.getX(), e.getY());
                                fromNode = edge.getFromNode();
                                edge.setInShortestPath(true);
                                navigationBar.getFrom().setText(edge.getRoadName());
                                System.out.println(fromNode);
			}
		}
                if (e.getButton() == MouseEvent.BUTTON3)
			{
                                Edge edge = drawMapComponent.findClosestRoad(e.getX(), e.getY());
                                toNode = edge.getFromNode();
                                navigationBar.setToNode(toNode);
                                navigationBar.getTo().setText(edge.getRoadName());
                                System.out.println(toNode);
			} else if (e.getButton() == MouseEvent.BUTTON1)
			{

                                Edge edge = drawMapComponent.findClosestRoad(e.getX(), e.getY());
                                fromNode = edge.getFromNode();
                                navigationBar.setFromNode(fromNode);
                                navigationBar.getFrom().setText(edge.getRoadName());
                                drawMapComponent.setFrom(e.getX(), e.getY());
                                repaint();
			}
                
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		oldPosition = e.getPoint();
	}

	/**
	 * If the ctrl key is pressed then the drag and drop rectangle is removed
	 * and then the map is zoomed in.
	 *
	 * @param e
	 */
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
		this.drawMapComponent.requestFocusInWindow();
	}

	@Override
	public void mouseExited(MouseEvent e)
	{

	}

	/**
	 * The mouseDragged method either calls the moveVisibleArea method in
	 * mapComponent(pan the map), or if the ctrl key is pressed, draws a
	 * rectangle that will be used for the drag N drop zoom.
	 *
	 * @param e
	 */
	@Override
	public void mouseDragged(MouseEvent e)
	{
		if (pressedKeyCode == 17) // press ctrl key
		{
			newPosition = e.getPoint();
			drawMapComponent.drawRectangle((int) oldPosition.getX(), (int) oldPosition.getY(), (int) newPosition.getX(), (int) newPosition.getY(), true);
		} else  // just move
		{
			if (newPosition != null)
			{
				oldPosition = newPosition;
			}
			newPosition = e.getPoint();
			int x = (int) getDeltaPoint(oldPosition, newPosition).getX();
			int y = (int) getDeltaPoint(oldPosition, newPosition).getY();
			drawMapComponent.moveVisibleArea(x, y);
		}
		repaint();
	}

	/**
	 * When the mouse is moved, create a timer that will when ran, call the
	 * findClosest road method in mapComponent.
	 *
	 * @param e
	 */
	@Override
	public void mouseMoved(MouseEvent e)
	{
		final MouseEvent mouseEvent = e;

		TimerTask task = new TimerTask() {

			@Override
			public void run()
			{
				Edge e = drawMapComponent.findClosestRoad(mouseEvent.getX(), mouseEvent.getY());
                                String closestRoadString = e.getRoadName();
				// this implementation is copied from http://stackoverflow.com/questions/4212675/wrap-the-string-after-a-number-of-character-word-wise-in-java
				StringBuilder sb = new StringBuilder(closestRoadString);

				int i = 0;
				while ((i = sb.indexOf(" ", i + 10)) != -1)
				{
					sb.replace(i, i + 1, "<br>");
				}

				navigationBar.getClosestRoad().setText("<html>" + sb.toString() + " </html>");
				navigationBar.repaint();
			}
		};
		mouseStillTimer.cancel();
		mouseStillTimer = new Timer();
		mouseStillTimer.schedule(task, 150);
	}

	/**
	 * Call the callSmoothZoom which either zooms in or out depending on the value of the WheelRotation.
	 * @param e
	 */
	@Override
	public void mouseWheelMoved(MouseWheelEvent e)
	{
		callSmoothZoom(e.getX(), e.getY(), e.getWheelRotation());
	}

	protected void callRepaint()
	{
		drawMapComponent.repaint();
		this.repaint();
	}

	@Override
	public void keyTyped(KeyEvent e)
	{

	}

	// if a key is pressed then set the field pressedKeyCode to that value.
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
