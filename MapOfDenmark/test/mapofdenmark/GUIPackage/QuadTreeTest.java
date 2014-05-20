/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mapofdenmark.GUIPackage;

import database.Edge;
import database.Node;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Aleksandar Jonovic
 */
public class QuadTreeTest {

    public QuadTreeTest() {
    }

    @BeforeClass
    public static void setUpClass() {    
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

     private static List<Point2D> createRandomPoints(int xMin, int yMin, int xMax, int yMax, int amount) {
        List<Point2D> list = new ArrayList<Point2D>();
        for (int i = 1; i <= amount; i++) {
            Double x = xMin + Math.random() * (xMax - xMin);
            Double y = yMin + Math.random() * (yMax - yMin);
            Point2D p = new Point2D.Double(x, y);
            list.add(p);
        }

        return list;
    }

    /**
     * BlackBox test of class QuadTree.
     */
    @Test
    public void blackBoxTestQuadTree() {

        /* BLACK BOX QUADTREE TEST.
         * 
         * We want to create a QuadTree with the following parameters:
         * Length: 400.
         * Number of QuadTrees: 16 (4x4).
         * Number of Points in each: 1 (but + 499 in the four corners).
         * Total number of points: 16 (with the corner padding; 2012).
         * 
         * Our implementation of QuadTree, splits the QuadTree after 500 points have been reached.
         * 
         * This gives us a total of 16 QuadTrees, with 12 of them having 1 point in them, and the rest having 500.
         * 
         *          
         * With this test, we want to make sure that the input will be parsed into an output which matches our expectations for the class and it's code.
         * This means that the test will try to uncover if the points (nodes/edges) are accepted by the QuadTree and distributed as expected.
         * 
         * |500|1  |1  |500|
         * |1  |1  |1  |1  |
         * |1  |1  |1  |1  |
         * |500|1  |1  |500|
         * 
         * The above is an ASCII rendering of the pattern of segmentation which we are aiming for.
         */
        
        //Clear the QuadTree
        QuadTree.clearQuadTree();
                
        //First create a list of edges and Point2D's, which to base our input on, and insert these into our QuadTree's constructor.
        List<Edge> edges = new ArrayList<>();
        List<Point2D> points = new ArrayList<>();
        
        //We set the length, and iterate over the 16 (4*4) QuadTree's which we expect to have as output. (add 1 point to them)
        int length = 400;
        for (int i = 0; i < 4; i++) {

            for (int j = 0; j < 4; j++) {
                int xMin = length / 4 * i;
                int yMin = length / 4 * j;
                int xMax = length / 4 * i + length / 4;
                int yMax = length / 4 * j + length / 4;
                points.addAll(createRandomPoints(xMin, yMin, xMax, yMax, 1));
            }
        }
        

        //Remember to fill each corner (of whole QuadTree) with 500 points, to ensure the QuadTree split (division).
        points.addAll(createRandomPoints(0, 0, 100, 100, 499));
        points.addAll(createRandomPoints(300, 0, 400, 100, 499));
        points.addAll(createRandomPoints(0, 300, 100, 400, 499));
        points.addAll(createRandomPoints(300, 300, 400, 400, 499));


        //System.out.println("Points Size: " + points.size());
        
        //Iterate over the points, and add them to the edges in preparation of starting up the QuadTree.
        for (Point2D point : points) {
            Node n1 = new Node(point);
            Node n2 = new Node(point);

            Edge e = new Edge(n1, n2, 1, "Test Edge ", 1, 1);
            e.setMidNode();
            edges.add(e);
        }
        //System.out.println("Edges size: " + edges.size());
        
        //Create the QuadTree
        QuadTree QT = new QuadTree(edges, 0, 0, 400);

        ArrayList<QuadTree> QTs = QT.getBottomTrees();
        
        //Collect all Bottom QuadTree's and extract the number of edges inside them. Add to tempResults for each.
        double tempResults = 0;
        for (QuadTree qt : QTs) {
            List<Edge> qtedges = qt.getHighwayEdges();
            for (Edge edge : qtedges) {
                tempResults++;
            }
        }
      
        //Test to see if there are indeed 2012 points inside the QuadTree(s).
        double expResult = 2012;
        double result = tempResults;
        assertEquals(expResult, result, 0);
        
        //Test to see if all 16 QuadTrees are created.
        double numQTExp = 16;
        double numQTResult = QTs.size();
        assertEquals(numQTExp, numQTResult, 0);
        
        //Test to see if the correct QuadTrees have the correct amount of points.
        //assertEquals(expResult, result, 0);
        
    }
    
    @Test
    public void whiteBoxTestQuadTree(){
        /* WHITE BOX QUADTREE TEST.
         * 
         * We want to create a QuadTree with the following parameters:
         * Length: 800.
         * Number of QuadTrees: 16 (See below for ASCII 'art').
         * Number of Points in each: (Check ASCII).
         * Total number of points: 2012.
         * 
         * Our implementation of QuadTree, splits the QuadTree after 500 points have been reached.
         * 
         * This gives us a total of 16 QuadTrees (Due to the way we've split the points).
         * 
         *          
         * We want to make sure that every step of the program text can be reached. We focus our attention to this, by trying to assert that they have been reached.
         * 
         */
        //Clear the QuadTree
        QuadTree.clearQuadTree();
        
        //First create a list of edges and Point2D's, which to base our input on, and insert these into our QuadTree's constructor.
        List<Edge> edges = new ArrayList<>();
        List<Point2D> points = new ArrayList<>();
        
        //Since we want a really specific division of our QuadTree, we must add points manually.
        // createRandomPoints(xMin, yMin, xMax, yMax, Amount) <- This is how we define the boundries for the creation of points.
        points.addAll(createRandomPoints(0, 0, 100, 100, 499));
        points.addAll(createRandomPoints(300, 0, 400, 100, 499));
        points.addAll(createRandomPoints(0, 300, 100, 400, 499));
        points.addAll(createRandomPoints(300, 300, 400, 400, 499));


        //System.out.println("Points Size: " + points.size());
        
        //Iterate over the points, and add them to the edges in preparation of starting up the QuadTree.
        for (Point2D point : points) {
            Node n1 = new Node(point);
            Node n2 = new Node(point);

            Edge e = new Edge(n1, n2, 1, "Test Edge ", 1, 1);
            e.setMidNode();
            edges.add(e);
        }
        //System.out.println("Edges size: " + edges.size());
        
        //Create the QuadTree
        QuadTree QT = new QuadTree(edges, 0, 0, 400);

        ArrayList<QuadTree> QTs = QT.getBottomTrees();
        
        //Collect all Bottom QuadTree's and extract the number of edges inside them. Add to tempResults for each.
        double tempResults = 0;
        List<Edge> resultEdges = new ArrayList<>();
        resultEdges.clear();
        for (QuadTree qt : QTs) {
            List<Edge> qtedges = qt.getHighwayEdges();
            for (Edge edge : qtedges) {
                resultEdges.add(edge);
                tempResults++;
            }
        }
      
        //Test to see if x = 0 for QuadTree.
        double expResult = 0;
        double result = QT.getQuadTreeX();
        assertEquals(expResult, result, 0);
        
        //Test to see if y = 0 for QuadTree.
        expResult = 0;
        result = QT.getQuadTreeY();
        assertEquals(expResult, result, 0);
        
        //Test to see if length = 400 for QuadTree.
        expResult = 400;
        result = QT.getQuadTreeLength();
        assertEquals(expResult, result, 0);
        
        //Test to see if amount of edges = 2012 for QuadTree.
        expResult = 2012;
        result = resultEdges.size();
        assertEquals(expResult, result, 0);
        
        //Test to see if y = 0 for QuadTree.
        expResult = 0;
        result = QT.getQuadTreeY();
        assertEquals(expResult, result, 0);
        
        
        //Test to see if division works, we input 2012 points into the QuadTree. That should give a maximum of 16 QuadTrees, and a minimum of 
        //expResult = 400/2;
        //result = QT.get
                
       // assertEquals(expResult, result, 0);
        
        
    }
}