package database;

import java.awt.Color;
import java.awt.Polygon;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.nocrala.tools.gis.data.esri.shapefile.ShapeFileReader;
import org.nocrala.tools.gis.data.esri.shapefile.exception.InvalidShapeFileException;
import org.nocrala.tools.gis.data.esri.shapefile.header.ShapeFileHeader;
import org.nocrala.tools.gis.data.esri.shapefile.shape.AbstractShape;
import org.nocrala.tools.gis.data.esri.shapefile.shape.PointData;
import org.nocrala.tools.gis.data.esri.shapefile.shape.ShapeType;
import org.nocrala.tools.gis.data.esri.shapefile.shape.shapes.MultiPointZShape;
import org.nocrala.tools.gis.data.esri.shapefile.shape.shapes.PointShape;
import org.nocrala.tools.gis.data.esri.shapefile.shape.shapes.PolygonShape;

/**
 * Class description:
 *
 * @version 0.1 - changed 28-04-2014
 * @authorNewVersion Anders Wind - awis@itu.dk
 *
 * @buildDate 28-04-2014
 * @author Anders Wind - awis@itu.dk
 */
public class ShapeFileParser {

	List<PolygonShape> polygons;

	public ShapeFileParser()
	{
		polygons = new ArrayList<>();
		startParsing();
	}

	public List<PolygonShape> getPolygons()
	{
		return polygons;
	}

	private void startParsing()
	{
		try
		{
			parseLanduse();
		} catch (InvalidShapeFileException | IOException ex)
		{
			Logger.getLogger(ShapeFileParser.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	private void parseLanduse() throws FileNotFoundException, InvalidShapeFileException, IOException
	{
		try (FileInputStream is = new FileInputStream(
				"assets/shape/landuse.shp"))
		{
			ShapeFileReader r = new ShapeFileReader(is);

			ShapeFileHeader h = r.getHeader();
			System.out.println("The shape type of this files is " + h.getShapeType());

			int total = 0;
			AbstractShape s;
			while ((s = r.next()) != null)
			{

				switch (s.getShapeType())
				{
					case POINT:
						PointShape aPoint = (PointShape) s;
						// Do something with the point shape...
						break;
					case MULTIPOINT_Z:
						MultiPointZShape aMultiPointZ = (MultiPointZShape) s;
						// Do something with the MultiPointZ shape...
						break;
					case POLYGON:
						PolygonShape aPolygon = (PolygonShape) s;
						polygons.add(aPolygon);

						/*
						 System.out.println("I read a Polygon with "
						 + aPolygon.getNumberOfParts() + " parts and "
						 + aPolygon.getNumberOfPoints() + " points");
						 for (int i = 0; i < aPolygon.getNumberOfParts(); i++)
						 {
						 PointData[] points = aPolygon.getPointsOfPart(i);
						 System.out.println("- part " + i + " has " + points.length
						 + " points.");
						 }
						 for(PointData points : aPolygon.getPoints())
						 {
						 System.out.println("point x: " + points.getX() + " y: " + points.getY());
						 }
						 */
						break;
					default:
						System.out.println("Read other type of shape.");
				}
				total++;
			}

			System.out.println("Total shapes read: " + total);
		}catch(FileNotFoundException ex)
		{
			
		}
		try (FileInputStream is = new FileInputStream(
				"assets/shape/landuse.chp"))
		{
			ShapeFileReader r = new ShapeFileReader(is);

			ShapeFileHeader h = r.getHeader();
			System.out.println("The shape type of this files is " + h.getShapeType());
		}catch(FileNotFoundException ex)
		{
			
		}
	}

	private int[][] splitData(PointData[] points)
	{
		return new int[2][points.length];
	}

	public static void main(String[] args)
	{
		new ShapeFileParser();
	}
}
