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
  private QuadTree NW, NE, SW, SE;
  private List<Point2D> points;
  private final double x, y, length;
  private boolean isDrawable = false;
  static Set<QuadTree> quadTrees = new LinkedHashSet<>();
  static Random random = new Random(); // For testing purposes...

  public QuadTree(List<Point2D> pointData, double x, double y, double length) {
    this.x = x;
    this.y = y;
    this.length = length;
    this.points = pointData;
	double h = length/2;
    
    if (points.size() > 4) {
      List<Point2D> 
          NWPoints = new ArrayList<>(), 
          NEPoints = new ArrayList<>(),
          SWPoints = new ArrayList<>(),
          SEPoints = new ArrayList<>();
      for (Point2D point : points) {
		  double px = point.getX();
		  double py = point.getY();
        if (px >= x && px < x+h && py > y+h && py <= y+length) {
          NWPoints.add(point);
        }
        if (px > x+h && px <= x+length && py > y+h && py <= y+length) {
          NEPoints.add(point);
        }
        if (px >= x && px < x+h && py >= y && py < y+h) {
          SWPoints.add(point);
        }
        if (px >= x+h && px <= x+length && py >= y && py <= y+h) {
          SEPoints.add(point);
        } 
      } 
	  this.NW = new QuadTree(NWPoints, x, y+h,h);
        this.NE = new QuadTree(NEPoints, x+h,y+h,h);
        this.SW = new QuadTree(SWPoints, x, y, h);
        this.SE = new QuadTree(SEPoints, x+h,y, h);
        this.points = Collections.emptyList();
    } else {
       // this.points = pointData;
    }  
  } 

  public boolean isDrawable() {
    return this.isDrawable;
  }

  public void setDrawable(boolean isDrawable) {
    this.isDrawable = isDrawable;
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
    for (int i = 0; i < 35; i++) {
      points.add(new Point2D.Double(QuadTree.randomInRange(0, 199), QuadTree.randomInRange(0, 199)));
    }
    QuadTree q = new QuadTree(points, 0, 0, 200);

    //q.printTrees(q);
    Set<QuadTree> qt = new LinkedHashSet<>();
    qt = q.getChildQuadTrees(q); 
    System.out.println(qt.size());
    for (QuadTree t : qt) {
      System.out.println(t);
      System.out.println("Tree x: " + t.getQuadTreeX() + " y: " + t.getQuadTreeY() + " length: " + t.getQuadTreeLength());
      System.out.println("Tree has: " + t.getPoints().size() + " points");

      for (Point2D p : t.getPoints()) {
        System.out.println(p.getX() + ", " + p.getY());
      }
      System.out.println("\n####\n");
    }
  }
}



