/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DataStructure;

import java.util.Comparator;

/**
 *
 * @author Alex
 */
public class StreetComparer implements Comparator<Street>{

    @Override
    public int compare(Street o1, Street o2) {
        return Integer.compare(o1.getID(), o2.getID());
    }
    
}
