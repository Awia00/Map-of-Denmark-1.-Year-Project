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
		Edge edge = edges.get(Collections.binarySearch(edges, new Edge(null, null, 0, address, 0, 0), nameComparator));
		return edge;
	}
	
	private String cleanString(String string)
	{
		return string;
	}
	
	
}
