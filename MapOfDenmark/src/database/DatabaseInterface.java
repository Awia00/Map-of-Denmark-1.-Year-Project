/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

/**
 * This interface states all public methods available for interaction with the
 * database, and should provide sufficient coverage for developers wishing to
 * use it, and it's associated classes to build a user interface.
 * <p>
 *
 * Please note that while SQLExceptions are handled, NullPointerExceptions are
 * not, unless explicitly stated.
 *
 * @author Aleksandar Jonovic
 */
public interface DatabaseInterface {
    void getEdges(); 
    String getString();
    public double[][] getNodeIDs(String vejnavn, int vejkode);
    public void getNodes();
     public void getEdges2();
}
