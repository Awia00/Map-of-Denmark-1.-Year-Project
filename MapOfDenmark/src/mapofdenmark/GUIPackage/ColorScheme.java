/*
 * This class represents a scheme of colors to be assigned to the background 
 * and the different road types. 
 */

package mapofdenmark.GUIPackage;

/**
 *
 * @author Christian Grøn
 */

import java.awt.Color;

public class ColorScheme {
    // The name of the color scheme.
    private final String name;
    // The color of the background. 
    private final Color backgroundColor;
    // The color of the place name.
    private final Color[] highwayColor = new Color[2];
    // The color of the highway.
    private final Color[] secondaryRoadColor = new Color[2];
    // The color of the ferryway.
    private final Color[] normalRoadColor = new Color[2];
    // The color of the secondary road.
    private final Color[] smallRoadColor = new Color[2];
    // The color of the pathway.
    private final Color[] pathwayColor = new Color[2];
    // The color of the road.
    private final Color ferrywayColor;
    // The color of the place name.
    private final Color placeNameColor;
    
    //Constructor for the ColorScheme class. This constructor takes Color 
    //objects as parameters and initializes the color for the background, 
    //highway, secondary road, normal road, small road, pathway, ferryway 
    //as well as setting the name of the ColorScheme. 
    public ColorScheme(String name, Color backgroundColor, Color highwayColor, Color secondaryRoadColor, Color normalRoadColor, Color smallRoadColor, Color pathwayColor, Color ferrywayColor, Color placeNameColor)
    {
    this.name = name;
    this.backgroundColor = backgroundColor;
    this.highwayColor[0] = highwayColor;
    this.secondaryRoadColor[0] = secondaryRoadColor;
    this.normalRoadColor[0] = normalRoadColor;
    this.smallRoadColor[0] = smallRoadColor;
    this.pathwayColor[0] = pathwayColor;
    this.ferrywayColor = ferrywayColor;
    this.placeNameColor = placeNameColor;
    }
	
	public ColorScheme(String name, Color backgroundColor, 
					   Color highwayColor,Color highwayColorBorder, 
					   Color secondaryRoadColor,Color secondaryRoadColorBorder, 
					   Color normalRoadColor, Color normalRoadColorBorder,
					   Color smallRoadColor, Color smallRoadColorBorder,
					   Color pathwayColor, Color pathwayColorBorder,
					   Color ferrywayColor,
					   Color placeNameColor)
    {
    this.name = name;
    this.backgroundColor = backgroundColor;
    this.highwayColor[0] = highwayColor;
	this.highwayColor[1] = highwayColorBorder;
    this.secondaryRoadColor[0] = secondaryRoadColor;
		this.secondaryRoadColor[1] = secondaryRoadColorBorder;
    this.normalRoadColor[0] = normalRoadColor;
		this.normalRoadColor[1] = normalRoadColorBorder;
    this.smallRoadColor[0] = smallRoadColor;
		this.smallRoadColor[1] = smallRoadColorBorder;
    this.pathwayColor[0] = pathwayColor;
		this.pathwayColor[1] = pathwayColorBorder;
    this.ferrywayColor = ferrywayColor;
    this.placeNameColor = placeNameColor;
    }
    
    //This method returns the name of the colorScheme.
    //@return name
    public String getName() {
        return name;
    }
    //This method returns the backgroundColor of the colorScheme.
    //@return backgroundColor
    public Color getBackgroundColor() {
        return backgroundColor;
    }
    //This method returns the highwayColor of the colorScheme.
    //@return highwayColor
    public Color getHighwayColor() {
        return highwayColor[0];
    }
	
	public Color getHighwayBorderColor() {
        return highwayColor[1];
    }
	
    //This method returns the secondaryRoadColor of the colorScheme.
    //@return secondaryRoadColor
    public Color getSecondaryRoadColor() {
        return secondaryRoadColor[0];
    }
	
	public Color getSecondaryRoadBorderColor() {
        return secondaryRoadColor[1];
    }
	
    //This method returns the normalRoadColor of the colorScheme.
    //@return normalRoadColor
    public Color getNormalRoadColor() {
        return normalRoadColor[0];
    }
	
	public Color getNormalRoadBorderColor() {
        return normalRoadColor[1];
    }
	
    //This method returns the smallRoadColor of the colorScheme.
    //@return smallRoadColor
    public Color getSmallRoadColor() {
        return smallRoadColor[0];
    }
	
	public Color getSmallRoadBorderColor() {
        return smallRoadColor[1];
    }
	
    //This method returns the pathwayColor of the colorScheme.
    //@return pathwayColor
    public Color getPathwayColor() {
        return pathwayColor[0];
    }
	
	public Color getPathwayBorderColor() {
        return pathwayColor[1];
    }
	
    //This method returns the ferrywayColor of the colorScheme.
    //@return ferrywayColor
    public Color getFerrywayColor() {
        return ferrywayColor;
    }
    //This method returns the placeNameColor of the colorScheme.
    //@return placeNameColor
    public Color getPlaceNameColor() {
        return placeNameColor;
    }

}
