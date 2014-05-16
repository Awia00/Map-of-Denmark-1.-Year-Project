/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mapofdenmark.GUIPackage;

import database.Edge;
import database.Node;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JComponent;
import org.nocrala.tools.gis.data.esri.shapefile.shape.PointData;
import org.nocrala.tools.gis.data.esri.shapefile.shape.shapes.PolygonShape;

/**
 * Class description:
 *
 * @version 0.1 - changed 27-02-2014
 * @authorNewVersion Anders Wind - awis@itu.dk
 *
 * @buildDate 27-02-2014
 * @author Anders Wind - awis@itu.dk
 */
public class MapComponent extends JComponent {

    // the toplevel quadtree
    private QuadTree quadTreeToDraw;
    protected VisibleArea visibleArea;
    private List<PolygonShape> landShapePolygons;
    private List<PolygonShape> landUseShapePolygons;

    private List<Node> routeNodes;

    private int xStartCoord, yStartCoord, xEndCoord, yEndCoord; // for drawing drag N drop zoom
    private boolean drawRectangle = false;

    private int xFrom, yFrom, xTo, yTo;

    // zoom constants to explain how much is zoomed in each time the zoom function is called.
    protected final double zoomInConstant = 0.98;
    protected final double zoomOutConstant = 1.02;

    private boolean toSet;
    private boolean fromSet;

    BufferedImage toIcon = null;
    BufferedImage fromIcon = null;

    Node fromNode;
    Node toNode;

    int moveIconX, moveIconY;

    private ColorScheme activeColorScheme;

    private double xVArea;
    private double xlength;
    private double componentWidth;
    private double yVArea;
    private double ylength;
    private double componentHeight;

    /**
     * The constructor of the mapComponent.
     *
     * @param streets is not used at the moment
     * @param edges an array with all the edges in the database.
     */
    public MapComponent(QuadTree quadTree, List<PolygonShape> landShapePolygons, List<PolygonShape> landUsePolygons) {
        initialize(quadTree, landShapePolygons, landUsePolygons);
    }

    /**
     * initializes the quadtree and sets values for visible area.
     *
     * @param streets not used at the moment.
     * @param edges an array with all the edges in the database.
     */
    private void initialize(QuadTree quadTree, List<PolygonShape> landShapePolygons, List<PolygonShape> landUsePolygons) {
        quadTreeToDraw = quadTree;
        visibleArea = new VisibleArea();
        this.landShapePolygons = landShapePolygons;
        this.landUseShapePolygons = landUsePolygons;

        try {
            toIcon = ImageIO.read(new File("assets/to.png"));
            fromIcon = ImageIO.read(new File("assets/from.png"));

        } catch (IOException ex) {
            Logger.getLogger(MapComponent.class.getName()).log(Level.SEVERE, null, ex);
        }

        visibleArea.setCoord(quadTreeToDraw.getQuadTreeX() - quadTreeToDraw.getQuadTreeLength() / 8, quadTreeToDraw.getQuadTreeY() - quadTreeToDraw.getQuadTreeLength() / 50, quadTreeToDraw.getQuadTreeLength() / 15 * 16, quadTreeToDraw.getQuadTreeLength() / 15 * 10);
        moveIconX = 0;
        moveIconY = 0;
        // set the initial Color scheme to Standard Color scheme
        this.setColorScheme("Standard");
    }

    public void setTo(boolean to) {
        toSet = to;
    }

    public void setFrom(boolean from) {
        fromSet = from;
    }

    public void setFromNode(Node fromNode) {
        this.fromNode = fromNode;
    }

    public void setToNode(Node toNode) {
        this.toNode = toNode;
    }

    public void setRouteNodes(List<Node> routeNodes) {
        this.routeNodes = routeNodes;
    }

    public QuadTree quadTree() {
        return quadTreeToDraw;
    }

    public void setFrom(int x, int y) {
        this.xFrom = x;
        this.yFrom = y;
    }

    public void setTo(int x, int y) {
        this.xTo = x;
        this.yTo = y;
    }

    /**
     * Change the position of the visible area, such that the user can see this
     * at the next repaint.
     *
     * @param xCoord if this is negative - move map to the right
     * @param yCoord if this is negative - move map to the down
     */
    public void moveVisibleArea(double xCoord, double yCoord) {
        double xMapCoord = xCoord / getWidth() * visibleArea.getxLength() * 1.2;
        double yMapCoord = yCoord / getHeight() * visibleArea.getyLength() * 1.2;
        visibleArea.setCoord(visibleArea.getxCoord() + xMapCoord, visibleArea.getyCoord() + yMapCoord, visibleArea.getxLength(), visibleArea.getyLength());
//                System.out.println(xMapCoord + " " + yMapCoord);

    }

    /**
     * The dragNDropZoom method takes in 4 coordinates which describes a
     * rectangle. This is the area which the user wants to zoom in on. To begin
     * with it checks the values so it does not matter from which corner the
     * user has dragged and dropped. Afterwards it finds the largest length in
     * the drawn rectangle and then uses this to indicate how much is zoomed in.
     * Lastly it uses the values to update the visibleArea object.
     *
     * @param xStartCoord
     * @param yStartCoord
     * @param xEndCoord
     * @param yEndCoord
     */
    public void dragNDropZoom(double xStartCoord, double yStartCoord, double xEndCoord, double yEndCoord) {
        double mapXStartCoord;
        double mapYStartCoord;
        double mapXEndCoord;
        double mapYEndCoord;

        if (xStartCoord < xEndCoord) {
            mapXStartCoord = convertMouseXToMap(xStartCoord);
            mapXEndCoord = convertMouseXToMap(xEndCoord);
        } else {
            mapXStartCoord = convertMouseXToMap(xEndCoord);
            mapXEndCoord = convertMouseXToMap(xStartCoord);
        }
        if (yStartCoord > yEndCoord) {
            mapYStartCoord = convertMouseYToMap(yStartCoord);
            mapYEndCoord = convertMouseYToMap(yEndCoord);
        } else {
            mapYStartCoord = convertMouseYToMap(yEndCoord);
            mapYEndCoord = convertMouseYToMap(yStartCoord);
        }

        double zoomconstant;
        if (mapXEndCoord - mapXStartCoord > mapYEndCoord - mapYStartCoord) {
            zoomconstant = (mapXEndCoord - mapXStartCoord) / visibleArea.getxLength();
        } else {
            zoomconstant = (mapYEndCoord - mapYStartCoord) / visibleArea.getyLength();
        }
        int fixRounds = 0;
        while ((visibleArea.getxLength() * zoomconstant) < (quadTreeToDraw.getQuadTreeLength() / 100000) && fixRounds != 20) {
            zoomconstant += 0.02;
            fixRounds++;
        }
        if (fixRounds >= 20) {
            return;
        }
        System.out.println("zoomconstant: " + zoomconstant);
        visibleArea.setCoord(mapXStartCoord, mapYStartCoord, visibleArea.getxLength() * zoomconstant, visibleArea.getyLength() * zoomconstant);
    }

    /**
     * This method is used to set the values so the repaint will either draw or
     * not draw a rectangle showing the user how his drag and drop zoom looks.
     * It checks the values so that it does not matter which corner the user has
     * dragged from.
     *
     * @param xStartCoord
     * @param yStartCoord
     * @param xEndCoord
     * @param yEndCoord
     * @param drawRectangle tells if the rectangle should be drawn or not.
     */
    public void drawRectangle(int xStartCoord, int yStartCoord, int xEndCoord, int yEndCoord, boolean drawRectangle) {
        this.drawRectangle = drawRectangle;
        if (xStartCoord < xEndCoord) {
            this.xStartCoord = xStartCoord;
            this.xEndCoord = xEndCoord;
        } else {
            this.xStartCoord = xEndCoord;
            this.xEndCoord = xStartCoord;
        }
        if (yStartCoord < yEndCoord) {
            this.yStartCoord = yStartCoord;
            this.yEndCoord = yEndCoord;
        } else {
            this.yStartCoord = yEndCoord;
            this.yEndCoord = yStartCoord;
        }
    }

    /**
     * Converts the mouse x coordinate to map coordinates so it can be used to
     * set values in the visible area object.
     *
     * @param xCoord the coordinate to convert
     * @return the converted coordinate
     */
    public double convertMouseXToMap(double xCoord) {
        return visibleArea.getxCoord() + xCoord / getWidth() * visibleArea.getxLength();
    }

    /**
     * Converts the mouse y coordinate to map coordinates so it can be used to
     * set values in the visible area object. The y coordinate needs to be
     * handled a bit differently since a swing component has its y values
     * starting with 0 in the top and getWidth() in the bottom, but the map data
     * has the low values in the bottom and goes higher when you go up.
     *
     * @param yCoord the coordinate to convert
     * @return the converted coordinate
     */
    public double convertMouseYToMap(double yCoord) {
        return visibleArea.getyCoord() + (getHeight() - yCoord) / getHeight() * visibleArea.getyLength();
    }

    /**
     * The zoomOut method takes the mouse coordinates and uses them to find out
     * percentage wise how much the visibleArea's start coordinate- and how much
     * the length needs to change. It does this for each axis. It uses the
     * zoomOutConstant to find out how much overall the area needs to grow.
     *
     * @param mouseXCoord
     * @param mouseYCoord
     */
    public void zoomOut(double mouseXCoord, double mouseYCoord) {
        if (visibleArea.getyLength() + quadTreeToDraw.getQuadTreeLength() / 10 >= quadTreeToDraw.getQuadTreeLength()) {
            return;
        }
        double mouseMapXCoord = convertMouseXToMap(mouseXCoord);
        double mouseMapYCoord = convertMouseYToMap(mouseYCoord);
        double mouseLengthX = mouseMapXCoord - visibleArea.getxCoord();
        double mouseLengthY = mouseMapYCoord - visibleArea.getyCoord();

        double xPct = mouseLengthX / visibleArea.getxLength();
        double yPct = mouseLengthY / visibleArea.getyLength();

        double xZoomLength = visibleArea.getxLength() * zoomOutConstant;
        double yZoomLength = visibleArea.getyLength() * zoomOutConstant;

        double deltaXLength = visibleArea.getxLength() - xZoomLength;
        double deltaYLength = visibleArea.getyLength() - yZoomLength;

        visibleArea.setCoord(visibleArea.getxCoord() + deltaXLength * xPct, visibleArea.getyCoord() + deltaYLength * yPct, xZoomLength, yZoomLength);
    }

    /**
     * The zoomIn method takes the mouse coordinates and uses them to find out
     * percentage wise how much the visibleArea's start coordinate- and how much
     * the length needs to change. It does this for each axis. It uses the
     * zoomInConstant to find out how much overall the area needs to shrink.
     *
     * @param mouseXCoord
     * @param mouseYCoord
     */
    public void zoomIn(double mouseXCoord, double mouseYCoord) {
        if (visibleArea.getyLength() <= (quadTreeToDraw.getQuadTreeLength() / 100000)) {
            return;
        }
        final double mouseMapXCoord = convertMouseXToMap(mouseXCoord);
        final double mouseMapYCoord = convertMouseYToMap(mouseYCoord);
        final double mouseLengthX = mouseMapXCoord - visibleArea.getxCoord();
        final double mouseLengthY = mouseMapYCoord - visibleArea.getyCoord();

        final double xPct = mouseLengthX / visibleArea.getxLength();
        final double yPct = mouseLengthY / visibleArea.getyLength();

        final double xZoomLength = visibleArea.getxLength() * zoomInConstant;
        final double yZoomLength = visibleArea.getyLength() * zoomInConstant;

        final double deltaXLength = visibleArea.getxLength() - xZoomLength;
        final double deltaYLength = visibleArea.getyLength() - yZoomLength;

        final double newStartX = visibleArea.getxCoord() + deltaXLength * xPct;
        final double newStartY = visibleArea.getyCoord() + deltaYLength * yPct;

        visibleArea.setCoord(newStartX, newStartY, xZoomLength, yZoomLength);
    }

    /**
     * The findClosestRoad method checks which quadtree the user has its cursor
     * in and then goes through all the edges in that quadtree to find out which
     * edge is the closest.
     *
     * @param mouseCoordX
     * @param mouseCoordY
     * @return a string with that edge's roadName. Could be changed to return
     * that edge or the street it is in.
     */
    public Edge findClosestRoad(int mouseCoordX, int mouseCoordY) {
        //QuadTree quadTreeToSearch;
        double xCoord = convertMouseXToMap(mouseCoordX);
        double yCoord = convertMouseYToMap(mouseCoordY);
        List<Edge> edges = new ArrayList<>();
        for (QuadTree quadTree : QuadTree.getBottomTrees()) {
            if ((quadTree.getQuadTreeX() + quadTree.getQuadTreeLength() >= xCoord && quadTree.getQuadTreeY() + quadTree.getQuadTreeLength() >= yCoord)) {
                if ((quadTree.getQuadTreeX() <= xCoord && quadTree.getQuadTreeY() <= yCoord)) {
                    edges.addAll(quadTree.getEdges());
                }
            }
        }
        double distance = -1;
        Edge closestEdge = null;
        for (Edge edge : edges) {
            double tempDistance = calculateDistanceEdgeToPoint(edge, xCoord, yCoord);
            if (distance == -1 && !edge.getRoadName().trim().equals("")) {
                distance = tempDistance;
                closestEdge = edge;
            }
            if (tempDistance < distance && !edge.getRoadName().trim().equals("")) {
                distance = tempDistance;
                closestEdge = edge;
            }
        }
        if (distance != -1) {
            return closestEdge;
        }
        return null;
    }

    /**
     * Calculates the distance from the Edge edge to the point (xCoord, yCoord).
     *
     * @param edge the edge to find the distance to.
     * @param xCoord the first coordinate of the point
     * @param yCoord the second coordinate of the point.
     * @return a double describing the distance.s
     */
    private double calculateDistanceEdgeToPoint(Edge edge, double xCoord, double yCoord) {
        Line2D line = new Line2D.Double(edge.getFromNode().getxCoord(), edge.getFromNode().getyCoord(), edge.getToNode().getxCoord(), edge.getToNode().getyCoord());
        Point2D point = new Point2D.Double(xCoord, yCoord);
        return line.ptSegDist(point);
    }

    @Override
    public void paint(Graphics g) {

        // draw the map white and with a border
        Graphics2D g2 = (Graphics2D) g;
        if (landShapePolygons.isEmpty()) {
            g.setColor(this.activeColorScheme.getBackgroundColor());
        } else {
            g.setColor(new Color(181, 207, 241));
        }
        g.fillRect(0, 0, getSize().width - 1, getSize().height - 1);

        ArrayList<QuadTree> bottomTrees = QuadTree.getBottomTrees();
        xlength = visibleArea.getxLength();
        ylength = visibleArea.getyLength();

        xVArea = visibleArea.getxCoord();
        yVArea = visibleArea.getyCoord();

        componentHeight = getHeight();
        componentWidth = getWidth();

        double zoomFactorStroke = Math.sqrt(((quadTreeToDraw.getQuadTreeLength()) / (xlength * 3)));

        // create strokes using the zoomFacotrStroke.
        //BasicStroke highWayStrokeBorder = new BasicStroke((float) (Math.max(3.5, (zoomFactorStroke * 1.2) + 1.5)), BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND);
        BasicStroke highWayStroke = new BasicStroke((float) (Math.max(2, (zoomFactorStroke * 1.2))), BasicStroke.CAP_SQUARE, BasicStroke.JOIN_ROUND);

        BasicStroke secondaryRoadStrokeBorder = new BasicStroke((float) (Math.max(2.5, (zoomFactorStroke * 0.9) + 1.5)), BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND);
        BasicStroke secondaryRoadStroke = new BasicStroke((float) (Math.max(1.3, (zoomFactorStroke * 0.9))), BasicStroke.CAP_SQUARE, BasicStroke.JOIN_ROUND);

        //BasicStroke normalRoadStrokeBorder = new BasicStroke((float) (Math.max(2.1, (zoomFactorStroke * 0.6) + 1.5)), BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND);
        BasicStroke normalRoadStroke = new BasicStroke((float) (Math.max(1, (zoomFactorStroke * 0.6))), BasicStroke.CAP_SQUARE, BasicStroke.JOIN_ROUND);

        //BasicStroke smallRoadStrokeBorder = new BasicStroke((float) (Math.max(1.8, (zoomFactorStroke * 0.3) + 1.5)), BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND);
        BasicStroke smallRoadStroke = new BasicStroke((float) (Math.max(1, (zoomFactorStroke * 0.3))), BasicStroke.CAP_SQUARE, BasicStroke.JOIN_ROUND);

        //BasicStroke pathRoadStrokeBorder = new BasicStroke((float) (Math.max(1.7, (zoomFactorStroke * 0.1) + 1.5)), BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND);
        BasicStroke pathRoadStroke = new BasicStroke((float) (Math.max(1, (zoomFactorStroke * 0.1))), BasicStroke.CAP_SQUARE, BasicStroke.JOIN_ROUND);

        BasicStroke routeStroke = new BasicStroke((float) (Math.max(4, (zoomFactorStroke * 2.05))), BasicStroke.CAP_SQUARE, BasicStroke.JOIN_ROUND);
        BasicStroke routeBorderStroke = new BasicStroke((float) (Math.max(5, (zoomFactorStroke * 2.3))), BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND);

        // sets the rendering hints so that it uses ANTI-ALIASING to render the edges.
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_SPEED);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);

        g.setColor(new Color(227, 221, 218));

        for (PolygonShape poly : landShapePolygons) {
            PointData[] points = poly.getPoints();
            if (points[0].getX() * 100000 < xVArea - quadTreeToDraw.getQuadTreeLength() / 8 || points[0].getY() * 150000 < yVArea - quadTreeToDraw.getQuadTreeLength() / 8) {
                continue;
            }
            if (points[0].getX() * 100000 > xVArea + xlength + quadTreeToDraw.getQuadTreeLength() / 8 || points[0].getX() * 150000 > yVArea + ylength + quadTreeToDraw.getQuadTreeLength() / 8) {
                continue;
            }
            int[] xPoints = new int[poly.getNumberOfPoints()];
            int[] yPoints = new int[poly.getNumberOfPoints()];
            int i = 0;
            for (PointData point : points) {
                xPoints[i] = (int) ((((point.getX() * 100000) - xVArea) / xlength) * componentWidth);
                yPoints[i] = (int) (componentHeight - (((point.getY() * 150000) - yVArea) / ylength) * componentHeight);
                i++;
            }
            g2.fillPolygon(xPoints, yPoints, Math.min(xPoints.length, yPoints.length));
        }
        if (xlength <= (quadTreeToDraw.getQuadTreeLength() / 5)) {
            g.setColor(new Color(137, 198, 103));
            for (PolygonShape poly : landUseShapePolygons) {
                PointData[] points = poly.getPoints();
                if (points[0].getX() * 100000 < xVArea - quadTreeToDraw.getQuadTreeLength() / 100 || points[0].getY() * 150000 < yVArea - quadTreeToDraw.getQuadTreeLength() / 100) {
                    continue;
                }
                if (points[0].getX() * 100000 > xVArea + xlength + quadTreeToDraw.getQuadTreeLength() / 100 || points[0].getX() * 150000 > yVArea + ylength + quadTreeToDraw.getQuadTreeLength() / 100) {
                    continue;
                }
                int[] xPoints = new int[poly.getNumberOfPoints()];
                int[] yPoints = new int[poly.getNumberOfPoints()];
                int i = 0;
                for (PointData point : points) {
                    xPoints[i] = (int) ((((point.getX() * 100000) - xVArea) / xlength) * componentWidth);
                    yPoints[i] = (int) (componentHeight - (((point.getY() * 150000) - yVArea) / ylength) * componentHeight);
                    i++;
                }
                g2.fillPolygon(xPoints, yPoints, Math.min(xPoints.length, yPoints.length));
            }
        }
        for (QuadTree quadTree : bottomTrees) {
            // checks that they should be drawn, this is set when the visibleArea is updated.
            if (quadTree.isDrawable()) {
                for (Edge edge : quadTree.getCoastLineEdges()) {
                    double x1 = edge.getFromNode().getxCoord();
                    double y1 = edge.getFromNode().getyCoord();
                    double x2 = edge.getToNode().getxCoord();
                    double y2 = edge.getToNode().getyCoord();

                    // drawing the coastline
                    g.setColor(new Color(255 - 50, 239 - 50, 213 - 50));
                    g2.setStroke(new BasicStroke(1.4f));
                    g.drawLine((int) (((x1 - xVArea) / xlength) * componentWidth), (int) (componentHeight - ((y1 - yVArea) / ylength) * componentHeight), (int) (((x2 - xVArea) / xlength) * componentWidth), (int) (componentHeight - ((y2 - yVArea) / ylength) * componentHeight));

                }
                if (xlength <= (quadTreeToDraw.getQuadTreeLength() / 60)) {
                    for (Edge edge : quadTree.getPathEdges()) {
                        double x1 = edge.getFromNode().getxCoord();
                        double y1 = edge.getFromNode().getyCoord();
                        double x2 = edge.getToNode().getxCoord();
                        double y2 = edge.getToNode().getyCoord();
						// drawing the border
                        //g.setColor(activeColorScheme.getPathwayBorderColor());
                        //g2.setStroke(pathRoadStrokeBorder);
                        //g.drawLine((int) (((x1 - xVArea) / xlength) * componentWidth), (int) (componentHeight - ((y1 - yVArea) / ylength) * componentHeight), (int) (((x2 - xVArea) / xlength) * componentWidth), (int) (componentHeight - ((y2 - yVArea) / ylength) * componentHeight));

                        // drawing the road
                        g.setColor(activeColorScheme.getPathwayColor());
                        g2.setStroke(pathRoadStroke);
                        g.drawLine((int) (((x1 - xVArea) / xlength) * componentWidth), (int) (componentHeight - ((y1 - yVArea) / ylength) * componentHeight), (int) (((x2 - xVArea) / xlength) * componentWidth), (int) (componentHeight - ((y2 - yVArea) / ylength) * componentHeight));
                    }
                }
                for (Edge edge : quadTree.getFerryEdges()) {
                    g2.setStroke(new BasicStroke(2.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 0, new float[]{
                        9
                    }, 0));
                    g.setColor(this.activeColorScheme.getFerrywayColor());

                    double x1 = edge.getFromNode().getxCoord();
                    double y1 = edge.getFromNode().getyCoord();
                    double x2 = edge.getToNode().getxCoord();
                    double y2 = edge.getToNode().getyCoord();
                    g.drawLine((int) (((x1 - xVArea) / xlength) * componentWidth), (int) (componentHeight - ((y1 - yVArea) / ylength) * componentHeight), (int) (((x2 - xVArea) / xlength) * componentWidth), (int) (componentHeight - ((y2 - yVArea) / ylength) * componentHeight));
                }
                if (xlength <= (quadTreeToDraw.getQuadTreeLength() / 20)) {
                    for (Edge edge : quadTree.getSmallEdges()) {
                        double x1 = edge.getFromNode().getxCoord();
                        double y1 = edge.getFromNode().getyCoord();
                        double x2 = edge.getToNode().getxCoord();
                        double y2 = edge.getToNode().getyCoord();
						// drawing the border
                        //g.setColor(activeColorScheme.getSmallRoadBorderColor());
                        //g2.setStroke(smallRoadStrokeBorder);
                        //g.drawLine((int) (((x1 - xVArea) / xlength) * componentWidth), (int) (componentHeight - ((y1 - yVArea) / ylength) * componentHeight), (int) (((x2 - xVArea) / xlength) * componentWidth), (int) (componentHeight - ((y2 - yVArea) / ylength) * componentHeight));

                        // drawing the road
                        g.setColor(activeColorScheme.getSmallRoadColor());
                        g2.setStroke(smallRoadStroke);

                        g.drawLine((int) (((x1 - xVArea) / xlength) * componentWidth), (int) (componentHeight - ((y1 - yVArea) / ylength) * componentHeight), (int) (((x2 - xVArea) / xlength) * componentWidth), (int) (componentHeight - ((y2 - yVArea) / ylength) * componentHeight));
                    }
                }
                if (xlength <= (quadTreeToDraw.getQuadTreeLength() / 5)) {
                    for (Edge edge : quadTree.getNormalEdges()) {
                        double x1 = edge.getFromNode().getxCoord();
                        double y1 = edge.getFromNode().getyCoord();
                        double x2 = edge.getToNode().getxCoord();
                        double y2 = edge.getToNode().getyCoord();

                        // drawing the border
                        //g.setColor(activeColorScheme.getNormalRoadBorderColor());
                        //g2.setStroke(normalRoadStrokeBorder);
                        //g.drawLine((int) (((x1 - xVArea) / xlength) * componentWidth), (int) (componentHeight - ((y1 - yVArea) / ylength) * componentHeight), (int) (((x2 - xVArea) / xlength) * componentWidth), (int) (componentHeight - ((y2 - yVArea) / ylength) * componentHeight));
                        // drawing the road
                        g.setColor(activeColorScheme.getNormalRoadColor());

                        g2.setStroke(normalRoadStroke);
                        g.drawLine((int) (((x1 - xVArea) / xlength) * componentWidth), (int) (componentHeight - ((y1 - yVArea) / ylength) * componentHeight), (int) (((x2 - xVArea) / xlength) * componentWidth), (int) (componentHeight - ((y2 - yVArea) / ylength) * componentHeight));
                        
                        
                        drawRotatedString(g2, edge);
                        
                    }
                }
                if (xlength <= (quadTreeToDraw.getQuadTreeLength() / 2)) {
//                    int drawString = 0;
                    for (Edge edge : quadTree.getSecondaryEdges()) {
                        double x1 = edge.getFromNode().getxCoord();
                        double y1 = edge.getFromNode().getyCoord();
                        double x2 = edge.getToNode().getxCoord();
                        double y2 = edge.getToNode().getyCoord();
                        // drawing the border
                        g.setColor(activeColorScheme.getSecondaryRoadBorderColor());
                        g2.setStroke(secondaryRoadStrokeBorder);
                        g.drawLine((int) (((x1 - xVArea) / xlength) * componentWidth), (int) (componentHeight - ((y1 - yVArea) / ylength) * componentHeight), (int) (((x2 - xVArea) / xlength) * componentWidth), (int) (componentHeight - ((y2 - yVArea) / ylength) * componentHeight));

                        // drawing the road
                        g.setColor(activeColorScheme.getSecondaryRoadColor());

                        g2.setStroke(secondaryRoadStroke);
                        g.drawLine((int) (((x1 - xVArea) / xlength) * componentWidth), (int) (componentHeight - ((y1 - yVArea) / ylength) * componentHeight), (int) (((x2 - xVArea) / xlength) * componentWidth), (int) (componentHeight - ((y2 - yVArea) / ylength) * componentHeight));
//                        if (drawString % 2 == 0 && xlength <= (quadTreeToDraw.getQuadTreeLength() / 35) && !(edge.getRoadName().contains("Fra"))) {
//                           drawString(g, edge, (int) (((edge.getMidX() - xVArea) / xlength) * componentWidth), (int) (componentHeight - ((edge.getMidY() - yVArea) / ylength) * componentHeight));
//                        }
//                        drawString++;
                    }
                }
                for (Edge edge : quadTree.getHighwayEdges()) {
                    double x1 = edge.getFromNode().getxCoord();
                    double y1 = edge.getFromNode().getyCoord();
                    double x2 = edge.getToNode().getxCoord();
                    double y2 = edge.getToNode().getyCoord();

                    // drawing the border
                    //g.setColor(activeColorScheme.getHighwayBorderColor());
                    //g2.setStroke(highWayStrokeBorder);
                    //g.drawLine((int) (((x1 - xVArea) / xlength) * componentWidth), (int) (componentHeight - ((y1 - yVArea) / ylength) * componentHeight), (int) (((x2 - xVArea) / xlength) * componentWidth), (int) (componentHeight - ((y2 - yVArea) / ylength) * componentHeight));
                    // drawing the road
                    g.setColor(activeColorScheme.getHighwayColor());

                    g2.setStroke(highWayStroke);
                    g.drawLine((int) (((x1 - xVArea) / xlength) * componentWidth), (int) (componentHeight - ((y1 - yVArea) / ylength) * componentHeight), (int) (((x2 - xVArea) / xlength) * componentWidth), (int) (componentHeight - ((y2 - yVArea) / ylength) * componentHeight));

                }
                if (xlength <= (quadTreeToDraw.getQuadTreeLength() / 35)) {
                    for (Edge edge : quadTree.getPlaceNameEdges()) {
                        g.setColor(this.activeColorScheme.getPlaceNameColor());
                        g.setFont(new Font("Verdana", Font.BOLD, 14));
                        g.drawString(edge.getRoadName(), (int) (((edge.getMidX() - xVArea) / xlength) * componentWidth), (int) (componentHeight - ((edge.getMidY() - yVArea) / ylength) * componentHeight));
                    }
                }
            }
        }

        if (routeNodes != null) {
            int jumpOver = 1;
            if (xlength >= (quadTreeToDraw.getQuadTreeLength() / 15)) {
                jumpOver = 2;
            }
            if (xlength >= (quadTreeToDraw.getQuadTreeLength() / 7)) {
                jumpOver = 3;
            }
            if (xlength >= (quadTreeToDraw.getQuadTreeLength() / 5)) {
                jumpOver = 6;
            }
            if (xlength >= (quadTreeToDraw.getQuadTreeLength() / 2)) {
                jumpOver = 8;
            }
            if (xlength >= (quadTreeToDraw.getQuadTreeLength())) {
                jumpOver = 10;
            }

            Color colorBorder = new Color(37, 35, 83);
            Node fromNode = null;
            int i;
            for (i = 0; i < routeNodes.size(); i += jumpOver) {
                Node node = routeNodes.get(i);
                if (fromNode != null) {
                    g2.setStroke(routeBorderStroke);
                    g.setColor(colorBorder);
                    g.drawLine((int) (((fromNode.getxCoord() - xVArea) / xlength) * componentWidth), (int) (componentHeight - ((fromNode.getyCoord() - yVArea) / ylength) * componentHeight), (int) (((node.getxCoord() - xVArea) / xlength) * componentWidth), (int) (componentHeight - ((node.getyCoord() - yVArea) / ylength) * componentHeight));
                    g2.setStroke(routeStroke);
                    g.setColor(Color.blue);
                    g.drawLine((int) (((fromNode.getxCoord() - xVArea) / xlength) * componentWidth), (int) (componentHeight - ((fromNode.getyCoord() - yVArea) / ylength) * componentHeight), (int) (((node.getxCoord() - xVArea) / xlength) * componentWidth), (int) (componentHeight - ((node.getyCoord() - yVArea) / ylength) * componentHeight));
                    fromNode = node;
                } else {
                    fromNode = node;
                }
            }
            if (i > routeNodes.size() - 1 || i < routeNodes.size() - 1) {
                Node node = routeNodes.get(routeNodes.size() - 1);
                g2.setStroke(routeBorderStroke);
                g.setColor(colorBorder);
                g.drawLine((int) (((fromNode.getxCoord() - xVArea) / xlength) * componentWidth), (int) (componentHeight - ((fromNode.getyCoord() - yVArea) / ylength) * componentHeight), (int) (((node.getxCoord() - xVArea) / xlength) * componentWidth), (int) (componentHeight - ((node.getyCoord() - yVArea) / ylength) * componentHeight));
                g2.setStroke(routeStroke);
                g.setColor(Color.blue);
                g.drawLine((int) (((fromNode.getxCoord() - xVArea) / xlength) * componentWidth), (int) (componentHeight - ((fromNode.getyCoord() - yVArea) / ylength) * componentHeight), (int) (((node.getxCoord() - xVArea) / xlength) * componentWidth), (int) (componentHeight - ((node.getyCoord() - yVArea) / ylength) * componentHeight));
            }
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        }
        int iconOffsetX = fromIcon.getWidth() / 2;
        int iconOffsetY = fromIcon.getHeight();
        // Draw a pin at destination
        if (toSet) {
            double toIconX = toNode.getxCoord();
            double toIconY = toNode.getyCoord();
            g2.drawImage(toIcon, (int) (((toIconX - xVArea) / xlength) * componentWidth) - iconOffsetX, (int) (componentHeight - ((toIconY - yVArea) / ylength) * componentHeight) - iconOffsetY, this);
//                                    toSet = false;
        }
        // Draw a pin at start
        if (fromSet) {
            double fromIconX = fromNode.getxCoord();
            double fromIconY = fromNode.getyCoord();
            g2.drawImage(fromIcon, (int) (((fromIconX - xVArea) / xlength) * componentWidth) - iconOffsetX, (int) (componentHeight - ((fromIconY - yVArea) / ylength) * componentHeight) - iconOffsetY, this);
//                                    fromSet = false;
        }

        g2.setStroke(new BasicStroke(1));
        g.setColor(Color.blue);

        g.drawRect(0, 0, getSize().width - 1, getSize().height - 1);

        // draw the "drag and drop" rectangle if the user is dragging and dropping it.
        if (drawRectangle) {
            //The color of the rectangle is set as the inverted color of the background color.

            g.setColor(new Color(255 - (this.activeColorScheme.getBackgroundColor().getRed()), 255 - (this.activeColorScheme.getBackgroundColor().getGreen()), 255 - (this.activeColorScheme.getBackgroundColor().getBlue())));

            g2.setStroke(new BasicStroke(2));
            g.drawRect(xStartCoord, yStartCoord, xEndCoord - xStartCoord, yEndCoord - yStartCoord);
            g2.setStroke(new BasicStroke());
        }

        // when drawing: take the coord, substract its value with the startCoord from visible area
        // then divide by the length. that way you get values from 0-1.
    }

//      private void drawPin(Graphics2D g2, Node atNode, BufferedImage icon) {
//        System.out.println("ho");
//        int iconOffsetX = icon.getWidth() / 2;
//        int iconOffsetY = icon.getHeight();
//        double nodeIconX = atNode.getxCoord();
//        double nodeIconY = atNode.getyCoord();
//        System.out.println(icon);
//        g2.drawImage(icon, (int) (((nodeIconX - xVArea) / xlength) * componentWidth) - iconOffsetX, (int) (componentHeight - ((nodeIconY - yVArea) / ylength) * componentHeight) - iconOffsetY, this);
//    }
//    public BufferedImage createStringImage(Graphics g, String s) {
//        int w = g.getFontMetrics().stringWidth(s) + 5;
//        int h = g.getFontMetrics().getHeight();
//
//        BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
//        Graphics2D imageGraphics = image.createGraphics();
//        imageGraphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
//        imageGraphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
//        imageGraphics.setColor(Color.BLACK);
//        imageGraphics.setFont(g.getFont());
//        imageGraphics.drawString(s, 0, h - g.getFontMetrics().getDescent());
//        imageGraphics.dispose();
//
//        return image;
//    }
//
//    private void drawString(Graphics g, Edge e, int tx, int ty) {
//        AffineTransform aff = AffineTransform.getRotateInstance(getAngle(e));
//        aff.translate(tx, ty);
//
//        Graphics2D g2D = ((Graphics2D) g);
//        g2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
//        g2D.drawImage(createStringImage(g, e.getRoadName()), aff, this);
//    }
//
    public double getAngle(Edge edge) {
        double x1 = edge.getFromNode().getxCoord();
        double y1 = edge.getFromNode().getyCoord();
        double x2 = edge.getToNode().getxCoord();
        double y2 = edge.getToNode().getyCoord();

        double xDiff = x2 - x1;
        double yDiff = y2 - y1;
        double angle = -Math.atan2(yDiff, xDiff);
        if (Math.toDegrees(angle) >= 180) {
            return angle + Math.PI;
        } else {
            return angle;
        }
    }

    public void drawRotatedString(Graphics2D g2, Edge edge) {
        g2.setStroke(new BasicStroke());
        g2.setColor(Color.black);
        if (xlength <= quadTreeToDraw.getQuadTreeLength() / 35) {
            String roadName = edge.getRoadName();

            AffineTransform orig = g2.getTransform();
            g2.rotate(getAngle(edge), (int) (((edge.getMidX() - xVArea) / xlength) * componentWidth), (int) (componentHeight - ((edge.getMidY() - yVArea) / ylength) * componentHeight));
            if (!roadName.contains("kørsel")) {
                g2.drawString(roadName, (int) (((edge.getMidX() - xVArea) / xlength) * componentWidth), (int) (componentHeight - ((edge.getMidY() - yVArea) / ylength) * componentHeight));
            }
            g2.setTransform(orig);
        }
    }

    /**
     * Set the activeColorScheme to one of the three modes.
     *
     * @param colorScheme a name of a color scheme that should be set.
     */
    protected void setColorScheme(String colorScheme) {
        switch (colorScheme) {
            case "Standard":
                //set Standard ColorScheme
                this.activeColorScheme = new ColorScheme("Standard", Color.white, Color.black, new Color(100, 100, 100), Color.orange, new Color(230, 140, 0), Color.gray, new Color(90, 90, 90), Color.gray, new Color(90, 90, 90), new Color(0, 230, 30), new Color(0, 170, 10), Color.blue, Color.black);
                break;
            case "Night":
                // set Night ColorScheme
                this.activeColorScheme = new ColorScheme("Night", Color.black, Color.orange, new Color(155, 100, 0), Color.gray, new Color(200, 200, 200), Color.cyan, new Color(30, 100, 75), Color.cyan, new Color(30, 100, 75), Color.magenta, new Color(100, 25, 65), Color.blue, Color.white);
                break;
            case "Colorblind":
                // set Colorblind ColorScheme
                this.activeColorScheme = new ColorScheme("Standard", Color.white, Color.black, new Color(100, 100, 100), Color.orange, new Color(230, 140, 0), new Color(82, 82, 82), new Color(50, 50, 50), new Color(82, 82, 82), new Color(50, 50, 50), new Color(190, 190, 190), new Color(150, 150, 150), Color.blue, Color.black);
                break;

            default:
                // default to Standard ColorScheme
                setColorScheme("Standard");
                break;
        }
        repaint();
    }
}
