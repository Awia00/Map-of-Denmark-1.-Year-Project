/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import java.util.Comparator;

/**
 *
 * @author Anders & Alsay
 */
public class EdgeDistanceComparator implements Comparator<Edge>{

    @Override
    public int compare(Edge o1, Edge o2) {
        if (o1.getLength()> o2.getLength()) {
				return 1;
			}

			else if (o1.getLength() == o2.getLength()) {
				return 0;
			}

			else {
				return -1;
			}
    }
}
