/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mapofdenmark.GUIPackage;

import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.HeadlessException;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author Anders
 */
public class LoadingFrame extends JFrame {

	private loadingComponent loadingBar;
	private JTextField messageField;
	private Container mainContainer;

	public LoadingFrame()
	{
		setTitle("Map of Denmark");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		requestFocus();
		setLocationRelativeTo(null);
		MigLayout migMainLayout = new MigLayout("", "10[center]10[center]10", "10[center]10[center]10");

		mainContainer = new JPanel(migMainLayout);

		getContentPane().add(mainContainer);

		messageField = new JTextField("Step 1/4 - Loading nodes from the database...");
		messageField.setEditable(false);
		loadingBar = new loadingComponent();

		mainContainer.add(messageField, "cell 0 0");
		mainContainer.add(loadingBar, "cell 0 1, width 501:501:501, height 51:51:51");

		revalidate();
		repaint();
		pack();
		setVisible(true);
	}

	public void updateLoadingBar(double nodesLoaded, double edgesLoaded, double streetsLoaded)
	{
		loadingBar.setValues(nodesLoaded, edgesLoaded, streetsLoaded);
		if (streetsLoaded >= 1)
		{
			messageField.setText("Step 4/4 - Preparing the last details...");
		} else if (edgesLoaded >= 1)
		{
			messageField.setText("Step 3/4 - Loading streets from the database...");
		} else if (nodesLoaded >= 1)
		{
			messageField.setText("Step 2/4 - Loading edges from the database...");
		}
		this.repaint();
	}

	private static class loadingComponent extends JComponent {

		private double nodesLoaded, edgesLoaded, streetsLoaded;

		public loadingComponent()
		{
			nodesLoaded = 0;
			edgesLoaded = 0;
			streetsLoaded = 0;
		}

		public void setValues(double nodesLoaded, double edgesLoaded, double streetsLoaded)
		{
			this.nodesLoaded = nodesLoaded;
			this.edgesLoaded = edgesLoaded;
			this.streetsLoaded = streetsLoaded;
			repaint();
		}

		@Override
		public void paint(Graphics g)
		{
			super.paint(g); //To change body of generated methods, choose Tools | Templates.

			Color color = new Color((int)(streetsLoaded*255),(int)(nodesLoaded*255),(int)(edgesLoaded*255));
			g.setColor(color);
			g.fillRect(0, 0, (int) (((nodesLoaded + edgesLoaded + streetsLoaded) / 3) * 500), 50);
			g.setColor(Color.black);
			g.drawRect(0, 0, 500, 50);
			g.drawString("Loading process...", 200, 28);
		}
	}
}
