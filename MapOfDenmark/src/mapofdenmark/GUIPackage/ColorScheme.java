/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
    // The color of the place name.'
    private final Color placeNameColor;
    
    //Constructor for the ColorScheme class. This constructor takes Color 
    //objects as parameters and initializes the color for the background,
    //placeName, highway, ferryway, secondaryRoad, pathway and road as well as
    //setting the name of the ColorScheme. 
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

    public String getName() {
        return name;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public Color getHighwayColor() {
        return highwayColor;
    }

    public Color getSecondaryRoadColor() {
        return secondaryRoadColor;
    }

    public Color getNormalRoadColor() {
        return normalRoadColor;
    }

    public Color getSmallRoadColor() {
        return smallRoadColor;
    }

    public Color getPathwayColor() {
        return pathwayColor;
    }

    public Color getFerrywayColor() {
        return ferrywayColor;
    }

    public Color getPlaceNameColor() {
        return placeNameColor;
    }

}
