/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package database;

/**
 *
 * @author Aleksandar Jonovic
 */

public enum RoadTypeEnum {
    
    
    ROAD(new int[] {3,4,5,6,5,10,23,24,25,26,33,34,35,43,44,45,46,48}),
    HIGHWAY(new int[] {1,2,21,22,31,32,41,42}),
    PATHWAY(new int[] {8,11,28,48}),
    FERRYWAY(new int[] {80}),
    PLACENAME(new int[] {99});
    
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
