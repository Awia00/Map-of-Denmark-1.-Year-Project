/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import java.util.ArrayList;

/**
 *
 * @author Anders
 */
public class Street implements Comparable<Street>{

	private String name = "";
	private final int ID;
	private ArrayList<Edge> edges = new ArrayList<>();

	public Street(int ID, String name)
	{
		this.ID = ID;
                this.name = name;
	}

	/**
	 * Returns the name of the street if it has one, otherwise asks the database
	 * for the name.
	 *
	 * @return
	 */
	public String getStreetName()
	{
		if (!name.equals(""))
		{
			return name;
		} else
		{
			// ask the database for the street name
			return "";
		}
	}

	public int getID()
	{
		return ID;
	}

	public ArrayList<Edge> getEdges()
	{
		return edges;
	}
        public void addEdge(Edge e){
            edges.add(e);
        }
        
        @Override
        public String toString(){
            String edgeString = "";
            //System.out.println("NEW ROAD HERE ---------------------------------------------------");
            for(Edge edge : edges)
            {
                edgeString += edge.toString();
            }
            return "VEJKODE: " + ID + " VEJNAVN: " + name + edgeString;
        }

    @Override
    public int compareTo(Street o) {
        return this.ID - o.ID;
    }
        
        
}
