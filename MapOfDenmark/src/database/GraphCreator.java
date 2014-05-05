/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Anders
 */
public class GraphCreator {

    private final HashMap<Node, LinkedList<Edge>> graph;
    private List<Edge> edges;
    private List<Node> nodes;
    private Set<Node> settledNodes;
    private Set<Node> unSettledNodes;
    private Map<Node, Node> predecessors;
    private Map<Node, Double> distance;

    public GraphCreator(List<Edge> edges, List<Node> nodes) {
        graph = new HashMap<>();
        this.nodes = new ArrayList<>(nodes);
        this.edges = new ArrayList<>(edges);
        
        createGraphStructure(edges.toArray(new Edge[edges.size()]), nodes.toArray(new Node[nodes.size()]));
    }

    private void createGraphStructure(Edge[] edges, Node[] nodes) {
        for (Edge edge : edges) {
            graph.put(edge.getFromNodeTrue(), new LinkedList<Edge>());
            graph.get(edge.getFromNodeTrue()).add(edge);
            graph.put(edge.getToNodeTrue(), new LinkedList<Edge>());
            graph.get(edge.getToNodeTrue()).add(edge);
            
            
        }
    }

    /**
     * Find the shortest route from a point x to another point y, using Dijkstra
     * algorithm Dijkstra uses the shortest path to find the next edge to
     * include.
     * 
     * 
     */
public LinkedList<Node> runDijkstra2(GraphCreator graph, Node source){
    
    for (Node node : nodes){
        
    }
    
    LinkedList results = new LinkedList<>();
    return results;
}
    
    
    
    public void runDijkstra(Node source) {
        settledNodes = new HashSet<>();
        unSettledNodes = new HashSet<>();
        distance = new HashMap<>();
        predecessors = new HashMap<>();
        distance.put(source, 0.0);
        unSettledNodes.add(source);
        while (unSettledNodes.size() > 0) {
            Node node = getMinimum(unSettledNodes);
            settledNodes.add(node);
            unSettledNodes.remove(node);
            findMinimalDistances(node);
        }
    }

    private void findMinimalDistances(Node node) {
        List<Node> adjacentNodes = getNeighbors(node);
        for (Node target : adjacentNodes) {
            if (getShortestDistance(target) > getShortestDistance(node)
                    + getDistance(node, target)) {
                distance.put(target, getShortestDistance(node)
                        + getDistance(node, target));
                predecessors.put(target, node);
                unSettledNodes.add(target);
            }
        }

    }

    private double getDistance(Node node, Node target) {
        for (Edge edge : edges) {
            if (edge.getFromNodeTrue().equals(node)
                    && edge.getToNodeTrue().equals(target)) {
                return edge.getWeight();
            }
        }
        throw new RuntimeException("Should not happen");
    }

    private List<Node> getNeighbors(Node node) {
        List<Node> neighbors = new ArrayList<>();
        for (Edge edge : edges) {
            if (edge.getFromNodeTrue().equals(node)
                    && !isSettled(edge.getToNodeTrue())) {
                neighbors.add(edge.getToNodeTrue());
            }
        }
        return neighbors;
    }

    private Node getMinimum(Set<Node> nodes) {
        Node minimum = null;
        for (Node node : nodes) {
            if (minimum == null) {
                minimum = node;
            } else {
                if (getShortestDistance(node) < getShortestDistance(minimum)) {
                    minimum = node;
                }
            }
        }
        return minimum;
    }

    private boolean isSettled(Node node) {
        return settledNodes.contains(node);
    }

    private double getShortestDistance(Node destination) {
        Double d = distance.get(destination);
        if (d == null) {
            return Double.MAX_VALUE;
        } else {
            return d;
        }
    }

    /*
     * This method returns the path from the source to the selected target and
     * NULL if no path exists
     */
    public LinkedList<Node> getPath(Node target) {
        LinkedList<Node> path = new LinkedList<>();
        Node step = target;
        // check if a path exists
        if (predecessors.get(step) == null) {
            return null;
        }
        path.add(step);
        while (predecessors.get(step) != null) {
            step = predecessors.get(step);
            path.add(step);
        }
        // Put it into the correct order
        Collections.reverse(path);
        return path;
    }

    /**
     * Find the shortest route from node1 to node2. BFS works like a queue,
     * taking the oldest edge and checking all its branches first.
     *
     * @param node1
     * @param node2
     */
    public double runBFS(Node start, Node end) {
        // Not correct implementation yet.
        HashMap<Node, Integer> distTo = new HashMap<>();
        ArrayDeque<Node> queue = new ArrayDeque<>();

        queue.add(start);
        distTo.put(start, 0);

        while (!queue.isEmpty()) {
            Node parent = (Node) queue.removeFirst();

            if (parent.equals(end)) {
                return distTo.get(parent);
            }

            for (Object edgeObject : graph.get(parent)) {
                Edge edge = (Edge) edgeObject;

                if (!distTo.containsKey(edge.getFromNodeTrue()) || !distTo.containsKey(edge.getToNodeTrue())) {
                    if (parent != edge.getFromNodeTrue()) {
                        queue.add(edge.getFromNodeTrue());
                        distTo.put(edge.getFromNodeTrue(), distTo.get(parent) + 1);
                    } else {
                        queue.add(edge.getToNodeTrue());
                        distTo.put(edge.getToNodeTrue(), distTo.get(parent) + 1);
                    }
                }
            }
        }

        return -1;
    }

    /**
     * Find the shortest route from node1 to node2. DFS works like a Stack,
     * taking the newest edge and checking all its branches first.
     *
     * @param fromNode
     * @param toNode
     */
    private void runDFS(Node fromNode, Node toNode) {
    }

    /**
     * Find the shortest route from a point x to another point y, using Prims
     * algorithm Prim uses the edge with the lowest weigth to include the next
     * edge.
     */
    private void runPrims() {
    }

    /**
     * Find the shortest route from one point to another.
     *
     * @param node1
     * @param node2
     */
    public void getShortestRoute(Node fromNode, Node toNode) {
    }

    public void printGraph() {
        for (Node node : graph.keySet()) {
            System.out.println(node.toString());
            for (Edge edge : graph.get(node)) {
                System.out.println(edge.toString());
            }
        }
    }
}
