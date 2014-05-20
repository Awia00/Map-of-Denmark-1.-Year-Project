/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mapofdenmark.GUIPackage;

import AddressParser.AddressFinder;
import database.Edge;
import database.Node;
import database.RoadTypeEnum;
import database.pathfinding.WeightedMapGraph;
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author Christian
 */
public class NavigatonBar extends JPanel {

	private PlaceholderTextField from, to, searchAddress;
	private JLabel rutevejledning;
	private Container roadInfo;
	private JLabel roadNameField, velocityField, roadTypeField;
	private Button visVej;
	private JButton printRoute, findRoute, findAddress, reset;
	private JLabel closestRoad;
	private Edge closestRoadEdge;

	private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

	private Node fromNode = null;
	private Node toNode = null;

	private JTextArea directions;
	private List<String> directionKeys;

	private JFrame routeFrame;
	private JScrollPane routeScroll;

	private JRadioButton shortestRoute;
	private JRadioButton fastestRoute;
	private boolean isFastestRoute = true;

	private WeightedMapGraph wGraph;
	private AddressFinder addressFinder;

	private MapComponent mapComponent;

	public NavigatonBar(MapComponent mc, final AddressFinder addressFinder)
	{
		mapComponent = mc;
		this.addressFinder = addressFinder;

		rutevejledning = new AAJLabel("Get Directions ");
		rutevejledning.setFont(FontLoader.getFontWithSize("Roboto-Bold", 15f));
		rutevejledning.setForeground(Color.decode("#9B9B9B"));
		printRoute = new JButton("Print directions");
		printRoute.setEnabled(false);

		searchAddress = new PlaceholderTextField("");
		searchAddress.setPlaceholder("Find Address...");
		searchAddress.setColumns(20);
		searchAddress.setFont(FontLoader.getFontWithSize("Roboto-Bold", 14f));
		searchAddress.setForeground(Color.decode("#4A4A4A"));

		from = new PlaceholderTextField("");
		from.setPlaceholder("From");
		from.setColumns(20);
		from.setFont(FontLoader.getFontWithSize("Roboto-Bold", 14f));
		from.setForeground(Color.decode("#4A4A4A"));

		to = new PlaceholderTextField("");
		to.setPlaceholder("To");
		to.setColumns(20);
		to.setFont(FontLoader.getFontWithSize("Roboto-Bold", 14f));
		to.setForeground(Color.decode("#4A4A4A"));

		roadNameField = new AAJLabel("");
//        roadNameFiled.setEditable(false);
//        roadNameField.setBorder(null);
		velocityField = new AAJLabel("");
//        velocityField.setEditable(false);
//        velocityField.setBorder(null);
		roadTypeField = new AAJLabel("");
//        roadTypeField.setEditable(false);
//        roadTypeField.setBorder(null);

		roadInfo = new JPanel();
		roadInfo.setLayout(new BoxLayout(roadInfo, BoxLayout.Y_AXIS));
		roadInfo.add(roadNameField);
		roadInfo.add(velocityField);
		roadInfo.add(roadTypeField);

//        visVej = new Button("button");
		//visVej.setText("Vis");
//        findRoute = new JButton("Find Route");
		printRoute.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e)
			{
				createRouteDirectionFrame();
			}
		});

		findAddress = new JButton("Search");
		findAddress.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e)
			{
				Edge edge = addressFinder.searchForEdge(searchAddress.getText());
				if (edge != null && !edge.getRoadName().equals(""))
				{
					mapComponent.moveVisibleAreaToCoord(edge.getMidX(), edge.getMidY());
					mapComponent.setSearchedRoad(edge.getRoadName());
					searchAddress.setText(edge.getRoadName());
					mapComponent.repaint();
				} else
				{
					mapComponent.setSearchedRoad("no road");
					searchAddress.setText(null);
					searchAddress.setPlaceholder("Find Address...");
					mapComponent.repaint();
				}
				// pick the edge the addressParser find's midNode.

			}
		});
		searchAddress.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e)
			{
				Edge edge = addressFinder.searchForEdge(searchAddress.getText());
				if (edge != null && !edge.getRoadName().equals(""))
				{
					mapComponent.moveVisibleAreaToCoord(edge.getMidX(), edge.getMidY());
					mapComponent.setSearchedRoad(edge.getRoadName());
					searchAddress.setText(edge.getRoadName());
					mapComponent.repaint();
				} else
				{
					mapComponent.setSearchedRoad("no road");
					searchAddress.setText(null);
					searchAddress.setPlaceholder("Find Address...");
					mapComponent.repaint();
				}
			}
		});

		closestRoad = new AAJLabel("");
		closestRoad.setFont(FontLoader.getFontWithSize("Roboto-Bold", 12f));
		closestRoad.setForeground(Color.decode("#9B9B9B"));

		directions = new JTextArea(50, 20);
		directions.setEditable(false);
		directions.setMinimumSize(new Dimension(200, 600));

		reset = new JButton("Reset");
		reset.setEnabled(false);
		reset.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e)
			{
				printRoute.setEnabled(false);
				fromNode = null;
				toNode = null;
				mapComponent.setTo(false);
				mapComponent.setToNode(null);
				mapComponent.setFrom(false);
				mapComponent.setFromNode(null);
				mapComponent.setRouteNodes(null);
				from.setText("");
				to.setText("");
				reset.setEnabled(false);
				mapComponent.repaint();
			}
		});

		fastestRoute = new JRadioButton("Fastest Route");
		fastestRoute.setSelected(true);
		fastestRoute.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e)
			{
				isFastestRoute = true;
				if (fromNode == null && toNode == null)
				{
					Edge edgeFrom = addressFinder.searchForEdge(from.getText());
					if (edgeFrom != null)
					{
						fromNode = edgeFrom.getFromNode();
					}
					edgeFrom = addressFinder.searchForEdge(to.getText());
					if (edgeFrom != null)
					{
						toNode = edgeFrom.getFromNode();
					}
				}

				runRouteCalculation();
			}
		});

		shortestRoute = new JRadioButton("Shortest Route");
		shortestRoute.setSelected(false);
		shortestRoute.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e)
			{
				isFastestRoute = false;

				if (fromNode == null && toNode == null)
				{
					Edge edgeFrom = addressFinder.searchForEdge(from.getText());
					if (edgeFrom != null)
					{
						fromNode = edgeFrom.getFromNode();
					}
					edgeFrom = addressFinder.searchForEdge(to.getText());
					if (edgeFrom != null)
					{
						toNode = edgeFrom.getFromNode();
					}
				}

				runRouteCalculation();
			}
		});

		ButtonGroup bg = new ButtonGroup();
		bg.add(fastestRoute);
		bg.add(shortestRoute);

		setLayout(new MigLayout("", "[center]", "[][][]50[][][][][]200[grow]"));

		add(rutevejledning, "cell 0 0, align left");
		add(searchAddress, "cell 0 1");
		add(findAddress, "cell 0 2, align right");

		add(from, "cell 0 3");
		add(to, "cell 0 4");
		add(reset, "cell 0 5");
		add(printRoute, " cell 0 5, align right");

		add(fastestRoute, "cell 0 6, align left");
		add(shortestRoute, "cell 0 7, align left");

		add(roadInfo, "cell 0 8, dock south");

		//add(visVej, "cell 0 3, align right");
		//add(closestRoad, "cell 0 4, align left");
		//add(findRoute, "cell 0 3");
		wGraph = GUIController.getGraph();

		from.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e)
			{
				Edge edgeFrom = addressFinder.searchForEdge(from.getText());
				if (edgeFrom != null)
				{
					fromNode = edgeFrom.getFromNode();
					from.setText(edgeFrom.getRoadName());
					mapComponent.setFrom(true);
					mapComponent.setFromNode(fromNode);
					mapComponent.repaint();
					reset.setEnabled(true);
					runRouteCalculation();
				} else
				{
					from.setText(null);
					from.setPlaceholder("Street not found");
					fromNode = null;
					mapComponent.setFrom(false);
					mapComponent.setFromNode(null);
					printRoute.setEnabled(false);
					mapComponent.setRouteNodes(null);
					mapComponent.repaint();
				}
			}
		});

		from.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e)
			{
				if(!from.getText().equals(""))checkFromNode();
				if(!to.getText().equals(""))checkToNode();
			}
		});

		to.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e)
			{
				if(!to.getText().equals(""))checkToNode();
				if(!from.getText().equals(""))checkFromNode();
			}
		});
	}

	private void checkToNode()
	{
		Edge edgeFrom = addressFinder.searchForEdge(to.getText());
		if (edgeFrom != null)
		{
			toNode = edgeFrom.getFromNode();
			to.setText(edgeFrom.getRoadName());
			mapComponent.setTo(true);
			mapComponent.setToNode(toNode);
			mapComponent.repaint();
			reset.setEnabled(true);
			runRouteCalculation();
		} else
		{
			to.setText(null);
			to.setPlaceholder("Street not found");
			toNode = null;
			mapComponent.setTo(false);
			mapComponent.setToNode(null);
			printRoute.setEnabled(false);
			mapComponent.setRouteNodes(null);
		}
		mapComponent.repaint();
	}

	private void checkFromNode()
	{
		Edge edgeFrom = addressFinder.searchForEdge(from.getText());
		if (edgeFrom != null)
		{
			fromNode = edgeFrom.getFromNode();
			from.setText(edgeFrom.getRoadName());
			mapComponent.setFrom(true);
			mapComponent.setFromNode(fromNode);
			mapComponent.repaint();
			reset.setEnabled(true);
			runRouteCalculation();
		} else
		{
			from.setText(null);
			from.setPlaceholder("Street not found");
			fromNode = null;
			mapComponent.setFrom(false);
			mapComponent.setFromNode(null);
			printRoute.setEnabled(false);
			mapComponent.setRouteNodes(null);
		}
		mapComponent.repaint();
	}

	// LAV KODE FOR FROM OG TO.
	private void createRouteDirectionFrame()
	{
		if (wGraph.hasRoute(toNode))
		{
			if (routeFrame != null)
			{
				routeFrame.dispose();
				routeFrame = null;
				routeScroll = null;
			}
			routeScroll = new JScrollPane(directions);
			routeFrame = new JFrame("Directions"); // evt fra bla til bla
			//routeFrame.add(directions);
			routeFrame.setPreferredSize(new Dimension(getWidth()*2, getHeight()));
			routeFrame.add(routeScroll);

			routeFrame.pack();
			routeFrame.repaint();
			routeFrame.setVisible(true);
		}
	}

	public void setClosestRoadEdge(Edge closestRoadEdge)
	{
		this.closestRoadEdge = closestRoadEdge;
		roadNameField.setText(closestRoadEdge.getRoadName());
		velocityField.setText("" + String.format("%.0f", closestRoadEdge.getLength() / closestRoadEdge.getWeight()) + " km/h");
		roadTypeField.setText(RoadTypeEnum.getRoadTypeName(closestRoadEdge.getRoadType()) + "  ");
	}

	@Override
	public Dimension getPreferredSize()
	{
		return new Dimension(400, 800);
	}

	public static void main(String[] args)
	{
		JFrame frame = new JFrame();
		frame.add(new NavigatonBar(null, null));
		frame.pack();
		frame.setVisible(true);
	}

	public JLabel getClosestRoad()
	{
		return closestRoad;
	}

	public PlaceholderTextField getFrom()
	{
		return from;
	}

	public PlaceholderTextField getTo()
	{
		return to;
	}

	public void setFromNode(Node node)
	{
		directions.setText("");

		fromNode = node;
		mapComponent.setFrom(true);
		mapComponent.setFromNode(fromNode);
		reset.setEnabled(true);
		runRouteCalculation();

		// wGraph.hasRoute(toNode) tjek med denne også
	}

	public void setToNode(Node node)
	{
		directions.setText("");

		toNode = node;
		mapComponent.setTo(true);
		mapComponent.setToNode(toNode);
		reset.setEnabled(true);
		runRouteCalculation();
		// wGraph.hasRoute(toNode) tjek med denne også
	}
	
	private void runRouteCalculation()
	{
		if (fromNode != null && toNode != null)
		{
			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			wGraph.runDij(fromNode, toNode, isFastestRoute);
			if (wGraph.hasRoute(toNode))
			{
				mapComponent.setRouteNodes(wGraph.calculateRoute(toNode));
				printRoute.setEnabled(true);
				displayDirections();
			}
			setCursor(Cursor.getDefaultCursor());
		}
	}

	private void displayDirections()
	{

		//HashMap<String, Double> directionsMap = wGraph.getDirections(toNode);
		List<Edge> routeEdges = wGraph.getDirections(toNode);
		Collections.reverse(routeEdges); //needs reversing because the roads are apprently loaded in opposite direction (relative to to/from)
		directions.setText("");

		String currentRoad = "";
		double currentLength = 0;
		double total = 0;
		double drivetime = 0;
		for (Edge edge : routeEdges)
		{

			if (currentRoad.equals(""))
			{
				currentRoad = edge.getRoadName();
			}
			if (!currentRoad.equals(edge.getRoadName()))
			{
				if (currentRoad.contains("Fra-/tilkørsel"))
				{
					directions.append("Take " + currentRoad + "\n\n-------------------------------\n");
					currentRoad = edge.getRoadName();
					continue;
				}
				directions.append("Drive along " + currentRoad + "\n\t\t" + String.format("%.2f", currentLength + (edge.getLength())) + " km \n-------------------------------\n");
				currentRoad = edge.getRoadName();
				currentLength = 0;
			} else
			{
				currentLength += edge.getLength();
			}
			drivetime += edge.getWeight();
			total += edge.getLength();
		}
		if (directions.getText().equals("") || currentLength != 0)
		{
			directions.append("Drive along " + currentRoad + "\n\t\t" + String.format("%.2f", currentLength) + " km \n-------------------------------\n");
		}
		directions.append("\nTotal distance\t\t" + String.format("%.2f", total) + " km \n");
		drivetime =  drivetime*60+3+Math.sqrt(drivetime*60);
		if(drivetime < 60)
		directions.append("\nTotal drive time\t" + String.format("%.0f",drivetime) + " minutes\n");
		else
		{
			directions.append("\nTotal drive time\t" + String.format("%.2f", drivetime/60) + " hours\n");
		}
	}

}


/*
 HashSet<Integer> set = new HashSet<>();
 for(Node node2 : wGraph.calculateRoute(node))
 {
 set.add(node.getID());
 }
 mapComponent.setRoute(set);
 */
