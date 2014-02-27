/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mapofdenmark.GUIPackage;

import javax.swing.JFrame;

/**
 * Class description:
 *
 * @version 0.1 - changed 27-02-2014
 * @authorNewVersion  Anders Wind - awis@itu.dk
 *
 * @buildDate 27-02-2014
 * @author Anders Wind - awis@itu.dk
 */
public class MainFrame extends JFrame {
    
    private MapComponent drawMapComponent;
    
    public MainFrame()
    {
		initialize();        
    }
    
    private void initialize()
    {
		drawMapComponent = new MapComponent();
    }
    
}
