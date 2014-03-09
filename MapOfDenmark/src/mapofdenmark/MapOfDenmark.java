/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mapofdenmark;

import database.Database;
import database.DatabaseInterface;

/**
 *
 * @author Anders
 */
public class MapOfDenmark {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
       DatabaseInterface db = Database.db();
       
       db.getNodes();
       //db.printNodes();
       db.getEdges();
       
      
    }
    
}
