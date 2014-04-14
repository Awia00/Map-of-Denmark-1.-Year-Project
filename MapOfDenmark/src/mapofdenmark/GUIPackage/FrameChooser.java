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
    
    private JButton krakButton, openButton;
    
    public FrameChooser()
    {
        makeFrame();
    }
    
    public void makeFrame()
    {
        //frame = new JFrame("Map of Denmark");
        
        try {
                myPicture = ImageIO.read(new File("assets/Icon48.png"));
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
        
        krakButton = new JButton("Krak data");
        krakButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
				dispose();
				GUIController.LoadData(true);
            }
        });
        openButton = new JButton("Open-street-map data");
        openButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
				dispose();
				GUIController.LoadData(false);
            }
        });
        botContainer.add(krakButton);
        botContainer.add(openButton);
        
        topContainer.add(logoLabel);
        
        mainContainer.add(topContainer, "align center, wrap");
        mainContainer.add(botContainer, "wrap");
        
        
        
        pack();
        setResizable(false);
        repaint();
        setVisible(true);
    }
    
    
    public static void main(String[] args)
    {
        new FrameChooser();
    }
}
