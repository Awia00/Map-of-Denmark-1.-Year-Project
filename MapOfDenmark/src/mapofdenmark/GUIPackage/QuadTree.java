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
import database.RoadTypeEnum;
import java.util.*;

public class QuadTree implements Iterable<QuadTree> {

	private QuadTree NW, NE, SW, SE;
	private List<Edge> allEdges;

	// Individual lists of certain edge types
	private List<Edge> highwayEdges;
	private List<Edge> secondaryEdges;
	private List<Edge> normalEdges;
	private List<Edge> smallEdges;
	private List<Edge> pathEdges;
	private List<Edge> ferryEdges;
	private List<Edge> placeNameEdges;
	private List<Edge> coastLineEdges;
	
  // Origin and dimension of quadtree
	private final double x, y, length;

	private boolean isDrawable = true;
	static Set<QuadTree> quadTrees = new LinkedHashSet<>();

	static Random random = new Random(); // For testing purposes...

  // Static list of innermost trees containing all edges
	private static ArrayList<QuadTree> bottomTrees = new ArrayList<>();

	public QuadTree(List<Edge> edges, double x, double y, double length)
	{
		this.x = x;
		this.y = y;
		this.length = length;
		this.allEdges = edges;
		double h = length / 2;
                
    // Divide when number of edges is above 500
		if (edges.size() > 500)
		{
			List<Edge> NWEdges = new ArrayList<>(),
					NEEdges = new ArrayList<>(),
					SWEdges = new ArrayList<>(),
					SEEdges = new ArrayList<>();
			for (Edge edge : edges)
			{
				double px = edge.getMidX();
				double py = edge.getMidY();
        // Assign edges to corresponding quad leaves
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
      // Instantiate quad leaves recursively
			this.NW = new QuadTree(NWEdges, x, y + h, h);
			this.NE = new QuadTree(NEEdges, x + h, y + h, h);
			this.SW = new QuadTree(SWEdges, x, y, h);
			this.SE = new QuadTree(SEEdges, x + h, y, h);
			this.allEdges = Collections.emptyList();
		} else
		{
      // Assign edges to distinct list if end of recursion is reached
			highwayEdges = new ArrayList<>();
			secondaryEdges = new ArrayList<>();
			normalEdges = new ArrayList<>();
			smallEdges = new ArrayList<>();
			pathEdges = new ArrayList<>();
			ferryEdges = new ArrayList<>();
			placeNameEdges = new ArrayList<>();
			coastLineEdges = new ArrayList<>();
			splitEdgesIntoTypes();
			bottomTrees.add(this);
			// this.points = pointData;
		}
	}
  
  // Sort all edges into their respective lists
	private void splitEdgesIntoTypes()
	{
		for (Edge edge : allEdges)
		{
			for (int roadType : RoadTypeEnum.HIGHWAY.getTypes())
			{
				if (roadType == edge.getRoadType())
				{
					highwayEdges.add(edge);
					break;
				}
			}
			for (int roadType : RoadTypeEnum.SECONDARYROAD.getTypes())
			{
				if (roadType == edge.getRoadType())
				{
					secondaryEdges.add(edge);
					break;
				}
			}
			for (int roadType : RoadTypeEnum.NORMALROAD.getTypes())
			{
				if (roadType == edge.getRoadType())
				{
					normalEdges.add(edge);
					break;
				}
			}
			for (int roadType : RoadTypeEnum.SMALLROAD.getTypes())
			{
				if (roadType == edge.getRoadType())
				{
					smallEdges.add(edge);
					break;
				}
			}
			for (int roadType : RoadTypeEnum.PATHWAY.getTypes())
			{
				if (roadType == edge.getRoadType())
				{
					pathEdges.add(edge);
					break;
				}
			}
			for (int roadType : RoadTypeEnum.FERRYWAY.getTypes())
			{
				if (roadType == edge.getRoadType())
				{
					ferryEdges.add(edge);
					break;
				}
			}
			for (int roadType : RoadTypeEnum.COASTLINE.getTypes())
			{
				if (roadType == edge.getRoadType())
				{
					coastLineEdges.add(edge);
					break;
				}
			}
			for (int roadType : RoadTypeEnum.PLACENAME.getTypes())
			{
				if (roadType == edge.getRoadType())
				{
					boolean addThisEdge = true;
					for(Edge edge2 : placeNameEdges)
					{
						if(edge.getRoadName().equals(edge2.getRoadName())){addThisEdge = false;}
					}
					if (addThisEdge){
						placeNameEdges.add(edge);
					}
					break;
				}
			}
		}
	}
  
  // Check if quadtree is drawable
	public boolean isDrawable()
	{
		return this.isDrawable;
	}

  // Set quadtree drawable
	public void setDrawable(boolean isDrawable)
	{
		this.isDrawable = isDrawable;
	}

  // Get origin of quadtree, x-coordinate
	public double getQuadTreeX()
	{
		return this.x;
	}

  // Get origin of quadtree, y-coordinate
	public double getQuadTreeY()
	{
		return this.y;
	}

  // Get the length of the quadtree
	public double getQuadTreeLength()
	{
		return this.length;
	}
  
  // Get all trees at the bottom
	public static ArrayList<QuadTree> getBottomTrees()
	{
		return bottomTrees;
	}

  // Get immediate childs of quadtree
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

  // Get all edges in quadtree
	public List<Edge> getEdges()
	{
		if (this.allEdges.isEmpty())
		{
			return Collections.emptyList();
		} else
		{
			return this.allEdges;
		}
	}

  // Get edges of certain type
	public List<Edge> getHighwayEdges()
	{
		return highwayEdges;
	}

	public List<Edge> getSecondaryEdges()
	{
		return secondaryEdges;
	}

	public List<Edge> getNormalEdges()
	{
		return normalEdges;
	}

	public List<Edge> getSmallEdges()
	{
		return smallEdges;
	}

	public List<Edge> getPathEdges()
	{
		return pathEdges;
	}

	public List<Edge> getFerryEdges()
	{
		return ferryEdges;
	}

	public List<Edge> getCoastLineEdges()
	{
		return coastLineEdges;
	}

	public List<Edge> getPlaceNameEdges()
	{
		return placeNameEdges;
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

  // Get 
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
        public static void clearQuadTree(){
            bottomTrees.clear();
        }
}
