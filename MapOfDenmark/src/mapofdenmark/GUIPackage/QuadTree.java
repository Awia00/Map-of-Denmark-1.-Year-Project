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

  public QuadTree(List<Point2D> pointData, double x, double y, double length) {
    this.x = x;
    this.y = y;
    this.length = length;
    this.points = pointData;
    if (pointData.size() > 10) {
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

  public QuadTree[] getQuadTrees() {
    if (NW == null && NE == null && SW == null && SE == null) return new QuadTree[0];
    else return new QuadTree[]{NW, NE, SW, SE};
  }


  public List<Point2D> getPoints() {
    if (this.points.isEmpty()) {
      return Collections.emptyList();
    } else return this.points;
  } 

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
  
  public void listTrees(QuadTree tree) {
    for (QuadTree t : tree) {
      System.out.println(t);  
      for (Point2D p : t.getPoints()) {
        System.out.println(p.getX() + ", " + p.getY());
      }
      t.listTrees(t);
    }
  }
  
  protected static Random random = new Random();
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
      points.add(new Point2D.Double(QuadTree.randomInRange(0.0, 50.0), QuadTree.randomInRange(0.0, 50.0)));
    }
    
    QuadTree q = new QuadTree(points, 0, 0, 50);

    q.listTrees(q);
  }
}

