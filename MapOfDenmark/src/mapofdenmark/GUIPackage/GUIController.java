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
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author Anders
 */
public class GUIController {

	protected static DatabaseInterface db;
	protected static LoadingFrame lframe;
	private static FrameChooser frameChooser;
	private static MainFrame mainframe;
	private static Timer timer;

	/**
	 * The GUI controller starts loading the data, and meanwhile creates the
	 * loading screen which shows the loading process. When the data is loaded
	 * it creates the MainFrame and shows it.
	 */
	public static void Initiate()
	{
		frameChooser = new FrameChooser();
	}

	public static void LoadData(boolean isKrak)
	{
		frameChooser.setVisible(false);
		frameChooser.dispose();
		
		db = Database.db(isKrak); // true for krak
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
		timer.scheduleAtFixedRate(task, 4000, 100);

		
		List<Edge> edges = db.getData();
		QuadTree quadTree = db.getQuadTree();

		GraphCreator graph;
		graph = new GraphCreator(edges.toArray(new Edge[edges.size()]), db.getListOfNodes().toArray(new Node[db.getListOfNodes().size()]));

		mainframe = new MainFrame(quadTree); // brug disse 
		
				
		
		//mainframe = new MainFrame(new QuadTree(null, 0, 0, 0)); // brug denne hvis du ikke vil loade
		
				
		lframe.setVisible(false);
		lframe.dispose();
				
	}

	public static void main(String[] args)
	{
		GUIController.Initiate();
	}

}
