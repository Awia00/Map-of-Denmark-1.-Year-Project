/*
 * This enum represents the different road types.
 */

package database;

/**
 *
 * @author Aleksandar Jonovic
 */

public enum RoadTypeEnum {
    
    
    HIGHWAY(new int[] {1,2,21,22,31,32,41,42}),
    SECONDARYROAD(new int[] {3,4,23,24,33,34,43,44}),
    NORMALROAD(new int[] {5,25,35,45}),
    SMALLROAD(new int[] {6,10,26,46,48}),
    PATHWAY(new int[] {8,11,28,48}),
    COASTLINE(new int[]{74,75,76}),
    FERRYWAY(new int[] {80}),
    PLACENAME(new int[] {99,100,101}); // 100 and 101 is for OSM for small cities and big cities
    
    private final int[] types;
    
    
    RoadTypeEnum(int[] types){
        this.types = types;
    }
    
    public int[] getTypes(){
        return types;
    }
    
    public Boolean checkType(int type){
        
        for(int i=0; i<types.length;i++){
            if (type == types[i]){
                return true;
            }
        }
        return false;
    }
}
