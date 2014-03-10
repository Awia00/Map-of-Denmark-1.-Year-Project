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

public class QTree {
  QTree NW, NE, SW, SE;
  Set<Point2D> rootPoints;
  double xOrigin, yOrigin, treeBounds;
  int treeLimit;
  boolean isDrawable = false;
  static Random random = new Random(); // For testing purposes...

  public QTree(Set<Point2D> pointData, double xOrigin, double yOrigin, double treeBounds, int treeLimit) {
    this.xOrigin = xOrigin;
    this.yOrigin = yOrigin;
    this.treeBounds = treeBounds;
    this.treeLimit = treeLimit;
    this.rootPoints = pointData;
      System.out.println(this.rootPoints.size());

    for (Point2D point : pointData) {
      insertPoint(point, this);
    }
  }

  public void insertPoint(Point2D newPoint, QTree insertionRoot) {

    if (this.rootPoints.size() > 5) {
      Set<Point2D> pointsToMigrate = new HashSet<>();
      pointsToMigrate.addAll(this.rootPoints);
      pointsToMigrate.add(newPoint);
      this.rootPoints = null;
        if (newPoint.getX() <= treeBounds/2 && newPoint.getX() > xOrigin && newPoint.getY() <= treeBounds/2 && newPoint.getY() > yOrigin) {
            System.out.println(NW);
          if (this.NW == null) NW = new QTree(pointsToMigrate, xOrigin, yOrigin, treeBounds / 2, this.treeLimit);
          //else NW.rootPoints.add(newPoint);
            System.out.println("NW initialised ");

        }
        if (newPoint.getX() > treeBounds/2 && newPoint.getX() < treeBounds && newPoint.getY() <= treeBounds/2 && newPoint.getY() > yOrigin) {
          if (this.NE == null) NE = new QTree(pointsToMigrate, treeBounds / 2, yOrigin, treeBounds / 2, this.treeLimit);
          //else NE.rootPoints.add(newPoint);
        }
        if (newPoint.getX() <= treeBounds/2 && newPoint.getX() > xOrigin && newPoint.getY() > treeBounds/2 && newPoint.getY() <= treeBounds) {
          if (this.SW == null) SW = new QTree(pointsToMigrate, xOrigin, treeBounds / 2, treeBounds / 2, this.treeLimit);
          //else SW.rootPoints.add(newPoint);
        }
        if (newPoint.getX() > treeBounds/2 && newPoint.getX() < treeBounds && newPoint.getY() > treeBounds/2 && newPoint.getY() <= treeBounds) {
          if (this.SE == null) SE = new QTree(pointsToMigrate, treeBounds / 2, treeBounds / 2, treeBounds / 2, this.treeLimit);
          //else SE.rootPoints.add(newPoint);
        }
    } else this.rootPoints.add(newPoint);
  }

  public Set<Point2D> getRootPoints() {
    return this.rootPoints;
  }

  public void setRootPoints(Set<Point2D> points) {
    this.rootPoints = points;
  }

  public boolean isDrawable() {
    return this.isDrawable;
  }

  public void setDrawable(boolean isDrawable) {
    this.isDrawable = isDrawable;
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
    Set<Point2D> points = new HashSet<>();
    for (int i = 0; i < 6; i++) {
      points.add(new Point2D.Double(QTree.randomInRange(10.0, 200.0), QTree.randomInRange(10.0, 200.0)));
    }
    
    QTree q = new QTree(points, 0, 0, 200, 5);
    
    //StdOut.println(rootPoints.size());
  }

}
