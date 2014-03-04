/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

/**
 *
 * @author Anders
 */
public class Street {

	private String name = "";
	private final int ID;
	private final Edge[] edges;

	public Street(int ID, Edge[] edges)
	{
		this.ID = ID;
		this.edges = edges;
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

	public Edge[] getEdges()
	{
		return edges;
	}
}
