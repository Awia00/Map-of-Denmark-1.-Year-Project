/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mapofdenmark.GUIPackage;

import database.Database;
import database.DatabaseInterface;
import database.Edge;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author Anders
 */
public class GUIController {

	protected DatabaseInterface db;
	protected LoadingFrame lframe;
	private MainFrame mainframe;
	private Timer timer;

	public GUIController()
	{
		lframe = new LoadingFrame();

		timer = new Timer();
		TimerTask task = new TimerTask() {

			@Override
			public void run()
			{
				lframe.updateLoadingBar(db.getNodesDownloadedPct(), db.getEdgesDownloadedPct(), db.getStreetsDownloadedPct());
				if(db.getStreetsDownloadedPct() == 1)
				{
					timer.cancel();
					timer.purge();
				}
			}
		};

		timer.scheduleAtFixedRate(task, 2000, 100);

		db = Database.db();

		List<Edge> edges = db.getData();
		
		lframe.setVisible(false);
		lframe.dispose();
		
		mainframe = new MainFrame(edges);
	}

	public static void main(String[] args)
	{
		new GUIController();
	}

}
