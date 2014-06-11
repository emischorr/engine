package de.micralon.engine.ai;

import java.util.ArrayList;
import java.util.Collections;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

import de.micralon.engine.ai.Path.Step;
import de.micralon.engine.gameobjects.GameObject;
import de.micralon.engine.map.GameMap;
import de.micralon.engine.utils.EngineQueue;

/**
 * 
 * A path finder implementation that uses the AStar heuristic based algorithm
 * to determine a path. 
 * 
 * @author Enrico Mischorr
 */
public class AStarPathFinder {
	/** The set of nodes that have been searched through */
	private Array<Node> closed = new Array<Node>();
	/** The set of nodes that we do not yet consider fully searched */
	private SortedList open = new SortedList();
	
	/** The maximum depth of search we're willing to accept before giving up */
	private int maxSearchDistance;
	
	/** The complete set of nodes across the map */
	private NodeSet nodes;

	/**
	 * Create a path finder 
	 * 
	 * @param gameMap The map to be searched
	 * @param maxSearchDistance The maximum depth we'll search before giving up
	 */
	public AStarPathFinder(GameMap gameMap, int maxSearchDistance) {
		this.maxSearchDistance = maxSearchDistance;
		
		nodes = new NodeSet(gameMap.width*gameMap.height);
		for (int x=0; x < gameMap.width; x++) {
			for (int y=0; y < gameMap.height; y++) {
				nodes.add(x, y, new Node(x,y));
			}
		}
	}
	
	public AStarPathFinder(NodeSet nodes, int maxSearchDistance) {
		this.maxSearchDistance = maxSearchDistance;
		this.nodes = nodes;
	}
	
	public Path findPath(Unit mover, int tx, int ty, boolean ignoreRange) {
		return findPath(mover.getX(), mover.getY(), tx, ty, mover, ignoreRange);
	}
	
	/**
	 * @see PathFinder#findPath(Vector2, Vector2, Movable)
	 */
	public Path findPath(Vector2 source, Vector2 target, boolean ignoreRange) {
		// easy first check, if the destination is blocked, we can't get there
		if (isCellBlocked(target, mover) && !ignoreRange) {
			return null;
		}
		
		// initial state for A*. The closed group is empty. Only the starting
		// tile is in the open list and it's cost is zero, i.e. we're already there
		nodes.get(source).cost = 0;
		nodes.get(source).depth = 0;
		closed.clear();
		open.clear();
		open.add(nodes.get(source));
		
		nodes.get(target).parent = null;
		
		// While we still have more nodes to search and haven't exceeded our max search depth
		int maxDepth = 0;
		while ((maxDepth < maxSearchDistance) && (open.size() != 0)) {
			// pull out the first node in our open list, this is determined to 
			// be the most likely to be the next step based on our heuristic
			Node current = getFirstInOpen();
			if (current == nodes.get(target)) {
				break;
			}
			
			removeFromOpen(current);
			addToClosed(current);
			
			Array<Vector2> neighbors = MapTools.getNeighbours(current.x, current.y);
			// search through all the neighbors of the current node evaluating
			// them as next steps
			for (Vector2 n : neighbors) {
				int xp = (int) n.x;
				int yp = (int) n.y;
				float nextStepCost = current.cost + getMovementCost(current.x,current.y,xp,yp, mover);
				Node neighbor = nodes.get(n);
				
				// If this step exceeds the movers energy, don't even bother with it
				if ((nextStepCost > mover.getEnergy() && !ignoreRange)) continue;
				
				// Check to see if we have found a new shortest route to this neighbor
				if (nextStepCost < neighbor.cost) {
					if (inOpenList(neighbor)) removeFromOpen(neighbor);
					if (inClosedList(neighbor)) removeFromClosed(neighbor);
				}
				
				// If it was a new shor
				if (!inOpenList(neighbor) && !inClosedList(neighbor)) {
					neighbor.cost = nextStepCost;
					neighbor.heuristic = (float)MapTools.distance(xp, yp, tx, ty);
					maxDepth = Math.max(maxDepth, neighbor.setParent(current));
					addToOpen(neighbor);
				}
			}
		}

		// since we've got an empty open list or we've run out of search 
		// there was no path. Just return null
		if (nodes.get(target).parent == null) {
			return null;
		}
		
		// At this point we've definitely found a path so we can uses the parent
		// references of the nodes to find out way from the target location back
		// to the start recording the nodes on the way.
		Path path = new Path();
		Node destination = nodes.get(target);
		path.setCost((int) destination.cost);
		while (destination != nodes.get(source)) {
			path.prependStep(destination.x, destination.y);
			destination = destination.parent;
		}
		path.prependStep(source.x, source.y);
		
		// thats it, we have our path 
		return path;
	}
	
	public Array<Vector2> getReachableCells(Unit mover) {
		return getReachableCells(mover, mover.getPosition(), mover.getEnergy());
	}
	
	/**
	 * 
	 * @param mover The mover
	 * @param position The position of the mover (may be a other than the units position for AI calculations)
	 * @param energy The energy the mover has left in order to move
	 * @return An Array<Pair> containing the coordinates for all cells the mover can reach
	 */
	public Array<Vector2> getReachableCells(Unit mover, Vector2 position, int energy) {
		int useableEnergy = energy;
		Array<Vector2> reachableCells = new Array<Vector2>();
		EngineQueue<Node> open = new EngineQueue<Node>();
		closed.clear();
		Node start = nodes.get(position);
		start.depth = 0;
		start.cost = 0;
		open.push(start);
		while (open.size() > 0) {
			// poll() the open queue
			Node current = open.poll();
			
			for (Vector2 n : MapTools.getNeighbours(current.x,current.y)) {
				Node neighbor = nodes.get(n);
				float nextStepCost = current.cost + getMovementCost(current.x, current.y, n.x, n.y, mover);
				
				// If the cell is beyond our reach, or otherwise blocked, ignore it
				if (nextStepCost > useableEnergy || isCellBlocked(n.x,n.y,mover)) continue;
				
				// Check to see if we have found a new shortest route to this neighbor, in
				// which case it must be totally reconsidered
				if (nextStepCost < neighbor.cost) {
					if (inClosedList(neighbor)) removeFromClosed(neighbor);
					if (open.contains(neighbor, false)) open.remove(neighbor,false);
				}

				if (!open.contains(neighbor, false) && !inClosedList(neighbor)) {
					neighbor.cost = nextStepCost;
					open.push(neighbor);
				}
			}
			addToClosed(current);
		}
		
		for (Node n : closed) {
			if (n.x != position.x || n.y != position.y) reachableCells.add(new Vector2(n.x,n.y));
		}
		
		return reachableCells;

	}
	
	public int getNumberOfTurns(int sx, int sy, int tx, int ty, Unit mover) {
		Path path = findPath(sx, sy, tx, ty, mover, true);
		if (path == null) return Integer.MAX_VALUE;
		
		int numberOfTurns = 1;
		int energySpent = 0;
		
		for (int i = path.getLength() - 1; i > 0; i--) {
			Step from = path.getStep(i);
			Step to = path.getStep(i-1);
			
			if (energySpent > mover.getEnergy()) {
				energySpent = 0;
				numberOfTurns++;
			}
			energySpent += getMovementCost(from.getX(),from.getY(),to.getX(),to.getY(), mover); 
		}
		
		return numberOfTurns;
	}

	/**
	 * Get the first element from the open list. This is the next
	 * one to be searched.
	 * 
	 * @return The first element in the open list
	 */
	protected Node getFirstInOpen() {
		return (Node) open.first();
	}
	
	/**
	 * Add a node to the open list
	 * 
	 * @param node The node to be added to the open list
	 */
	protected void addToOpen(Node node) {
		open.add(node);
	}
	
	/**
	 * Check if a node is in the open list
	 * 
	 * @param node The node to check for
	 * @return True if the node given is in the open list
	 */
	protected boolean inOpenList(Node node) {
		return open.contains(node);
	}
	
	/**
	 * Remove a node from the open list
	 * 
	 * @param node The node to remove from the open list
	 */
	protected void removeFromOpen(Node node) {
		open.remove(node);
	}
	
	/**
	 * Add a node to the closed list
	 * 
	 * @param node The node to add to the closed list
	 */
	protected void addToClosed(Node node) {
		closed.add(node);
	}
	
	/**
	 * Check if the node supplied is in the closed list
	 * 
	 * @param node The node to search for
	 * @return True if the node specified is in the closed list
	 */
	protected boolean inClosedList(Node node) {
		return closed.contains(node,false);
	}
	
	/**
	 * Remove a node from the closed list
	 * 
	 * @param node The node to remove from the closed list
	 */
	protected void removeFromClosed(Node node) {
		closed.removeValue(node,false);
	}
	
	/**
	 * Check if a given location is valid for the supplied mover
	 * 
	 * @param mover The mover that would hold a given location
	 * @param sx The starting x coordinate
	 * @param sy The starting y coordinate
	 * @param x The x coordinate of the location to check
	 * @param y The y coordinate of the location to check
	 * @return True if the location is valid for the given mover
	 */
	protected boolean isValidLocation(int sx, int sy, int x, int y) {
		boolean invalid = (x < 0) || (y < 0) || (x >= gameWorld.width) || (y >= gameWorld.height);
		
		if ((!invalid) && ((sx != x) || (sy != y))) {
			//invalid = map.blocked(mover, x, y);
		}
		
		return !invalid;
	}
	
	/**
	 * Get the cost to move through a given location
	 * 
	 * @param mover The entity that is being moved
	 * @param sx The x coordinate of the tile whose cost is being determined
	 * @param sy The y coordiante of the tile whose cost is being determined
	 * @param tx The x coordinate of the target location
	 * @param ty The y coordinate of the target location
	 * @return The cost of movement through the given tile
	 */
	public float getMovementCost(int sx, int sy, int tx, int ty, Unit mover) {
		return mover.terrainCost( gameWorld.getMap().getField(tx, ty).getTerrain() );
	}

	private boolean isCellBlocked(int x, int y, Unit mover) {
		return (mover.terrainBlocked(gameWorld.getMap().getField(x, y).getTerrain()) || gameWorld.cellOccupied(x, y));
	}
	
	/**
	 * Get the heuristic cost for the given location. This determines in which 
	 * order the locations are processed.
	 * 
	 * @param mover The entity that is being moved
	 * @param x The x coordinate of the tile whose cost is being determined
	 * @param y The y coordiante of the tile whose cost is being determined
	 * @param tx The x coordinate of the target location
	 * @param ty The y coordinate of the target location
	 * @return The heuristic cost assigned to the tile
	 */
	public float getHeuristicCost(int x, int y, int tx, int ty) {
		return MapTools.distance(x, y, tx, ty);
		//return heuristic.getCost(map, mover, x, y, tx, ty);
	}
	
	/**
	 * A simple sorted list
	 *
	 * @author Enrico Mischorr
	 */
	private class SortedList {
		/** The list of elements */
		private ArrayList<Node> list = new ArrayList<Node>();
		
		/**
		 * Retrieve the first element from the list
		 *  
		 * @return The first element from the list
		 */
		public Object first() {
			return list.get(0);
		}
		
		/**
		 * Empty the list
		 */
		public void clear() {
			list.clear();
		}
		
		/**
		 * Add an element to the list - causes sorting
		 * 
		 * @param o The element to add
		 */
		public void add(Node o) {
			list.add(o);
			Collections.sort(list);
		}
		
		/**
		 * Remove an element from the list
		 * 
		 * @param o The element to remove
		 */
		public void remove(Object o) {
			list.remove(o);
		}
	
		/**
		 * Get the number of elements in the list
		 * 
		 * @return The number of element in the list
 		 */
		public int size() {
			return list.size();
		}
		
		/**
		 * Check if an element is in the list
		 * 
		 * @param o The element to search for
		 * @return True if the element is in the list
		 */
		public boolean contains(Object o) {
			return list.contains(o);
		}
	}
	
	private class NodeSet {
		private ObjectMap<Vector2, Node> nodes;
		
		public NodeSet(int initialCapacity) {
			nodes = new ObjectMap<Vector2, Node>(initialCapacity, 1);
		}
		
		public Node get(Vector2 index) {
			return nodes.get(index);
		}
		
		public void add(float x, float y, Node node) {
			add(new Vector2(x,y), node);
		}
		
		public void add(Vector2 index, Node node) {
			nodes.put(index, node);
		}
	}
	
	/**
	 * A single node in the search graph
	 */
	private class Node implements Comparable<Node> {
		/** The x coordinate of the node */
		private float x;
		/** The y coordinate of the node */
		private float y;
		/** The path cost for this node */
		private float cost;
		/** The parent of this node, how we reached it in the search */
		private Node parent;
		/** The heuristic cost of this node */
		private float heuristic;
		/** The search depth of this node */
		private int depth;
		
		/**
		 * Create a new node
		 * 
		 * @param x The x coordinate of the node
		 * @param y The y coordinate of the node
		 */
		public Node(float x, float y) {
			this.x = x;
			this.y = y;
		}
		
		/**
		 * Set the parent of this node
		 * 
		 * @param parent The parent node which lead us to this node
		 * @return The depth we have no reached in searching
		 */
		public int setParent(Node parent) {
			depth = parent.depth + 1;
			this.parent = parent;
			
			return depth;
		}
		
		/**
		 * @see Comparable#compareTo(Object)
		 */
		@Override
		public int compareTo(Node o) {
			//Node o = (Node) other;
			
			float f = heuristic + cost;
			float of = o.heuristic + o.cost;
			
			if (f < of) {
				return -1;
			} else if (f > of) {
				return 1;
			} else {
				return 0;
			}
		}
		
		/**
		 * @see Object#equals(Object)
		 */
		public boolean equals(Object other) {
			if (other instanceof Node) {
				Node o = (Node) other;
				
				return (o.x == x) && (o.y == y);
			}
			
			return false;
		}
		
		public String toString() {
			return "("+x+","+y+")";
		}
	}
}
