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
    PLACENAME(new int[] {99,100,101,102}); // 100, 101, 102 is for OSM for small cities to big cities
    
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
	
	public static String getRoadTypeName(int roadtype)
	{
		switch (roadtype){
			case 1: return "Highway";
			case 2: return "Highway";
			case 21: return "Highway";
			case 22: return "Highway";
			case 31: return "Highway";
			case 32: return "Highway";
			case 41: return "Highway";
			case 42: return "Highway";
			//
			case 3: return "Secondary road";
			case 4: return "Secondary road";
			case 23: return "Secondary road";
			case 24: return "Secondary road";
			case 33: return "Secondary road";
			case 34: return "Secondary road";
			case 43: return "Secondary road";
			case 44: return "Secondary road";
			//
			case 5: return "Normal road";
			case 25: return "Normal road";
			case 35: return "Normal road";
			case 45: return "Normal road";
			//
			case 6: return "Small road";
			case 10: return "Small road";
			case 26: return "Small road";
			case 46: return "Small road";
			case 48: return "Small road";
			//
			case 8: return "Path";
			case 11: return "Path";
			case 28: return "Path";
			//case 48: return "Path";
			//
			case 80: return "Ferry route";
			
			default: return "Could not find road type";
		}
	}
}
