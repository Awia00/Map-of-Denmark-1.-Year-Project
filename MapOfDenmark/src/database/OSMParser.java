/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import mapofdenmark.GUIPackage.QuadTree;
import org.xml.sax.*;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.helpers.*;

/**
 * Class description:
 *
 * @version 0.1 - changed 14-04-2014
 * @authorNewVersion Anders Wind - awis@itu.dk
 *
 * @buildDate 14-04-2014
 * @author Anders Wind - awis@itu.dk
 */
public class OSMParser extends DefaultHandler implements DatabaseInterface {

	private double nodesDownloadedPct = 0, edgesDownloadedPct = 0, streetsDownloadedPct = 0;
	private double minX, maxX;
	private double minY, maxY;
	private HashMap<Long, Node> mapOfNodes;
	private ArrayList<Node> nodes;
	private ArrayList<Edge> edges;
	private QuadTree quadTree;
	private SAXParser saxParser;
	private boolean isParsed;

	private boolean createWay = false;
	private boolean createNode = false;
	private List<Node> nodesOnWay = new ArrayList<>();
	private String roadName = "";
	private String roadType = "";

	private Node placeNameNode = null;
	private String placeName = "";

	private HashSet<String> tempRoadTypeList = new HashSet<>();

	private int ID = 0;

	public OSMParser()
	{
		isParsed = false;
		nodesDownloadedPct = 0;
		edgesDownloadedPct = 0;
		streetsDownloadedPct = 0;
		nodes = new ArrayList<>();
		edges = new ArrayList<>();
	}

	private int getID()
	{
		return this.ID++;
	}

	private int convertRoadTypeToInt(String roadType)
	{
		if (roadType == null)
		{
			return -1;
		} else if (roadType.equalsIgnoreCase("motorway") || roadType.equalsIgnoreCase("motorway_link"))
		{
			return 1;
		} else if (roadType.equalsIgnoreCase("trunk") || roadType.equalsIgnoreCase("primary") || roadType.equalsIgnoreCase("trunk_link") || roadType.equalsIgnoreCase("primary_link"))
		{
			return 3;
		} else if (roadType.equalsIgnoreCase("tertiary") || roadType.equalsIgnoreCase("secondary") || roadType.equalsIgnoreCase("tunnel") || roadType.equalsIgnoreCase("secondary_link") || roadType.equalsIgnoreCase("tertiary_link"))
		{
			return 5;
		} else if (roadType.equalsIgnoreCase("residential") || roadType.equalsIgnoreCase("service") || roadType.equalsIgnoreCase("unclassified") || roadType.equalsIgnoreCase("road") || roadType.equalsIgnoreCase("mini_roundabout"))
		{
			return 6;
		} else if (roadType.equalsIgnoreCase("path") || roadType.equalsIgnoreCase("cycleway") || roadType.equalsIgnoreCase("footway"))
		{
			return 8;
		} else if (roadType.equalsIgnoreCase("coastline"))
		{
			return 75;
		} else if (roadType.equalsIgnoreCase("ferry"))
		{
			return 80;
		}else if (roadType.equalsIgnoreCase("city")) // big cities
		{
			return 102;
		}
		else if (roadType.equalsIgnoreCase("town")) // big cities
		{
			return 101;
		}
		else if (roadType.equalsIgnoreCase("village") || roadType.equalsIgnoreCase("suburb") || roadType.equalsIgnoreCase("hamlet") ||  roadType.equalsIgnoreCase("neighbourhood"))
		{
			return 100;
		}
		tempRoadTypeList.add(roadType);
		return -1;
	}

	private int convertRoadTypeToSpeedLimit(String roadType)
	{
		if (roadType == null)
		{
			return -1;
		} else if (roadType.equalsIgnoreCase("motorway"))
		{
			return 130;
		} //motortrafikvej amtsvej
		else if (roadType.equalsIgnoreCase("trunk") || roadType.equalsIgnoreCase("primary"))
		{
			return 110;
		} //hovedvej kommunevej
		else if (roadType.equalsIgnoreCase("secondary") || roadType.equalsIgnoreCase("motorway_link"))
		{
			return 90;
		} else if (roadType.equalsIgnoreCase("tertiary") || roadType.equalsIgnoreCase("trunk_link") || roadType.equalsIgnoreCase("primary_link") || roadType.equalsIgnoreCase("tunnel"))
		{
			return 80;
		} else if (roadType.equalsIgnoreCase("residential") || roadType.equalsIgnoreCase("road") || roadType.equalsIgnoreCase("secondary_link") || roadType.equalsIgnoreCase("tertiary_link"))
		{
			return 50;
		} else if (roadType.equalsIgnoreCase("service") || roadType.equalsIgnoreCase("unclassified") || roadType.equalsIgnoreCase("mini_roundabout"))
		{
			return 30;
		} else if (roadType.equalsIgnoreCase("ferry"))
		{
			return 60;
		} else if (roadType.equalsIgnoreCase("path") || roadType.equalsIgnoreCase("cycleway") || roadType.equalsIgnoreCase("footway"))
		{
			return -1;
		} else if (roadType.equalsIgnoreCase("coastline"))
		{
			return -1;
		}
		return -1;
	}

	@Override
	public void startDocument() throws SAXException
	{
		mapOfNodes = new HashMap<>();
//		System.out.println("start document   : ");
	}

	@Override
	public void endDocument() throws SAXException
	{
		for (String roadType : tempRoadTypeList)
		{
//			System.out.println(roadType);
		}
		mapOfNodes = null;
		nodesDownloadedPct = 1;
		edgesDownloadedPct = 1;
		streetsDownloadedPct = 1;
//		System.out.println("end document     : ");
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException
	{
		//System.out.println("start characters : " + new String(ch, start, length));
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
	{
            switch (qName) {
                case "node":
                    //Node node = new Node(Long.parseLong(attributes.getValue("id")), new Point2D.Double(Double.parseDouble(attributes.getValue("lon"))*10000, Double.parseDouble(attributes.getValue("lat"))*15000));
                    Node node = new Node(getID(), new Point2D.Double(Double.parseDouble(attributes.getValue("lon")) * 100000, Double.parseDouble(attributes.getValue("lat")) * 150000));
                    //System.out.println(node);
                    placeNameNode = node;
                    mapOfNodes.put(Long.parseLong(attributes.getValue("id")), node);
                    nodes.add(node);
                    nodesDownloadedPct += (double) 1 / 3500000;
                    createNode = true;
                    return;
                case "way":
                    createWay = true;
                    return;
                case "nd":
                    if (createWay)
                    {
                        nodesOnWay.add(mapOfNodes.get(Long.parseLong(attributes.getValue("ref"))));
                    }
                    break;
                case "tag":
                    if (createWay)
                    {
                        if (attributes.getValue("k").equalsIgnoreCase("highway"))//|| attributes.getValue("k").equalsIgnoreCase("natural"))
                        {
                            roadType = attributes.getValue("v");
                        }
                        if (attributes.getValue("k").equalsIgnoreCase("route"))
						{
							roadType = attributes.getValue("v"); // for ferries
						}
                        if (attributes.getValue("k").equalsIgnoreCase("name"))
                        {
                            roadName = attributes.getValue("v");
                        }
                    } else if (createNode)
                    {
                        if (attributes.getValue("k").equalsIgnoreCase("place"))//|| attributes.getValue("k").equalsIgnoreCase("natural"))
                        {
                            roadType = attributes.getValue("v");
                        }
                        if (attributes.getValue("k").equalsIgnoreCase("name"))
                        {
                            placeName = attributes.getValue("v");
                        }
                    }
                    break;
                case "bounds":
                    minX = Double.parseDouble(attributes.getValue("minlon")) * 100000;
                    minY = Double.parseDouble(attributes.getValue("minlat")) * 150000;
                    maxX = Double.parseDouble(attributes.getValue("maxlon")) * 100000;
                    maxY = Double.parseDouble(attributes.getValue("maxlat")) * 150000;
//                    System.out.println(minX + ", " + minY + " and " + maxX + ", " + maxY);
                    return;
            }
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
            switch (qName) {
                case "node":
                    if (convertRoadTypeToInt(roadType) != -1)
                    {
                        edges.add(new Edge(placeNameNode, placeNameNode, convertRoadTypeToInt(roadType), placeName, 0, convertRoadTypeToSpeedLimit(roadType)));
                    }
                    roadType = "";
                    placeName = "";
                    createNode = false;
                    placeNameNode = null;
                    break;
                case "way":
                    Node fromNode = null;
                    if (convertRoadTypeToInt(roadType) != -1 || convertRoadTypeToInt(roadType) == 99)
                    {
                        for (Node node : nodesOnWay)
                        {
                            if (fromNode != null)
                            {
                                edges.add(new Edge(fromNode, node, convertRoadTypeToInt(roadType), roadName, 0, convertRoadTypeToSpeedLimit(roadType)));
                                nodesDownloadedPct += (double) 1 / 6500000;
                                fromNode = node;
                            } else
                            {
                                fromNode = node;
                            }
                        }
                    }
                    // reset
                    roadName = "";
                    roadType = "";
                    fromNode = null;
                    nodesOnWay = new ArrayList<>();
                    createWay = false;
                    break;
            }
	}

	private void initiateParsing()
	{
		// http://tutorials.jenkov.com/java-xml/sax.html
		SAXParserFactory factory = SAXParserFactory.newInstance();
		File file = new File("assets/OSM_MapOfDenmark.osm");
		try
		{
			InputStream openStreetMapData = new FileInputStream(file);
			SAXParser saxParser = factory.newSAXParser();
			saxParser.parse(openStreetMapData, this);
			isParsed = true;
			//Release Memory
			openStreetMapData.close();
			saxParser.reset();

		} catch (Throwable err)
		{
			err.printStackTrace();
		}
		isParsed = false;
		tempRoadTypeList.clear();
		file = new File("assets/OSM_PlaceNames.osm");
		try
		{
			InputStream openStreetMapData = new FileInputStream(file);
			SAXParser saxParser = factory.newSAXParser();
			saxParser.parse(openStreetMapData, this);
			isParsed = true;
			//Release Memory
			openStreetMapData.close();
			saxParser.reset();
		} catch (Throwable err)
		{
			err.printStackTrace();
		}

		quadTree = new QuadTree(edges, minX, minY, Math.max(maxX - minX, maxY - minY));
	}

	@Override
	public ArrayList<Edge> getEdgeList()
	{
		if (isParsed)
		{
			return edges;
		} else
		{
			initiateParsing();
			return edges;
		}
	}

	@Override
	public ArrayList<Node> getListOfNodes()
	{
		if (isParsed)
		{
			return nodes;
		} else
		{
			initiateParsing();
			return nodes;
		}
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
		if (isParsed)
		{
			return quadTree;
		} else
		{
			initiateParsing();
			return quadTree;
		}
	}

}
