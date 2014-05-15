/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mapofdenmark.GUIPackage;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

/**
 *
 * @author Christian
 */
public class ColorSchemeMenu extends JMenu {

    private final JMenuItem standardItem, nightItem, colorblindItem;

    public ColorSchemeMenu(final MapComponent drawMapComponent) {
        this.setText("Colorschemes");
        this.setFont(FontLoader.getFontWithSize("Roboto-Light", 14f));

        // create the Stardard menu item
        standardItem = new JMenuItem("Standard");
        standardItem.setFont(FontLoader.getFontWithSize("Roboto-Light", 14f));

        // create the Night menu item
        nightItem = new JMenuItem("Night");
        nightItem.setFont(FontLoader.getFontWithSize("Roboto-Light", 14f));

        // create the Coloblind menu item 
        colorblindItem = new JMenuItem("Colorblind");
        colorblindItem.setFont(FontLoader.getFontWithSize("Roboto-Light", 14f));

        // add the menu items to the Color scheme menu
        this.add(standardItem);
        this.add(nightItem);
        this.add(colorblindItem);
        
        standardItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				drawMapComponent.setColorScheme("Standard");
			}
		});
		nightItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				drawMapComponent.setColorScheme("Night");
			}
		});
		colorblindItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				drawMapComponent.setColorScheme("Colorblind");
			}
		});
    }
}
