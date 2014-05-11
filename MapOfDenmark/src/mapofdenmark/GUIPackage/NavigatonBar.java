/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mapofdenmark.GUIPackage;

import database.Node;
import database.pathfinding.WeightedMapGraph;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author Christian
 */
public class NavigatonBar extends JPanel {

	private PlaceholderTextField from;
	private PlaceholderTextField to;
	private JLabel rutevejledning;
	private Button visVej;
	private JButton findRoute;
	private JLabel closestRoad;
	private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

	private Node fromNode = null;
	private Node toNode = null;
        
        private JTextArea directions;
        private List<String> directionKeys;

	private WeightedMapGraph wGraph;

	MapComponent mapComponent;

	public NavigatonBar(MapComponent mc)
	{
		mapComponent = mc;
                

		rutevejledning = new AAJLabel("Rutevejledning ");
		rutevejledning.setFont(FontLoader.getFontWithSize("Roboto-Bold", 15f));
		rutevejledning.setForeground(Color.decode("#9B9B9B"));

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

		closestRoad = new AAJLabel("");
		closestRoad.setFont(FontLoader.getFontWithSize("Roboto-Bold", 12f));
		closestRoad.setForeground(Color.decode("#9B9B9B"));
                
                directions = new JTextArea(50, 20);
                directions.setEditable(false);
                directions.setMinimumSize(new Dimension(200, 600));

		setLayout(new MigLayout("", "[center]", "[][][][]50[][]"));

		add(rutevejledning, "cell 0 0, align left");
		add(from, "cell 0 1");
		add(to, "cell 0 2");
//        add(visVej, "cell 0 3, align right");
		add(closestRoad, "cell 0 4, align left");
		add(findRoute, "cell 0 3");
                add(directions, " cell 0 5");

		wGraph = GUIController.getGraph();
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
		if (toNode != null)
		{
                        
			wGraph.runDij(fromNode, toNode);
			mapComponent.setRouteNodes(wGraph.calculateRoute(toNode));
                        displayDirections();
		}
	}

	public void setToNode(Node node)
	{
                directions.setText("");
                
		toNode = node;
		if (fromNode != null)
		{
                    
			wGraph.runDij(fromNode, toNode);
			mapComponent.setRouteNodes(wGraph.calculateRoute(toNode));
                        displayDirections();
		}

		//mapComponent.setRoute(wGraph.drawablePath2D(node));
	}
        
        private void displayDirections() {
            HashMap<String, Double> directionsMap = wGraph.getDirections(toNode);
                        directionKeys = new ArrayList<>(wGraph.getDirectionKeys());
                        Collections.reverse(directionKeys);
                        double total = 0;
                        for (String s : directionKeys) {
                            double length = directionsMap.get(s) / 1000;
                            directions.append(s + " " + String.format("%.2f", length) + " km \n");
                            total += length;
                            
                        }
                        directions.append("Total distance " + String.format("%.2f", total) + " km \n");
        }

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
