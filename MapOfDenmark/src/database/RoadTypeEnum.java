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
public enum RoadTypeEnum {
    HIGHWAY(1), 
    MAINROAD(2), 
    PATH(3), 
    OTHER(4);

    RoadTypeEnum(int roadTypeNumber)
    {
        this.roadTypeNumber = roadTypeNumber;
    }
    
    private final int roadTypeNumber;
    
    public int getRoadTypeNumber()
    {
        return roadTypeNumber;
    }
    /**
    * This method returns the RoadTypeEnum corresponding the roadTypeNumber.
    * @param roadTypeNumber. The number corresponding to the roadtype.
    * @return RoadTypeEnum. The roadtype.
    * @throws RuntimeException.
    */
    public static RoadTypeEnum createEnumWithInt(int roadTypeNumber)
    {
        for(RoadTypeEnum roadTypeEnum:RoadTypeEnum.values())
        {
            if(roadTypeEnum.getRoadTypeNumber()==roadTypeNumber)
            {
                return roadTypeEnum;
            }
        }
        throw new RuntimeException("Invalid Value");
    }
}
