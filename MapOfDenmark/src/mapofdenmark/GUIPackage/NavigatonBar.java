/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mapofdenmark.GUIPackage;

import database.Edge;
import database.Node;
import database.pathfinding.WeightedMapGraph;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author Christian
 */
public class NavigatonBar extends JPanel {

	private PlaceholderTextField from, to, searchAddress;
	private JLabel rutevejledning;
	private Container roadInfo;
	private JTextField roadNameField, velocityField, roadTypeField;
	private Button visVej;
	private JButton printRoute, findRoute, findAddress;
	private JLabel closestRoad;
	private Edge closestRoadEdge;

	private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

	private Node fromNode = null;
	private Node toNode = null;

	private JTextArea directions;
	private List<String> directionKeys;

	private JFrame routeFrame;
	private JScrollPane routeScroll;

	private WeightedMapGraph wGraph;

	MapComponent mapComponent;

	public NavigatonBar(MapComponent mc)
	{
		mapComponent = mc;

		rutevejledning = new AAJLabel("Rutevejledning ");
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
		from.setPlaceholder("Fra");
		from.setColumns(20);
		from.setFont(FontLoader.getFontWithSize("Roboto-Bold", 14f));
		from.setForeground(Color.decode("#4A4A4A"));

		to = new PlaceholderTextField("");
		to.setPlaceholder("Til");
		to.setColumns(20);
		to.setFont(FontLoader.getFontWithSize("Roboto-Bold", 14f));
		to.setForeground(Color.decode("#4A4A4A"));

		roadNameField = new JTextField("roadname");
		roadNameField.setEditable(false);
		roadNameField.setBorder(null);
		velocityField = new JTextField("velocity");
		velocityField.setEditable(false);
		velocityField.setBorder(null);
		roadTypeField = new JTextField("roadtype");
		roadTypeField.setEditable(false);
		roadTypeField.setBorder(null);

		roadInfo = new JPanel();
		roadInfo.setLayout(new BoxLayout(roadInfo, BoxLayout.Y_AXIS));
		roadInfo.setBackground(Color.white);
		roadInfo.add(roadNameField);
		roadInfo.add(velocityField);
		roadInfo.add(roadTypeField);

//        visVej = new Button("button");
		//visVej.setText("Vis");
		findRoute = new JButton("Find Route");
		findRoute.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e)
			{
				findRoute();
				didFindRoute();
			}
		});

		printRoute.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e)
			{
				createRouteDirectionFrame();
			}
		});

		findAddress = new JButton("Find the address");

		closestRoad = new AAJLabel("");
		closestRoad.setFont(FontLoader.getFontWithSize("Roboto-Bold", 12f));
		closestRoad.setForeground(Color.decode("#9B9B9B"));

		directions = new JTextArea(50, 20);
		directions.setEditable(false);
		directions.setMinimumSize(new Dimension(200, 600));

		setLayout(new MigLayout("", "[center]", "[][][]50[][][]200[grow]"));

		add(rutevejledning, "cell 0 0, align left");
		add(searchAddress, "cell 0 1");
		add(findAddress, "cell 0 2, align right");

		add(from, "cell 0 3");
		add(to, "cell 0 4");
		add(printRoute, " cell 0 5, align right");

		add(roadInfo, "cell 0 6, dock south");

		//add(visVej, "cell 0 3, align right");
		//add(closestRoad, "cell 0 4, align left");
		//add(findRoute, "cell 0 3");
		wGraph = GUIController.getGraph();
	}

	private void createRouteDirectionFrame()
	{
		if (wGraph.hasRoute(toNode))
		{
			if (routeFrame != null)
			{
				routeFrame.dispose();
				routeFrame = null;
			}
			routeScroll = new JScrollPane(directions);
			routeFrame = new JFrame("Directions"); // evt fra bla til bla
			//routeFrame.add(directions);
			routeFrame.setPreferredSize(new Dimension(300, 800));
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
		velocityField.setText("" + String.format("%.0f", closestRoadEdge.getLength() / closestRoadEdge.getWeight()) + " km/t");
		roadTypeField.setText("roadType: " + closestRoadEdge.getRoadType());
	}

	@Override
	public Dimension getPreferredSize()
	{
		return new Dimension(400, 800);
	}

	public static void main(String[] args)
	{
		JFrame frame = new JFrame();
		frame.add(new NavigatonBar(null));
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

	public void findRoute()
	{
		//g.shortestPath(fromNode.getID(), toNode.getID());
		getParent().repaint();
	}

	public void setFromNode(Node node)
	{
		directions.setText("");

		fromNode = node;
		mapComponent.setFrom(true);
		mapComponent.setFromNode(fromNode);
		if (toNode != null)
		{
			wGraph.runDij(fromNode, toNode);
			if (wGraph.hasRoute(toNode))
			{
				mapComponent.setRouteNodes(wGraph.calculateRoute(toNode));
				printRoute.setEnabled(true);
				displayDirections();
			}
		}

		// wGraph.hasRoute(toNode) tjek med denne også
	}

	public void setToNode(Node node)
	{
		directions.setText("");

		toNode = node;
		mapComponent.setTo(true);
		mapComponent.setToNode(toNode);
//                mapComponent.setTo((int) fromNode.getyCoord(), (int)toNode.getyCoord());
		if (fromNode != null)
		{
			wGraph.runDij(fromNode, toNode);
			if (wGraph.hasRoute(toNode))
			{
				mapComponent.setRouteNodes(wGraph.calculateRoute(toNode));
				printRoute.setEnabled(true);
				displayDirections();
			}
		}

		// wGraph.hasRoute(toNode) tjek med denne også
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
					directions.append("Tag " + currentRoad + "\n\n-------------------------------\n");
					currentRoad = edge.getRoadName();
					continue;
				}
				directions.append("Kør ad " + currentRoad + "\n\t\t" + String.format("%.2f", currentLength + (edge.getLength() / 1000)) + " km \n-------------------------------\n");
				currentRoad = edge.getRoadName();
				currentLength = 0;
			} else
			{
				currentLength += edge.getLength() / 1000;
			}
			total += edge.getLength() / 1000;
		}
		if (directions.getText().equals("") || currentLength != 0)
		{
			directions.append("Kør ad " + currentRoad + "\n\t\t" + String.format("%.2f", currentLength) + " km \n-------------------------------\n");
		}
		directions.append("\nTotal distance\t\t" + String.format("%.2f", total) + " km \n");
	}

	//Kør ad Fra-/tilkørsel
	public void didFindRoute()
	{
		mapComponent.didFindRoute();
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
