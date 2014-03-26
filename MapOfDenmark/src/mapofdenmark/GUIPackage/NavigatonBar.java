/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mapofdenmark.GUIPackage;

import java.awt.Color;
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
    
    private PlaceholderTextField from;
    private PlaceholderTextField to;
    private JLabel rutevejledning;
    private Button visVej;
    private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    
    public NavigatonBar() {
        rutevejledning = new JLabel("Rutevejledning");
        rutevejledning.setFont(FontLoader.getFontWithSize("Roboto-Bold", 15f));
        rutevejledning.setForeground(Color.decode("#9B9B9B"));
        
        from = new PlaceholderTextField("");
        from.setPlaceholder("Fra");
        from.setColumns(20);
        from.setFont(FontLoader.getFontWithSize("Roboto-Bold", 14f));
        
        to = new PlaceholderTextField("");
        to.setPlaceholder("Til");
        to.setColumns(20);
        to.setFont(FontLoader.getFontWithSize("Roboto-Bold", 14f));
        
        visVej = new Button("button");
        //visVej.setText("Vis");
        
        setLayout(new MigLayout("", "[center]","[][][][][]"));
        
        add(rutevejledning, "cell 0 0, align left");
        add(from, "cell 0 1");
        add(to, "cell 0 2");
        add(visVej, "cell 0 3, align right");
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
