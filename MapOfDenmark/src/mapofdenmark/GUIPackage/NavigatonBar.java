/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mapofdenmark.GUIPackage;

import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author Christian
 */
public class NavigatonBar extends JPanel {
    
    private JTextField from;
    private JTextField to;
    private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    
    public NavigatonBar() {
        from = new JTextField("From", 100);
        to = new JTextField("To", 100);
        setLayout(new MigLayout("", "[center]","[][]400[]"));
        
        add(from, "cell 0 0");
        add(to, "cell 0 1");
//        add(new Compass(), "cell 0 2");
    }
    
    
    @Override
  public Dimension getPreferredSize() {
    return new Dimension(300, 800);
  }
  
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.add(new NavigatonBar());
        frame.pack();
        frame.setVisible(true);
    }
}
