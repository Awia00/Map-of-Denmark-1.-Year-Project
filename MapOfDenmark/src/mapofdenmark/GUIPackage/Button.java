/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mapofdenmark.GUIPackage;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;

/**
 *
 * @author Christian
 */
public class Button extends JLabel {
    
private BufferedImage buttonImage;

public Button(String imageName) {
    try {
        buttonImage = ImageIO.read(new File("assets/" + imageName + ".png"));
    } catch (IOException e) {
        e.printStackTrace();
    }
    this.setOpaque(false);

    //this.setIcon(new ImageIcon(buttonImage));


}

@Override
protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    g.drawImage(buttonImage, 0, 0, null);
}

@Override
public Dimension getPreferredSize() {
    return new Dimension(buttonImage.getWidth(), buttonImage.getHeight());
}
}
