/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mapofdenmark.GUIPackage;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author Christian
 */
public class FontLoader {
 
    public FontLoader(String fontName) {
           
    }
    
    public static Font getFontWithSize(String fontName, float size) {
        Font customFont = null;
        try {
                        //create the font to use. Specify the size!
                       customFont = Font.createFont(Font.TRUETYPE_FONT, new File("assets/" + fontName + ".ttf")).deriveFont(size);
                        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                        //register the font
                        ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("assets/" + fontName + ".ttf")));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    catch(FontFormatException e)
                    {
                        e.printStackTrace();
                    }
        return customFont;
    }
}
