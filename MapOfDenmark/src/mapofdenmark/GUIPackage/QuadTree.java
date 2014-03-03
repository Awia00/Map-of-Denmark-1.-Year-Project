/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mapofdenmark.GUIPackage;

/**
 *
 * @author Christian
 */
import java.util.*;
import java.awt.geom.Point2D;

public class QuadTree implements Iterable<QuadTree> {
  QuadTree NW, NE, SW, SE;
  List<Point2D> points;
  double x, y, length;
  static Set<QuadTree> quadTrees = new LinkedHashSet<>();
  static Random random = new Random(); // For testing purposes...

  public QuadTree(List<Point2D> pointData, double x, double y, double length) {
    this.x = x;
    this.y = y;
    this.length = length;
    this.points = pointData;
    if (pointData.size() > 5) {
      List<Point2D> 
          NWPoints = new ArrayList<Point2D>(), 
          NEPoints = new ArrayList<Point2D>(),
          SWPoints = new ArrayList<Point2D>(),
          SEPoints = new ArrayList<Point2D>();
      for (Point2D point : pointData) {
        
        if (point.getX() <= length/2 && point.getX() > x && point.getY() <= length/2 && point.getY() > y) {
          NWPoints.add(point);
        }
        if (point.getX() > length/2 && point.getX() < length && point.getY() <= length/2 && point.getY() > y) {
          NEPoints.add(point);
        }
        if (point.getX() <= length/2 && point.getX() > x && point.getY() > length/2 && point.getY() <= length) {
          SWPoints.add(point);
        }
        if (point.getX() > length/2 && point.getX() < length && point.getY() > length/2 && point.getY() <= length) {
          SEPoints.add(point);
        }
        this.NW = new QuadTree(NWPoints, x, y, length / 2);
        this.NE = new QuadTree(NEPoints, length / 2, y, length /2);
        this.SW = new QuadTree(SWPoints, x, length / 2, length / 2);
        this.SE = new QuadTree(SEPoints, length / 2, length / 2, length / 2);
      } 
    } 
  } 

  public double getQuadTreeX() {
    return this.x;
  }

  public double getQuadTreeY() {
    return this.y;
  }

  public double getQuadTreeLength() {
    return this.length;
  }
  public QuadTree[] getQuadTrees() {
    if (NW == null && NE == null && SW == null && SE == null) return new QuadTree[0];
    else return new QuadTree[]{NW, NE, SW, SE};
  }


  public List<Point2D> getPoints() {
    if (this.points.isEmpty()) {
      return Collections.emptyList();
    } else return this.points;
  } 
  
  // Iterator iterates through trees within current trees if any
  private class TreeIterator implements Iterator<QuadTree> {
    QuadTree[] trees = getQuadTrees();
    private int i = 0;

    public boolean hasNext() { return i < trees.length; }
    public void remove() { throw new UnsupportedOperationException(); }    
    public QuadTree next() {
      return trees[i++];
    }
  }

  public Iterator<QuadTree> iterator() {
    return new TreeIterator();
  }
  
  // Print all trees and their associated points - for testing purposes
  public void printTrees(QuadTree tree) {
    // Include parent tree
   // System.out.println(tree);  
   // for (Point2D p : tree.getPoints()) {
     //   System.out.println(p.getX() + ", " + p.getY());
      //}
    // Child trees
    for (QuadTree t : tree) {
      System.out.println(t);  
      //for (Point2D p : t.getPoints()) {
        //System.out.println(p.getX() + ", " + p.getY());
      //}
      t.printTrees(t);
    }
  }

  public Set<QuadTree> getChildQuadTrees(QuadTree tree) {
    this.quadTrees.add(tree);
    for (QuadTree t : tree) {
      //System.out.println("recursion");
      this.quadTrees.add(t);
      t.getChildQuadTrees(t);
    }
    return quadTrees;
  }
  
  // For testing purposes...
  public static double randomInRange(double min, double max) {
    double range = max - min;
    double scaled = random.nextDouble() * range;
    double shifted = scaled + min;
    return shifted; // == (rand.nextDouble() * (max-min)) + min;
  }


  // Class testing
  public static void main(String[] args) {
    List<Point2D> points = new ArrayList<>();
    for (int i = 0; i < 50; i++) {
      points.add(new Point2D.Double(QuadTree.randomInRange(10.0, 200.0), QuadTree.randomInRange(10.0, 200.0)));
    }
    
    QuadTree q = new QuadTree(points, 0, 0, 200);

    //q.printTrees(q);
    Set<QuadTree> qt = new LinkedHashSet<>();
    qt = q.getChildQuadTrees(q); 
    System.out.println(qt.size());
    for (QuadTree t : qt) {
      System.out.println(t);
      System.out.println("Tree x: " + t.getQuadTreeX() + " y: " + t.getQuadTreeY() + " length: " + t.getQuadTreeLength());
      for (Point2D p : t.getPoints()) {
       // System.out.println(p.getX() + ", " + p.getY());
      }
    }
  }
}
