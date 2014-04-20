/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package database;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;

import mapofdenmark.GUIPackage.QuadTree;
import org.xml.sax.*;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
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

	private double nodesDownloadedPct = 0, edgesDownloadedPct = 0, streetsDownloadedPct = 0;
	private double minX, maxX;
	private double minY, maxY;
	private ArrayList<Node> nodes;
	private ArrayList<Edge> edges;
	private QuadTree quadTree;
	private SAXParser saxParser;
	
	public OSMParser()
	{
		nodesDownloadedPct = 0;
		edgesDownloadedPct = 0;
		streetsDownloadedPct = 0;
		nodes = new ArrayList<>();
		edges = new ArrayList<>();
		
		// http://tutorials.jenkov.com/java-xml/sax.html
		SAXParserFactory factory = SAXParserFactory.newInstance();
		File file = new File("src\\highways.osm");
		try{
			InputStream openStreetMapData = new FileInputStream(file);
			SAXParser saxParser = factory.newSAXParser();
			saxParser.parse(openStreetMapData, this);
		}
		catch(Throwable err)
		{
			err.printStackTrace();
		}
		
	}
	

	@Override
	public void startDocument() throws SAXException
	{
		System.out.println("start document   : ");
	}

	@Override
	public void endDocument() throws SAXException
	{
		System.out.println("end document     : ");
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException
	{
		System.out.println("start characters : " + new String(ch, start, length));
	}
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
	{
		System.out.println("start element    : " + qName);
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
		System.out.println("end element      : " + qName);
		super.endElement(uri, localName, qName); //To change body of generated methods, choose Tools | Templates.
	}

	
	
	@Override
	public ArrayList<Edge> getEdgeList()
	{
		return edges;
	}

	@Override
	public ArrayList<Node> getListOfNodes()
	{
		return nodes;
	}

	@Override
	public double getNodesDownloadedPct()
	{
		return nodesDownloadedPct;
	}

	@Override
	public double getEdgesDownloadedPct()
	{
		return edgesDownloadedPct;
	}

	@Override
	public double getStreetsDownloadedPct()
	{
		return streetsDownloadedPct;
	}

	@Override
	public QuadTree getQuadTree()
	{
		return quadTree;
	}

}
