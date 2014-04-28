/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mapofdenmark.GUIPackage;

import database.Database;
import database.DatabaseInterface;
import database.Edge;
import database.GraphCreator;
import database.Node;
import database.ShapeFileParser;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
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

	private static GUIController guiController;

	/**
	 * The GUI controller starts loading the data, and meanwhile creates the
	 * loading screen which shows the loading process. When the data is loaded
	 * it creates the MainFrame and shows it.
	 */
	private GUIController()
	{
		frameChooser = new FrameChooser();
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

				
				GraphCreator graph;
				graph = new GraphCreator(edges.toArray(new Edge[edges.size()]), db.getListOfNodes().toArray(new Node[db.getListOfNodes().size()]));

				List<PolygonShape> polygons = new ArrayList<>();
				if(!isKrakB){polygons=new ShapeFileParser().getPolygons();}
				mainframe = new MainFrame(quadTree, polygons); // brug disse 

				//mainframe = new MainFrame(new QuadTree(null, 0, 0, 0)); // brug denne hvis du ikke vil loade
				lframe.setVisible(false);
				lframe.dispose();
			}
		};
		thread.start();

	}

	public static void main(String[] args)
	{
		guiController = new GUIController();
	}

}
