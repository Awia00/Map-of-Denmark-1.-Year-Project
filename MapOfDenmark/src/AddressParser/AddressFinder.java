/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package AddressParser;

import database.Edge;
import database.EdgeComparerName;
import java.util.Collections;
import java.util.List;

/**
 * Class description:
 *
 * @version 0.1 - changed 19-05-2014
 * @authorNewVersion  Anders Wind - awis@itu.dk
 *
 * @buildDate 19-05-2014
 * @author Anders Wind - awis@itu.dk
 */
public class AddressFinder {
	List<Edge> edges;
	EdgeComparerName nameComparator;

	public AddressFinder(List<Edge> edges)
	{
		this.edges = edges;
		nameComparator = new EdgeComparerName();
		Collections.sort(edges, nameComparator);
	}
	
	public Edge searchForEdge(String input)
	{
		String address = cleanString(input);
		String tempString = address;
		int index = 0;
		while(tempString.length()>0)
		{
			index = Collections.binarySearch(edges, new Edge(tempString), nameComparator);
			if(index >=0)
			{
				break;
			}
			tempString = tempString.substring(0,tempString.length()-1);
		}
		if (index>=0 && index < edges.size())
		{
			Edge edge = edges.get(index);
			if (edge.getRoadName().trim().equals("")) return null;
			return edge;
		}
		return null;
	}
	
	private String cleanString(String string)
	{
		return string.replaceAll(" ", "");
	}
	
	
}
