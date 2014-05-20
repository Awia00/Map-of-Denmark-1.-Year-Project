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
public class NodeComparer implements Comparator<Node>{
        @Override
    public int compare(Node o1, Node o2) {
        Node n1 = o1;
        Node n2 = o2;
        return n1.getID() - n2.getID();
    }
    
}
