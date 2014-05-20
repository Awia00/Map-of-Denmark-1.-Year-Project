/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JLabel;

/**
 * Class description:
 *
 * @version 0.1 - changed 28-03-2014
 * @authorNewVersion Anders Wind - awis@itu.dk
 *
 * @buildDate 28-03-2014
 * @author Anders Wind - awis@itu.dk
 */
public class AAJLabel extends JLabel {

	public AAJLabel(String str)
	{
		super(str);
	}

	@Override
	public void paint(Graphics g)
	{
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
				RenderingHints.VALUE_FRACTIONALMETRICS_ON);

		super.paint(g);
	}
}
