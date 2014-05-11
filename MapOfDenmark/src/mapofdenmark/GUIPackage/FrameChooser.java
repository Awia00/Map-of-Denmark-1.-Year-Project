/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mapofdenmark.GUIPackage;

//import javax.swing.JFrame;
//import java.awt.Container;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;

/**
 *
 * @author Envy
 */
public class FrameChooser extends JFrame {

    private Container mainContainer;
    private Container topContainer;
    private Container botContainer;
    private BufferedImage myPicture;
    private BufferedImage krakLogo;
    private BufferedImage osmLogo;
    
    private JButton krakButton, openButton;
    
    public FrameChooser()
    {
        makeFrame();
        
    }
    
    private void makeFrame()
    {
        //frame = new JFrame("Map of Denmark");
        
        try {
                myPicture = ImageIO.read(new File("assets/Icon48.png"));
                krakLogo = ImageIO.read(new File ("assets/krak.gif"));
                osmLogo = ImageIO.read(new File ("assets/osm.png"));
                setIconImage(myPicture);                
            } catch (IOException ex) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        JLabel logoLabel = new JLabel(new ImageIcon(myPicture));
        
        
        MigLayout migMainLayout = new MigLayout();
        mainContainer = new JPanel(migMainLayout);
        
        this.add(mainContainer);
        
        topContainer = new JPanel();
       
        botContainer = new JPanel();
        botContainer.setLayout(new MigLayout("", "[center][center]", "[center][center]"));
        
        krakButton = new JButton("");
        krakButton.setFocusable(false);
        krakButton.setIcon(new ImageIcon(krakLogo));
        krakButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                
				GUIController.startLoading(true);
            }
        });
        openButton = new JButton("");
        openButton.setFocusable(false);
        openButton.setIcon(new ImageIcon(osmLogo));
        openButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
				GUIController.startLoading(false);
            }
        });
        
        JLabel osm = new JLabel("OpenStreetMap");
        osm.setFont(FontLoader.getFontWithSize("Roboto-Regular", 14f));
        
        JLabel krak = new JLabel("Krak");
        krak.setFont(FontLoader.getFontWithSize("Roboto-Regular", 14f));
        
        botContainer.add(krakButton, "cell 0 0");
        botContainer.add(openButton, "cell 1 0");
        botContainer.add(krak, "cell 0 1");
        botContainer.add(osm, "cell 1 1");
        
        JLabel header = new JLabel("Please select a dataset");
        header.setFont(FontLoader.getFontWithSize("Roboto-Regular", 18f));
        topContainer.add(header);
        
        mainContainer.add(topContainer, "align center, wrap");
        mainContainer.add(botContainer, "wrap");
        
        
        
        pack();
        setLocationRelativeTo(null);
        setResizable(false);
        repaint();
        setVisible(true);
    }
    
    
    public static void main(String[] args)
    {
        new FrameChooser();
    }
}
