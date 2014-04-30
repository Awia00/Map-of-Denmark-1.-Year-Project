/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package database.pathfinding;

import database.Database;
import database.DatabaseInterface;
import database.Edge;
import database.Node;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Christian
 */
public class MapGraph {
    
    EdgeWeightedDigraph graph;
    Map<Integer, Edge> edges;
    static List<Integer> id;
    
    public MapGraph(List<Edge> edges) {
        this.edges = new HashMap<>();
        graph = new EdgeWeightedDigraph(edges.size());
        int id = 0;
        for (Edge e : edges ) {
            double weight = e.getWeight();
            int from = (int) e.getFromNodeTrue().getID();
            int to = (int) e.getToNodeTrue().getID();
            if (from > 0 && to > 0) {
                graph.addEdge(new DirectedEdge(from, to, weight, id));
                graph.addEdge(new DirectedEdge(to, from, weight, id));
                this.edges.put(id, e);
            }
        }
        
    }
    
    public EdgeWeightedDigraph graph() {
        return graph;
    }
    
    public double shortestPath(int from, int to) {
        DijkstraSP sp = new DijkstraSP(this.graph, from);
        if (sp.hasPathTo(to)) this.markNodesInPath(sp.pathTo(to));
        return sp.distTo(to);
    }
    
    private void markNodesInPath(Iterable<DirectedEdge> e) {
        for (DirectedEdge diEdge : e) {
            Edge edge = edges.get(diEdge.ID());
            edge.setInShortestPath(true);
            System.out.println(edge.isInShortestPath());
        }
    }
    
    public static void main(String[] args) {
        
        DatabaseInterface db = Database.db(true);
        List<Edge> edges = db.getEdgeList();
        MapGraph mg = new MapGraph(edges);
        
        EdgeWeightedDigraph g = mg.graph();
        DijkstraSP sp = new DijkstraSP(g, 615295);
        mg.shortestPath(615295, 615291);
        double dist = sp.distTo(615291);
//        for (DirectedEdge de : sp.pathTo(615291)) {
//            System.out.println(de);
//        }
        System.out.println(dist);
        
        
        System.out.println(g.E());
        
    }
}
