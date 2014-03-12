/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package database;

/**
 *
 * @author Alex
 */

public enum RoadTypes {
    
    
    ROAD(new int[] {1,2,3,4,5}),
    HIGHWAY(new int[] {1,2,3,4,5}),
    PATHWAY(new int[] {1,2,3,4,5});
    
    private final int[] types;
    
    RoadTypes(int[] types){
        this.types = types;
    }
    
    public int[] getTypes(){
        return types;
    }
    
}
