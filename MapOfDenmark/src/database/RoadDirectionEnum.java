/*
 * This enum represents the different road directions. The road direction is
 * the direction in which you are allowed to drive.
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