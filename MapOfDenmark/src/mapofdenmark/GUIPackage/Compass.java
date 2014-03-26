/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mapofdenmark.GUIPackage;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author Christian
 */

public class Compass extends JPanel implements MouseListener {
    
    public Compass() {
        setLayout(new MigLayout("", "[]5[]48[]","[][][]"));
        
        JComponent north = new CompassButton("north");
        JComponent south = new CompassButton("south");
        JComponent east = new CompassButton("east");
        JComponent west = new CompassButton("west");
        
        add(north, "cell 1 0 3 1");
        add(west, "cell 0 1 1 1");
        add(east,  "cell 2 1 1 1");
        add(south, "cell 1 2 3 1");
        
        north.addMouseListener(this);
        west.addMouseListener(this);
        east.addMouseListener(this);
        south.addMouseListener(this);
       
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        CompassButton cb = (CompassButton) e.getComponent();
        String buttonOrientation = cb.getOrientation();
        switch (buttonOrientation) {
            case "north" : System.out.println(buttonOrientation);
            break;
            
            case "south" : System.out.println(buttonOrientation);
            break;
            
            case "east" : System.out.println(buttonOrientation);
            break;
            
            case "west" : System.out.println(buttonOrientation);
            break;
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        requestFocus();
    }

    @Override
    public void mouseExited(MouseEvent e) {
        
    }
    
    
private class CompassButton extends JComponent {
        // The image to display
  private Image img;
  private String orientation;
  // The MouseListener that handles the click, etc.


  // Instantiate the panel and perform initialization
  CompassButton(String filename) {
      try {
          img = ImageIO.read(new File("assets/" + filename + ".png"));
      } catch (IOException ex) {
          Logger.getLogger(CompassButton.class.getName()).log(Level.SEVERE, null, ex);
      }
      orientation = filename;
  }
  
  public String getOrientation() {
      return orientation;
  }

  @Override
  public void paintComponent(Graphics g) {
    g.drawImage(img, 0, 0, null);
  }


  @Override
  public Dimension getPreferredSize() {
    return new Dimension(img.getWidth(this), img.getHeight(this));
  }
}

public static void main(String[] args) {
        JFrame frame = new JFrame();
        JPanel panel = new Compass();
                        
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
        
    }
}
