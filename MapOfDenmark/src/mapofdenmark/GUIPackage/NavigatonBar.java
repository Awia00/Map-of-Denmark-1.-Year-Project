/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mapofdenmark.GUIPackage;

import database.Node;
import database.pathfinding.DijkstraSP;
import database.pathfinding.EdgeWeightedDigraph;
import database.pathfinding.MapGraph;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
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
    
    MapComponent mapComponent;
    
    public NavigatonBar(MapComponent mc) {
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
            public void actionPerformed(ActionEvent e) {
                findRoute();
                didFindRoute();
            }
        });
        
        
        
        closestRoad = new AAJLabel("");
        closestRoad.setFont(FontLoader.getFontWithSize("Roboto-Bold", 12f));
        closestRoad.setForeground(Color.decode("#9B9B9B"));
        
        setLayout(new MigLayout("", "[center]","[][][][]50[]"));
        
        add(rutevejledning, "cell 0 0, align left");
        add(from, "cell 0 1");
        add(to, "cell 0 2");
//        add(visVej, "cell 0 3, align right");
        add(closestRoad, "cell 0 4, align left");
        add(findRoute, "cell 0 3");
    }
    
    
    @Override
  public Dimension getPreferredSize() {
    return new Dimension(300, 800);
  }
  
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.add(new NavigatonBar(null));
        frame.pack();
        frame.setVisible(true);
    }

    public JLabel getClosestRoad() {
        return closestRoad;
    }
    
    public PlaceholderTextField getFrom() {
        return from;
    }
    
    public PlaceholderTextField getTo() {
        return to;
    }
   
    public void findRoute() {
        MapGraph g = GUIController.graph();
        g.shortestPath((int)fromNode.getID(), (int)toNode.getID());
        
		mapComponent.setRoute(g.getRoute());
        getParent().repaint();
    }
    
    public void setFromNode(Node node) {
        fromNode = node;
    }
    
    public void setToNode(Node node) {
        toNode = node;
    }
    
    public void didFindRoute() {
        mapComponent.didFindRoute();
    }
}
