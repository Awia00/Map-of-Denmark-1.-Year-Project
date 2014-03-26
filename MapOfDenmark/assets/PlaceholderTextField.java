// From http://stackoverflow.com/questions/16213836/java-swing-jtextfield-set-placeholder


import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.awt.Font;
/**
 * Created by Nikolaj on 09-12-13.
 *
 * Class used to add placeholder labels into textfields for a more nice and cleaner interface
 *
 */

class PlaceholderTextField extends JTextField {

static Font customFont; 
  //Example use
  public static void main(final String[] args) {
    final PlaceholderTextField tf = new PlaceholderTextField("");
    tf.setColumns(20);
    tf.setPlaceholder("All your base are belong to us!");
    final Font f = new Font("Roboto-Bold", Font.PLAIN, 13);
    tf.setFont(customFont);
    JOptionPane.showMessageDialog(null, tf);
  }

  private String placeholder;

  public PlaceholderTextField(final String pText) {
    super(pText);
    

   try {
                        //create the font to use. Specify the size!
                       customFont = Font.createFont(Font.TRUETYPE_FONT, new File("Roboto-Bold.ttf")).deriveFont(12f);
                        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                        //register the font
                        ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("Roboto-Bold.ttf")));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    catch(FontFormatException e)
                    {
                        e.printStackTrace();
                    }
  }

  @Override
  protected void paintComponent(final Graphics pG) {
    super.paintComponent(pG);

    if (placeholder.length() == 0 || getText().length() > 0) {
      return;
    }

    final Graphics2D g = (Graphics2D) pG;
    g.setRenderingHint(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON);
    g.setColor(getDisabledTextColor());
    g.drawString(placeholder, getInsets().left, pG.getFontMetrics()
            .getMaxAscent() + getInsets().top);
  }

  public void setPlaceholder(final String s) {
    placeholder = s;
  }




}

