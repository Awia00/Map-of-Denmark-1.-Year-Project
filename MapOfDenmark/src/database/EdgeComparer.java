/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import java.util.Comparator;

/**
 *
 * @author Alex
 */
public class EdgeComparer implements Comparator<Edge>{

    @Override
    public int compare(Edge o1, Edge o2) {
        return Integer.compare(o1.getRoadcode(), o2.getRoadcode());
    }
}
