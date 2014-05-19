/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mapofdenmark.GUIPackage;

import AddressParser.AddressFinder;
import database.Database;
import database.DatabaseInterface;
import database.Edge;
import database.ShapeFileParser;
import database.pathfinding.WeightedMapGraph;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import org.nocrala.tools.gis.data.esri.shapefile.shape.shapes.PolygonShape;

/**
 *
 * @author Anders
 */
public class GUIController {

	protected DatabaseInterface db;
	protected LoadingFrame lframe;
	private FrameChooser frameChooser;
	private MainFrame mainframe;
	private Timer timer;

	private static WeightedMapGraph graph;

	private static GUIController guiController;

	/**
	 * The GUI controller starts loading the data, and meanwhile creates the
	 * loading screen which shows the loading process. When the data is loaded
	 * it creates the MainFrame and shows it.
	 */
	private GUIController()
	{
		frameChooser = new FrameChooser();
		try {UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
		//Handle exception
        }
	}

	public static void startLoading(boolean isKrak)
	{
		guiController.loadData(isKrak);
	}

	public void loadData(boolean isKrak)
	{
		final boolean isKrakB = isKrak;
		// Solution with thread taken from StackOverflow: http://stackoverflow.com/questions/9419252/why-does-this-simple-java-swing-program-freeze
		Thread thread = new Thread() {
			@Override
			@SuppressWarnings("empty-statement")
			public void run()
			{
				frameChooser.setVisible(false);
				frameChooser.dispose();

				db = Database.db(isKrakB); // true for krak
				lframe = new LoadingFrame();

				timer = new Timer();
				TimerTask task = new TimerTask() {

					@Override
					public void run()
					{
						lframe.updateLoadingBar(db.getNodesDownloadedPct(), db.getEdgesDownloadedPct(), db.getStreetsDownloadedPct());
						if (db.getStreetsDownloadedPct() == 1)
						{
							timer.cancel();
							timer.purge();
						}
					}
				};
				timer.scheduleAtFixedRate(task, 1000, 100);

				List<Edge> edges = db.getEdgeList();
				QuadTree quadTree = db.getQuadTree();

				graph = new WeightedMapGraph(edges);
				AddressFinder addressFinder = new AddressFinder(edges);

				List<PolygonShape> landShapePolygons = new ArrayList<>();
				List<PolygonShape> landUsePolygons = new ArrayList<>();
				if (!isKrakB)
				{
					ShapeFileParser shapeParser = new ShapeFileParser();

					landShapePolygons = shapeParser.getLandShapePolygons();

					landUsePolygons = shapeParser.getLandUsePolygons();
				}
				mainframe = new MainFrame(quadTree, landShapePolygons, landUsePolygons, addressFinder); // brug disse 

				//mainframe = new MainFrame(new QuadTree(null, 0, 0, 0)); // brug denne hvis du ikke vil loade
				lframe.setVisible(false);
				lframe.dispose();
			}
		};
		thread.start();

	}

	public static WeightedMapGraph getGraph()
	{
		return graph;
	}

	public static void main(String[] args)
	{
		guiController = new GUIController();

	}

}
