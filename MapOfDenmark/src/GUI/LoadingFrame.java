/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author Anders
 */
public class LoadingFrame extends JFrame implements MouseMotionListener{

	private loadingComponent loadingBar;
	private JLabel messageField;
	private Container mainContainer;

	public LoadingFrame()
	{
		try {
                setIconImage(ImageIO.read(new File("assets/Icon48.png")));
                
            } catch (IOException ex) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
		setTitle("Map of Denmark");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		requestFocus();
		setLocationRelativeTo(null);
		setUndecorated(true);
		MigLayout migMainLayout = new MigLayout("", "10[center]10[center]10", "10[center]10[center]10");

		mainContainer = new JPanel(migMainLayout);

		getContentPane().add(mainContainer);

		messageField = new AAJLabel("Step 1/4 - Loading nodes...");
		messageField.setFont(FontLoader.getFontWithSize("Roboto-Bold", 14f));
	
		loadingBar = new loadingComponent();

		mainContainer.add(messageField, "cell 0 0");
		mainContainer.add(loadingBar, "cell 0 1, width 501:501:501, height 51:51:51");

		
		loadingBar.addMouseMotionListener(this);
		revalidate();
		repaint();
		pack();
        setLocationRelativeTo(null);
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
			messageField.setText("Step 3/4 - Building data structures...");
		} else if (nodesLoaded >= 1)
		{
			messageField.setText("Step 2/4 - Loading edges...");
		}
		this.repaint();
	}

	@Override
	public void mouseDragged(MouseEvent e)
	{
		//
	}

	@Override
	public void mouseMoved(MouseEvent e)
	{
		loadingBar.mouseHover(e);
	}

	private static class loadingComponent extends JComponent {

		private double nodesLoaded, edgesLoaded, streetsLoaded;
		private int mouseHoverX, mouseHoverY;

		public loadingComponent()
		{
			nodesLoaded = 0;
			edgesLoaded = 0;
			streetsLoaded = 0;
			mouseHoverX = 0;
			mouseHoverY = 0;
		}

		public void mouseHover(MouseEvent e)
		{
			mouseHoverX = e.getX();
			mouseHoverY = e.getY();
		}
		public void setValues(double nodesLoaded, double edgesLoaded, double streetsLoaded)
		{
			this.nodesLoaded = nodesLoaded;
			this.edgesLoaded = edgesLoaded;
			this.streetsLoaded = streetsLoaded;
		}

		@Override
		public void paint(Graphics g)
		{
			                 Graphics2D g2 = (Graphics2D)g;
                                         g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			Color loadingColor = new Color((int)(((double)mouseHoverY/50)*255),255,(int)(((double)mouseHoverX/500)*255));
			int position = 500%((int)((nodesLoaded+edgesLoaded+streetsLoaded)*150)+1);
			g2.setColor(loadingColor);
			g2.fillRect(0, 0, (int) (((nodesLoaded + edgesLoaded + streetsLoaded) / 3) * 500), 50);
			
			// white tranparent hover box
			Color color = new Color(255,255,255,100);
			Color color2 = new Color(255,255,255,50);
			g2.setColor(color);
			g2.fillRect(position, 0,15,50);
			g2.setColor(color2);
			g2.fillRect(position-5, 0,5,50);
			g2.fillRect(position+15, 0,5,50);
			
			g2.setFont(FontLoader.getFontWithSize("Roboto-Light", 12f));
			g2.setColor(Color.black);
			
			// draw a stickman
			int i = (int) (((nodesLoaded + edgesLoaded + streetsLoaded) / 3) * 500);
			g2.drawOval(i+1+3, 20, 10, 10); // hovede
			g2.drawOval(i+3+3, 22, 2, 2); // øje
			g2.drawLine(i+4,25,i+7,27); // mund
			
			g2.drawLine(i+5+3, 32, i, 25); // arms
			g2.drawLine(i+5+3, 32, i, 30); // arms
			
			g2.drawLine(i+5+3, 30,i+5+3, 38); // body
			
			g2.drawLine(i+5+3, 38, i+2+3, 50); // legs
			g2.drawLine(i+5+3, 38, i+13+3, 50);
			
			g2.drawOval(i+17, 48, 2, 2); // ruble
			g2.drawOval(i+19, 45, 3, 3); // ruble
			g2.drawOval(i+22, 47, 2, 2); // ruble
			
			g2.drawRect(0, 0, 500, 50);
			g2.drawString("Loading process...", 200, 28);
		}
	}
}
