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
    private final Color highwayColor;
    // The color of the highway.
    private final Color secondaryRoadColor;
    // The color of the ferryway.
    private final Color normalRoadColor;
    // The color of the secondary road.
    private final Color smallRoadColor;
    // The color of the pathway.
    private final Color pathwayColor;
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
    this.highwayColor = highwayColor;
    this.secondaryRoadColor = secondaryRoadColor;
    this.normalRoadColor = normalRoadColor;
    this.smallRoadColor = smallRoadColor;
    this.pathwayColor = pathwayColor;
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
        return highwayColor;
    }
    //This method returns the secondaryRoadColor of the colorScheme.
    //@return secondaryRoadColor
    public Color getSecondaryRoadColor() {
        return secondaryRoadColor;
    }
    //This method returns the normalRoadColor of the colorScheme.
    //@return normalRoadColor
    public Color getNormalRoadColor() {
        return normalRoadColor;
    }
    //This method returns the smallRoadColor of the colorScheme.
    //@return smallRoadColor
    public Color getSmallRoadColor() {
        return smallRoadColor;
    }
    //This method returns the pathwayColor of the colorScheme.
    //@return pathwayColor
    public Color getPathwayColor() {
        return pathwayColor;
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
