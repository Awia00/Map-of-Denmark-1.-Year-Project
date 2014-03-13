/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mapofdenmark.GUIPackage;

/**
 *
 * @author Christian
 */
import database.Edge;
import database.Node;
import java.util.*;
import java.awt.geom.Point2D;

public class QuadTree implements Iterable<QuadTree> {

	private QuadTree NW, NE, SW, SE;
	private List<Edge> edges;
	private final double x, y, length;
	private boolean isDrawable = true;
	static Set<QuadTree> quadTrees = new LinkedHashSet<>();
	static Random random = new Random(); // For testing purposes...
	private static ArrayList<QuadTree> bottomTrees = new ArrayList<>();

	public QuadTree(List<Edge> edges, double x, double y, double length)
	{
		this.x = x;
		this.y = y;
		this.length = length;
		this.edges = edges;
		double h = length / 2;

		if (edges.size() > 500)
		{
			List<Edge> NWEdges = new ArrayList<>(),
					NEEdges = new ArrayList<>(),
					SWEdges = new ArrayList<>(),
					SEEdges = new ArrayList<>();
			for (Edge edge : edges)
			{
				double px = edge.getMidNodeTrue().getxCoord();
				double py = edge.getMidNodeTrue().getyCoord();
				if (px >= x && px < x + h && py > y + h && py <= y + length)
				{
					NWEdges.add(edge);
				}
				if (px > x + h && px <= x + length && py > y + h && py <= y + length)
				{
					NEEdges.add(edge);
				}
				if (px >= x && px < x + h && py >= y && py < y + h)
				{
					SWEdges.add(edge);
				}
				if (px >= x + h && px <= x + length && py >= y && py <= y + h)
				{
					SEEdges.add(edge);
				}
			}
			this.NW = new QuadTree(NWEdges, x, y + h, h);
			this.NE = new QuadTree(NEEdges, x + h, y + h, h);
			this.SW = new QuadTree(SWEdges, x, y, h);
			this.SE = new QuadTree(SEEdges, x + h, y, h);
			this.edges = Collections.emptyList();
		} else
		{
			bottomTrees.add(this);
			// this.points = pointData;
		}
	}

	public boolean isDrawable()
	{
		return this.isDrawable;
	}

	public void setDrawable(boolean isDrawable)
	{
		this.isDrawable = isDrawable;
	}

	public double getQuadTreeX()
	{
		return this.x;
	}

	public static ArrayList<QuadTree> getBottomTrees()
	{
		return bottomTrees;
	}

	public double getQuadTreeY()
	{
		return this.y;
	}

	public double getQuadTreeLength()
	{
		return this.length;
	}

	public QuadTree[] getQuadTrees()
	{
		if (NW == null && NE == null && SW == null && SE == null)
		{
			return new QuadTree[0];
		} else
		{
			return new QuadTree[]
			{
				NW, NE, SW, SE
			};
		}
	}

	public List<Edge> getEdges()
	{
		if (this.edges.isEmpty())
		{
			return Collections.emptyList();
		} else
		{
			return this.edges;
		}
	}

	// Iterator iterates through trees within current trees if any
	private class TreeIterator implements Iterator<QuadTree> {

		QuadTree[] trees = getQuadTrees();
		private int i = 0;

		public boolean hasNext()
		{
			return i < trees.length;
		}

		public void remove()
		{
			throw new UnsupportedOperationException();
		}

		public QuadTree next()
		{
			return trees[i++];
		}
	}

	public Iterator<QuadTree> iterator()
	{
		return new TreeIterator();
	}

	// Print all trees and their associated points - for testing purposes
	public void printTrees(QuadTree tree)
	{
    // Include parent tree
		// System.out.println(tree);  
		// for (Point2D p : tree.getEdges()) {
		//   System.out.println(p.getX() + ", " + p.getY());
		//}
		// Child trees
		for (QuadTree t : tree)
		{
			System.out.println(t);
      //for (Point2D p : t.getEdges()) {
			//System.out.println(p.getX() + ", " + p.getY());
			//}
			t.printTrees(t);
		}
	}

	public Set<QuadTree> getChildQuadTrees(QuadTree tree)
	{
		this.quadTrees.add(tree);
		for (QuadTree t : tree)
		{
			//System.out.println("recursion");
			this.quadTrees.add(t);
			t.getChildQuadTrees(t);
		}
		return quadTrees;
	}

	// For testing purposes...
	public static double randomInRange(double min, double max)
	{
		double range = max - min;
		double scaled = random.nextDouble() * range;
		double shifted = scaled + min;
		return shifted; // == (rand.nextDouble() * (max-min)) + min;
	}

	public QuadTree getNW()
	{
		return NW;
	}

	public QuadTree getNE()
	{
		return NE;
	}

	public QuadTree getSW()
	{
		return SW;
	}

	public QuadTree getSE()
	{
		return SE;
	}

	// Class testing
	public static void main(String[] args)
	{
		List<Edge> edges = new ArrayList<>();
		for (int i = 0; i < 35; i++)
		{
			Edge edge = new Edge(0, 0, 0, null, 0);
			edge.setFromNodeTrue(new Node(new Point2D.Double(QuadTree.randomInRange(0, 199), QuadTree.randomInRange(0, 199))));
			edge.setToNodeTrue(new Node(new Point2D.Double(QuadTree.randomInRange(0, 199), QuadTree.randomInRange(0, 199))));
			edge.setMidNodeTrue();
			edges.add(edge);
		}
		QuadTree q = new QuadTree(edges, 0, 0, 200);

		//q.printTrees(q);
		Set<QuadTree> qt = new LinkedHashSet<>();
		qt = q.getChildQuadTrees(q);
		System.out.println(qt.size());
		for (QuadTree t : qt)
		{
			System.out.println(t);
			System.out.println("Tree x: " + t.getQuadTreeX() + " y: " + t.getQuadTreeY() + " length: " + t.getQuadTreeLength());
			System.out.println("Tree has: " + t.getEdges().size() + " points");

			for (Edge edge : t.getEdges())
			{
				System.out.println(edge.getMidNodeTrue().getxCoord() + ", " + edge.getMidNodeTrue().getyCoord());
			}
			System.out.println("\n####\n");
		}
	}
}
