/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package database;

import java.util.ArrayList;
import mapofdenmark.GUIPackage.QuadTree;
import org.xml.sax.*;
import org.xml.sax.helpers.*;


/**
 * Class description:
 *
 * @version 0.1 - changed 14-04-2014
 * @authorNewVersion  Anders Wind - awis@itu.dk
 *
 * @buildDate 14-04-2014
 * @author Anders Wind - awis@itu.dk
 */
public class OSMParser extends DefaultHandler implements DatabaseInterface {

	public OSMParser()
	{
		
	}
	
	/**
	 * Gets called when it hits a 
	 * @param uri
	 * @param localName
	 * @param qName
	 * @param attributes
	 * @throws SAXException 
	 */
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
	{
		super.startElement(uri, localName, qName, attributes); //To change body of generated methods, choose Tools | Templates.
	}

	/**
	 * 
	 * @param uri
	 * @param localName
	 * @param qName
	 * @throws SAXException 
	 */
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException
	{
		super.endElement(uri, localName, qName); //To change body of generated methods, choose Tools | Templates.
	}

	
	
	@Override
	public ArrayList<Edge> getEdgeList()
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public ArrayList<Node> getListOfNodes()
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public double getNodesDownloadedPct()
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public double getEdgesDownloadedPct()
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public double getStreetsDownloadedPct()
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public QuadTree getQuadTree()
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

}
