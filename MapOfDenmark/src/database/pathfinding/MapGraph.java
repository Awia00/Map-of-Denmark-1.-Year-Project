/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package database.pathfinding;

import database.Database;
import database.DatabaseInterface;
import database.Edge;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Christian
 */
public class MapGraph {
    
    EdgeWeightedDigraph graph;
    static List<Integer> id;
    
    public MapGraph(List<Edge> edges) {
//        List<Edge> edges = tree.getEdges();
        id = new ArrayList<>();
        graph = new EdgeWeightedDigraph(edges.size());
        for (Edge e : edges ) {
            double weight = e.getWeight();
            int from = (int) e.getFromNodeTrue().getID();
            int to = (int) e.getToNodeTrue().getID();
            id.add(from);
            if (from > 0 && to > 0) graph.addEdge(new DirectedEdge(from, to, weight));
        }
        
    }
    
    public EdgeWeightedDigraph graph() {
        return graph;
    }
    
    public double shortestPath(int from, int to) {
        DijkstraSP sp = new DijkstraSP(this.graph, from);
        return sp.distTo(to);
    }
    
    public static void main(String[] args) {
        
        DatabaseInterface db = Database.db(true);
        List<Edge> edges = db.getEdgeList();
        MapGraph mg = new MapGraph(edges);
        
        EdgeWeightedDigraph g = mg.graph();
//        DijkstraSP sp = new DijkstraSP(g, 615295);
//        
//        double dist = sp.distTo(615295);
//        System.out.println(dist);
        Iterable<DirectedEdge> diEdges = g.edges();
        
        // prints adjacent edges for all vertices in the graph
        for (Integer i : id) {
            Bag<DirectedEdge> b = (Bag)g.adj(i);
            System.out.println("Vertex (Node) id " + i);
            for (DirectedEdge e : b) {
                System.out.println(e);               
            }
            System.out.println("");
        }
        
        
        System.out.println(g.E());
        
    }
}
