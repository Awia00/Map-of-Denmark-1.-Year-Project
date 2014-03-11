/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package database;

/**
 *
 * @author Christian Grøn
 */
public enum RoadDirectionEnum {
    TF("tf"), 
    FT("ft"), 
    N("n");

    RoadDirectionEnum(String roadDirection)
    {
        this.roadDirectionString = roadDirection;
    }

    private final String roadDirectionString;

    public String getRoadDirection()
    {
        return roadDirectionString;
    }
    
    /**
    * This method returns the RoadDirectionEnum corresponding the roadDirectionString.
    * @param roadDirectionString. The String corresponding to the roadDirection.
    * @return RoadDirectionEnum. The direction of the road.
    * @throws RuntimeException.
    */
    public static RoadDirectionEnum createEnumWithString(String roadDirectionString)
    {
        for(RoadDirectionEnum roadDirectionEnum:RoadDirectionEnum.values())
        {
            if(roadDirectionEnum.getRoadDirection()==roadDirectionString)
            {
                return roadDirectionEnum;
            }
        }
        throw new RuntimeException("Invalid Value");
    }

}