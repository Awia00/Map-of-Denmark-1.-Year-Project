/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import java.awt.geom.Point2D;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.util.ArrayList;
import java.util.Collections;

/**
 * If you are looking for more details on the methods of this class, please
 * refer to the Interface that this class implements<p>
 *
 * SQLExceptions are handled by each and every method in this class.<p>
 *
 * NullPointerExceptions are not handled, unless explicitly stated.<p>
 *
 * @author Aleksandar Jonovic
 */
public class DatabaseHandler implements DatabaseInterface {

    //DB & SQL fields
    private static final String user = "jonovic_dk";
    private static final String pass = "Jegeradministratorher123";
    private static final String jdbcurl = "jdbc:sqlserver://db.jonovic.dk;databaseName=jonovic_dk_db;";
    ArrayList<PreparedStatement> pstatements = new ArrayList();
    ArrayList<ResultSet> resultsets = new ArrayList();
    ArrayList<Connection> cons = new ArrayList();
    ArrayList<SQLWarning> warnings = new ArrayList();
    ComboPooledDataSource cpds = new ComboPooledDataSource();
    //Fields for Streets, Edges and Nodes.
    ArrayList<Street> streets = new ArrayList<>();
    ArrayList<Edge> edges = new ArrayList<>();
    ArrayList<Node> nodes = new ArrayList<>();
    NodeComparer nc = new NodeComparer();
    EdgeComparer ec = new EdgeComparer();
    StreetComparer sc = new StreetComparer();

    private double nodesDownloadedPct, edgesDownloadedPct, streetsDownloadedPct;

    /**
     * Constructor for this object. For more detail about the API methods of
     * this class, please refer to:
     * {@link MapOfDenmark.database.DatabaseInterface DatabaseInterface}
     */
    protected DatabaseHandler() {
        try {
            //contructor method to initiate (combo)DataSource for pooled connections on spawning an object.
            //initiateDataSource();
            initiateDataSource();
        } catch (SQLException ex) {
            throw new RuntimeException("Something went wrong while initializing the DataSource", ex);
        }
        nodesDownloadedPct = 0;
        edgesDownloadedPct = 0;
        streetsDownloadedPct = 0;
    }

    public double getNodesDownloadedPct() {
        return nodesDownloadedPct;
    }

    public double getEdgesDownloadedPct() {
        return edgesDownloadedPct;
    }

    public double getStreetsDownloadedPct() {
        return streetsDownloadedPct;
    }

    private void initiateDataSource() throws SQLException {
        // initiates the connection to the database, and sets the basic parameters for the connection pooling.

        cpds.setUser(user);
        cpds.setPassword(pass);
        cpds.setJdbcUrl(jdbcurl);
        cpds.setMinPoolSize(30);
        cpds.setAcquireIncrement(3);
        cpds.setMaxPoolSize(1000);
        cpds.setIdleConnectionTestPeriod(10);
        cpds.setTestConnectionOnCheckin(true);
        cpds.setMaxIdleTimeExcessConnections(5);
        //initDataStructure();

    }

    private Connection getConnection() throws SQLException {

        return cpds.getConnection();

    }

    /**
     * This method is borrowed from:<br>
     *
     * http://docs.oracle.com/javase/tutorial/jdbc/basics/sqlexception.html<p>
     *
     * Direct download link to original source code:<br>
     * http://docs.oracle.com/javase/tutorial/jdbc/basics/examples/zipfiles/JDBCTutorial.zip<p>
     *
     * It provides a way of handling Exceptions, while ignoring common errors
     * which are not critical for application function.
     *
     *
     * @param sqlState SQL-state value
     * @return Boolean value TRUE or FALSE
     */
    public static boolean ignoreSQLException(String sqlState) {
        if (sqlState == null) {
            //System.out.println("The SQL state is not defined!");
            return false;
        }
        // X0Y32: Jar file already exists in schema
        if (sqlState.equalsIgnoreCase("X0Y32")) {
            return true;
        }
        // 42Y55: Table already exists in schema
        if (sqlState.equalsIgnoreCase("42Y55")) {
            return true;
        }
        return false;
    }

    /**
     * This method is borrowed from:<br>
     *
     * http://docs.oracle.com/javase/tutorial/jdbc/basics/sqlexception.html<p>
     *
     * Direct download link to original source code:<br>
     * http://docs.oracle.com/javase/tutorial/jdbc/basics/examples/zipfiles/JDBCTutorial.zip<p>
     *
     * It provides a way of handling Exceptions, while ignoring common errors
     * which are not critical for application function.
     *
     *
     * @param ex
     */
    public static void printSQLException(SQLException ex) {

        for (Throwable e : ex) {
            if (e instanceof SQLException) {
                if (ignoreSQLException(
                        ((SQLException) e).
                        getSQLState()) == false) {

                    e.printStackTrace(System.err);
                    System.err.println("SQLState: "
                            + ((SQLException) e).getSQLState());

                    System.err.println("Error Code: "
                            + ((SQLException) e).getErrorCode());

                    System.err.println("Message: " + e.getMessage());

                    Throwable t = ex.getCause();
                    while (t != null) {
                        //System.out.println("Cause: " + t);
                        t = t.getCause();
                    }
                }
            }
        }
    }

    private void executeUpdate(PreparedStatement pstatement) {
        //SET method, for putting data into the database.
        try {
            //Execute the prepared statement.
            pstatement.executeUpdate();

        } catch (SQLException ex) {
            printSQLException(ex);
        }
    }

    private ResultSet executeQuery(PreparedStatement pstatement) throws SQLException {
        //GET method for getting data from the database.
        ResultSet results = pstatement.executeQuery();
        return results;
    }

    /**
     *
     */
    private void getNumCon() {
        //Maintenance debug method, for getting all number of alive connections and connection pools - use to debug current state of the pool.
        try {
            System.out.println("Number of Connections: " + cpds.getNumConnections());
            System.out.println("Number of Idle Connections: " + cpds.getNumIdleConnections());
            System.out.println("Number of Busy Connections: " + cpds.getNumBusyConnections());
            System.out.println("Number of Connection Pools: " + cpds.getNumUserPools());
            System.out.println("");
        } catch (SQLException ex) {
            printSQLException(ex);
        }
    }

    private void closeConnection(ArrayList<Connection> cons, ArrayList<PreparedStatement> pstatements, ArrayList<ResultSet> resultsets) {
        //This is where all the "tidy'd" up Connections, preparedStatements and ResultSets go to die (and get closed).
        try {
            //Not all methods produce ResulSets, so check these first, and weed out.
            if (resultsets != null) {
                for (ResultSet results : resultsets) {
                    results.close();

                }
                this.resultsets.removeAll(resultsets);
            }
            //All methods create Connections and preparedStatements, so collect these, close them and destroy them.
            if (cons != null && pstatements != null) {

                for (PreparedStatement pstatement : pstatements) {
                    pstatement.close();

                }

                for (Connection con : cons) {
                    con.close();
                }

                this.pstatements.removeAll(pstatements);
                this.cons.removeAll(cons);

            }

        } catch (SQLException ex) {
            printSQLException(ex);
        }
    }

    private ArrayList<Node> getNodes() {
        Node node = null;
        try {

            //Get time, Connection and ResultSet
            Long time = System.currentTimeMillis();
            String sql = "SELECT * FROM [jonovic_dk_db].[dbo].[nodes];";
            Connection con = cpds.getConnection();

            PreparedStatement pstatement = con.prepareStatement(sql);
            ResultSet rs = executeQuery(pstatement);
            time -= System.currentTimeMillis();

            System.out.println("Time spent fetching elements: " + -time * 0.001 + " seconds...");

            int i = 0;
            
            //Add nodes to node ArrayList, and count percentage for loading screen.
            while (rs.next()) {
                node = new Node(rs.getInt(1), new Point2D.Double(rs.getDouble(2) - 440000, rs.getDouble(3) - 6040000)); // these subtractions needs to be done in the database.
                nodes.add(node);
                i++;
                nodesDownloadedPct += (double) 1 / 675902;
            }
            System.out.println("Total nodes: " + i);
            Collections.sort(nodes, nc);
        } catch (SQLException ex) {
            printSQLException(ex);
        } finally {
            closeConnection(cons, pstatements, resultsets);
        }
        nodesDownloadedPct = 1;
        return nodes;
    }

    private ArrayList<Edge> getEdges() {
        try {
            //Get time, Connection and ResultSet
            Long time = System.currentTimeMillis();
            Edge edge = null;
            String sql = "SELECT FNODE#, TNODE#, TYP, VEJNAVN, VEJKODE FROM [jonovic_dk_db].[dbo].[edges];";
            Connection con = cpds.getConnection();

            PreparedStatement pstatement = con.prepareStatement(sql);
            ResultSet rs = executeQuery(pstatement);
            time -= System.currentTimeMillis();

            System.out.println("Time spent fetching elements: " + -time * 0.001 + " seconds...");
            int i = 0;
            
            //Add edges to edge ArrayList, and count percentage for loading screen.
            while (rs.next()) {
                edge = new Edge(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getString(4), rs.getInt(5));
                edges.add(edge);
                i++;
                edgesDownloadedPct += (double) 1 / 812301;
            }

            System.out.println("Total edges: " + i);
        } catch (SQLException ex) {
            printSQLException(ex);
        } finally {
            closeConnection(cons, pstatements, resultsets);
        }
        Collections.sort(edges);
        edgesDownloadedPct = 1;
        return edges;
    }

    private Node getNode(int id) {
        Node node = null;
        Point2D p = new Point2D.Double(1, 1);
        node = nodes.get(Collections.binarySearch(nodes, new Node(id, p), nc));

        return node;
    }

    private ArrayList<Street> getStreets() {
        try {
            Long time = System.currentTimeMillis();
            Street street = null;
            String sql = "SELECT * FROM [jonovic_dk_db].[dbo].[road2id];";
            Connection con = cpds.getConnection();

            PreparedStatement pstatement = con.prepareStatement(sql);
            ResultSet rs = executeQuery(pstatement);
            time -= System.currentTimeMillis();

            System.out.println("Time spent fetching elements: " + -time * 0.001 + " seconds...");
            int i = 0;
            while (rs.next()) {

                street = new Street(rs.getInt(2), rs.getString(1));
                streets.add(street);
                i++;
                streetsDownloadedPct += (double) 1 / 115883;
            }

            System.out.println("Total streets: " + i);
        } catch (SQLException ex) {
            printSQLException(ex);
        } finally {
            closeConnection(cons, pstatements, resultsets);
        }
        Collections.sort(streets);
        streetsDownloadedPct = 1;
        return streets;
    }

    private ArrayList<Edge> getEdge(int id, String streetName) {
        ArrayList<Edge> edgeResults = new ArrayList<>();
        Edge e = new Edge(1, 1, 1, "1", id);
        int index = Collections.binarySearch(edges, e, ec);
        int indexVar = index;
        int topIndex = 0;
        int botIndex = 0;
        // go right
        do {

            //edgeResults.add(edges.get(indexVar-indexDif));
            //edges.remove(indexVar-indexDif);
            indexVar -= 10;
            if (indexVar < 0) {
                indexVar = 0;
            }
            if (edges.get(indexVar).getRoadcode() != id) {
                while (edges.get(++indexVar).getRoadcode() != id) {
                }
                botIndex = indexVar;
                break;
            }
        } while (true);

        indexVar = index;
        do {
            indexVar += 10;
            if (indexVar >= edges.size()) {
                indexVar = edges.size() - 1;
            }
            if (edges.get(indexVar).getRoadcode() != id) {
                while (edges.get(--indexVar).getRoadcode() != id) {
                }
                topIndex = indexVar;
                break;
            }
        } while (true);

        for (int i = botIndex; i <= topIndex; i++) {
            if (edges.get(i).getRoadName().equals(streetName)) {
                edgeResults.add(edges.get(i));
            }
        }
        return edgeResults;
    }

    private ArrayList<Street> getStreet(int id) {
        ArrayList<Street> streetResults = new ArrayList<>();

        Street s = new Street(id, "N/A");
        int index = Collections.binarySearch(streets, s, sc);
        int indexTemp = index;
        int indexMax;
        int indexMin;
        while (streets.get(indexTemp).getID() == id && indexTemp != streets.size() - 1) {
            indexTemp++;
        }
        indexMax = indexTemp;

        indexTemp = index;
        while (streets.get(indexTemp).getID() == id && indexTemp > 0) {
            indexTemp--;
        }
        indexMin = indexTemp;

        for (int i = indexMin; i <= indexMax; i++) {
            streetResults.add(streets.get(i));
        }

        return streetResults;
    }

    private void initDataStructure() {
        getNodes();
        getEdges();
        getStreets();
        int i = 0;
        for (Edge edge : edges) {
            edge.setFromNodeTrue(getNode(edge.getFromNode()));
            edge.setToNodeTrue(getNode(edge.getToNode()));
            edge.setMidNodeTrue();
            //System.out.println(i++);
        }
        System.out.println("Node-to-Edge joining complete.");
        /*for (Street street : streets) {
         ArrayList<Edge> el = getEdge(street.getID(), street.getStreetName());
         for (Edge e : el) {
         street.addEdge(e);
         }
         }*/
        /*
         for(Edge edge : edges){
            
         ArrayList<Street> s = getStreet(edge.getRoadcode());
         for(Street street : s){
         if(edge.getRoadName().equals(street.getStreetName()))
         {
         //System.out.println("Added edge!");
         street.addEdge(edge);
         }
         }
         }
        
         for (Street street : streets) {
         ArrayList<Edge> e = street.getEdges();
            
         if ( e.size() != 0){
         i += e.size();
         }
         }*/
        //System.out.println(i);

        //System.out.println("Edge-to-Street joining complete.");
    }

    @Override
    public ArrayList<Edge> getData() {
        if (edges.isEmpty()) {
            initDataStructure();
        }
        return edges;
    }
}
