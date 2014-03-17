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
    private final String name;
    private final Color backgroundColor;
    private final Color placeNameColor;
    private final Color highwayColor;
    private final Color ferrywayColor;
    private final Color secondaryRoadColor;
    private final Color pathwayColor;
    private final Color roadColor;
    
    public ColorScheme(String name, Color backgroundColor, Color placeNameColor, Color highwayColor, Color ferrywayColor, Color secondaryRoadColor, Color pathwayColor, Color roadColor)
    {
    this.name = name;
    this.backgroundColor = backgroundColor;
    this.placeNameColor = placeNameColor;
    this.highwayColor = highwayColor;
    this.ferrywayColor = ferrywayColor;
    this.secondaryRoadColor = secondaryRoadColor;
    this.pathwayColor = pathwayColor;
    this.roadColor = roadColor;
    }

    public String getName() {
        return name;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public Color getPlaceNameColor() {
        return placeNameColor;
    }

    public Color getHighwayColor() {
        return highwayColor;
    }

    public Color getFerrywayColor() {
        return ferrywayColor;
    }

    public Color getSecondaryRoadColor() {
        return secondaryRoadColor;
    }

    public Color getPathwayColor() {
        return pathwayColor;
    }

    public Color getRoadColor() {
        return roadColor;
    }
    
}
