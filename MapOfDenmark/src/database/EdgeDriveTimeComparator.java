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
public class EdgeDriveTimeComparator implements Comparator<Edge>{

    @Override
    public int compare(Edge o1, Edge o2) {
        if (o1.getWeight()> o2.getWeight()) {
				return 1;
			}

			else if (o1.getWeight() == o2.getWeight()) {
				return 0;
			}

			else {
				return -1;
			}
    }
}
