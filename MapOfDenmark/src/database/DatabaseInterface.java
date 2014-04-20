/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import java.util.ArrayList;
import mapofdenmark.GUIPackage.QuadTree;

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

	public ArrayList<Edge> getEdgeList();
	
	public ArrayList<Node> getListOfNodes();

	public double getNodesDownloadedPct();

	public double getEdgesDownloadedPct();

	public double getStreetsDownloadedPct();

        public QuadTree getQuadTree();
}
