/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DataStructureTest;

import DataStructure.QuadTree;
import DataStructure.Edge;
import DataStructure.Node;
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
        for (int i = 0; i < amount; i++) {
            Double x = xMin + 1 + Math.random() * (xMax - xMin - 2);
            Double y = yMin + 1 + Math.random() * (yMax - yMin - 2);
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

    @Test()
    public void whiteBoxTestQuadTree() {
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
         * ________________________________
         * |_1_|_0_|       |       	  |
         * | 0 |500|  500  |       	  |
         * |-------|-------|      0   	  |
         * |   0   |   0   |       	  |
         * |-------|-------|--------------|
         * |  500  |   0   |   0  |  200  |
         * |-------|-------|------|-------|
         * |   0   |   1   |  250 |   60  |
         * |_______|_______|______|_______|
         * We want to make sure that every step of the program text can be reached. We focus our attention to this, by trying to assert that they have been reached.
         * 
         */
        //Clear the QuadTree
        QuadTree.clearQuadTree();

        //First create a list of edges and Point2D's, which to base our input on, and insert these into our QuadTree's constructor.
        List<Edge> edges = new ArrayList<>();
        List<Point2D> points = new ArrayList<>();
        points.clear();
        //Since we want a really specific division of our QuadTree, we must add points manually.
        // createRandomPoints(xMin, yMin, xMax, yMax, Amount) <- This is how we define the boundries for the creation of points.

        //QT#1, QT#2, QT#3 ... QT#16.
        //Notice that the test specifically calls an area and the amount '0'. This equals to putting 0 inside that area.
        points.addAll(createRandomPoints(0, 0, 200, 200, 0));
        points.addAll(createRandomPoints(200, 0, 400, 200, 1));
        points.addAll(createRandomPoints(0, 200, 200, 400, 500));
        points.addAll(createRandomPoints(200, 200, 400, 400, 0));
        points.addAll(createRandomPoints(400, 0, 600, 200, 250));
        points.addAll(createRandomPoints(600, 0, 600, 200, 60));
        points.addAll(createRandomPoints(400, 200, 600, 400, 0));
        points.addAll(createRandomPoints(600, 200, 800, 400, 200));
        points.addAll(createRandomPoints(0, 400, 200, 600, 0));
        points.addAll(createRandomPoints(200, 400, 400, 600, 0));
        points.addAll(createRandomPoints(0, 600, 100, 700, 0));
        points.addAll(createRandomPoints(100, 600, 200, 700, 500));
        points.addAll(createRandomPoints(0, 700, 100, 800, 1));
        points.addAll(createRandomPoints(100, 700, 200, 800, 0));
        points.addAll(createRandomPoints(200, 600, 400, 800, 500));
        points.addAll(createRandomPoints(400, 400, 800, 800, 0));
        System.out.println("Points Size: " + points.size());

        //Iterate over the points, and add them to the edges in preparation of starting up the QuadTree.
        for (Point2D point : points) {
            Node n1 = new Node(point);
            Node n2 = new Node(point);

            Edge e = new Edge(n1, n2, 1, "Test Edge ", 1, 1);
            e.setMidNode();
            edges.add(e);
        }
        System.out.println("Edges size: " + edges.size());

        //Create the QuadTree, remember length 800, for 800x800 length quadratic quadtree.
        QuadTree QT = new QuadTree(edges, 0, 0, 800);

        ArrayList<QuadTree> QTs = QT.getBottomTrees();

        //Collect all Bottom QuadTree's and extract the number of edges inside them. Add to tempResults for each.
        double tempResults = 0;
        List<Edge> resultEdges = new ArrayList<>();
        for (QuadTree qt : QTs) {
            //System.out.println("x : "+qt.getQuadTreeX()+ " y : "+qt.getQuadTreeY());
            List<Edge> qtedges = qt.getHighwayEdges();
            for (Edge edge : qtedges) {
                resultEdges.add(edge);
                tempResults++;
            }
        }
        /*
         * 
         * Assertion of test parameters.
         * 
         */
        //Test to see if x = 0 for QuadTree.
        double expResult = 0;
        double result = QT.getQuadTreeX();
        assertEquals(expResult, result, 0);

        //Test to see if y = 0 for QuadTree.
        expResult = 0;
        result = QT.getQuadTreeY();
        assertEquals(expResult, result, 0);

        //Test to see if length = 800 for QuadTree.
        expResult = 800;
        result = QT.getQuadTreeLength();
        assertEquals(expResult, result, 0);

        //Test to see if amount of edges = 2012 for QuadTree.
        expResult = 2012;
        result = resultEdges.size();
        assertEquals(expResult, result, 0);

        //Test to see if all 16 QuadTrees are created.
        double numQTExp = 16;
        double numQTResult = QTs.size();
        assertEquals(numQTExp, numQTResult, 0);
        /*
         * 
         * End of assertion of parameters
         * 
         */

        /*
         * 
         * Actual whitebox test of QuadTree constructor.
         * 
         */
        //Series of tests to check every single QuadTree expected to be created, it's number of points, and whether they respect the logical mathematic boundries, and logical placements.
        // Basically a test of /*1 + 2 + 3 + 4 + 5 */ (see QuadTree.java for markings).

        //QT#1
        expResult = 0;
        result = QT.getSW().getSW().getEdges().size();
        System.out.println("Result for QT 1 : " + result);
        assertEquals(expResult, result, 0);

        //QT#2
        expResult = 1;
        result = QT.getSW().getSE().getEdges().size();
        System.out.println("Result for QT 2 : " + result);
        assertEquals(expResult, result, 500);

        //QT#3
        expResult = 500;
        result = QT.getSW().getNW().getEdges().size();
        System.out.println("Result for QT 3 : " + result);
        assertEquals(expResult, result, 500);

        //QT#4
        expResult = 0;
        result = QT.getSW().getNE().getEdges().size();
        System.out.println("Result for QT 4 : " + result);
        assertEquals(expResult, result, 500);

        //QT#5
        expResult = 250;
        result = QT.getSE().getSW().getEdges().size();
        System.out.println("Result for QT 5 : " + result);
        assertEquals(expResult, result, 500);

        //QT#6
        expResult = 60;
        result = QT.getSE().getSE().getEdges().size();
        System.out.println("Result for QT 6 : " + result);
        assertEquals(expResult, result, 500);

        //QT#7
        expResult = 0;
        result = QT.getSE().getNW().getEdges().size();
        System.out.println("Result for QT 7 : " + result);
        assertEquals(expResult, result, 500);

        //QT#8
        expResult = 200;
        result = QT.getSE().getNE().getEdges().size();
        System.out.println("Result for QT 8 : " + result);
        assertEquals(expResult, result, 500);

        //QT#9
        expResult = 0;
        result = QT.getNW().getSW().getEdges().size();
        System.out.println("Result for QT 9 : " + result);
        assertEquals(expResult, result, 500);

        //QT#10
        expResult = 0;
        result = QT.getNW().getSE().getEdges().size();
        System.out.println("Result for QT 10 : " + result);
        assertEquals(expResult, result, 500);

        //QT#11
        expResult = 0;
        result = QT.getNW().getNW().getSW().getEdges().size();
        System.out.println("Result for QT 11 : " + result);
        assertEquals(expResult, result, 500);

        //QT#12
        expResult = 500;
        result = QT.getNW().getNW().getSE().getEdges().size();
        System.out.println("Result for QT 12 : " + result);
        assertEquals(expResult, result, 500);

        //QT#13
        expResult = 1;
        result = QT.getNW().getNW().getNW().getEdges().size();
        System.out.println("Result for QT 13 : " + result);
        assertEquals(expResult, result, 500);

        //QT#14
        expResult = 0;
        result = QT.getNW().getNW().getNE().getEdges().size();
        System.out.println("Result for QT 14 : " + result);
        assertEquals(expResult, result, 500);

        //QT#15
        expResult = 500;
        result = QT.getNW().getNE().getEdges().size();
        System.out.println("Result for QT 15 : " + result);
        assertEquals(expResult, result, 500);

        //QT#16
        expResult = 0;
        result = QT.getNE().getEdges().size();
        System.out.println("Result for QT 16 : " + result);
        assertEquals(expResult, result, 500);

    }
}