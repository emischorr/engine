package de.micralon.engine.pathfinding;

import java.util.ArrayList;
import java.util.Collections;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import de.micralon.engine.GameWorld;
import de.micralon.engine.map.GameMap;

/**
 * 
 * A path finder implementation that uses the AStar heuristic based algorithm
 * to determine a path. 
 * 
 * @author Enrico Mischorr
 */
public class AStarPathFinder implements Pathfinder {
	/** The set of nodes that have been searched through */
	private Array<Node> closed = new Array<Node>();
	/** The set of nodes that we do not yet consider fully searched */
	private SortedList open = new SortedList();
	
	/** The maximum depth of search we're willing to accept before giving up */
	private int maxSearchDistance;
	
	/** The complete set of nodes across the map */
	private final NodeSet nodes;
	
	private Pathfindable rules;
	private Heuristic heuristic = new ClosestHeuristic();

	/**
	 * Create a path finder 
	 * 
	 * @param gameMap The map to be searched
	 * @param maxSearchDistance The maximum depth we'll search before giving up
	 */
	public AStarPathFinder(GameWorld gameWorld, int maxSearchDistance) {
		this(gameWorld, nodeSetFromMap(gameWorld.map), maxSearchDistance);
	}
	
	public AStarPathFinder(Pathfindable rules, Array<Vector2> points, int maxSearchDistance) {
		this(rules, nodeSetFromPoints(points), maxSearchDistance);
	}
	
	public AStarPathFinder(Pathfindable rules, NodeSet nodes, int maxSearchDistance) {
		this.rules = rules;
		this.nodes = nodes;
		this.maxSearchDistance = maxSearchDistance;
	}
	
	public static NodeSet nodeSetFromMap(GameMap gameMap) {
		NodeSet nodes = new NodeSet(gameMap.getWidth()*gameMap.getHeight());
		for (int x=0; x < gameMap.getWidth(); x++) {
			for (int y=0; y < gameMap.getHeight(); y++) {
				nodes.add(new Node(x,y));
			}
		}
		return nodes;
	}
	
	public static NodeSet nodeSetFromPoints(Array<Vector2> points) {
		NodeSet nodes = new NodeSet(points.size);
		for (Vector2 point : points) {
			nodes.add(new Node(point));
		}
		return nodes;
	}
	
	public void setHeuristic(Heuristic heuristic) {
		this.heuristic = heuristic;
	}
	
	@Override
	public Path findPath(Movable mover, Vector2 source, Vector2 target) {
		// easy first check, if the destination is blocked, we can't get there
		if (rules.isBlocked(target, mover)) {
			return null;
		}
		
		Node sourceNode = nodes.get(source);
		Node targetNode = nodes.get(target);
		// set source and target if not in node set (if node set consists of waypoints)
		if (sourceNode == null) {
			sourceNode = new Node(source);
		}
		if (targetNode == null) {
			targetNode = new Node(target);
		}
				
		// initial state for A*. The closed group is empty. Only the starting
		// tile is in the open list and it's cost is zero, i.e. we're already there
		sourceNode.cost = 0;
		sourceNode.depth = 0;
		closed.clear();
		open.clear();
		open.add(sourceNode);
		
		targetNode.parent = null;
		
		// While we still have more nodes to search and haven't exceeded our max search depth
		int maxDepth = 0;
		while ((maxDepth < maxSearchDistance) && (open.size() != 0)) {
			// pull out the first node in our open list, this is determined to 
			// be the most likely to be the next step based on our heuristic
			Node current = getFirstInOpen();
			if (current == targetNode) {
				break;
			}
			
			removeFromOpen(current);
			addToClosed(current);
			
			Array<Vector2> neighborsPos = rules.reachablePositions(current.pos, target);
			// search through all the neighbors of the current node evaluating
			// them as next steps
			for (Vector2 npos : neighborsPos) {
				Node n = nodes.get(npos);
				if (n == null && npos.equals(target)) { // node was not found among the node set because it's the target (waypoint)
					n = targetNode;
				}
				float nextStepCost = current.cost + rules.getMovementCost(current.pos, n.pos, mover);
				
				// If this step exceeds the movers energy, don't even bother with it
				//if (nextStepCost > mover.getEnergy() && !ignoreRange) continue;
				if (rules.toExpensive(mover, nextStepCost)) continue;
				
				// Check to see if we have found a new shortest route to this neighbor
				if (nextStepCost < n.cost) {
					if (inOpenList(n)) removeFromOpen(n);
					if (inClosedList(n)) removeFromClosed(n);
				}
				
				// If it was a new shor
				if (!inOpenList(n) && !inClosedList(n)) {
					n.cost = nextStepCost;
					n.heuristic = heuristic.getCost(mover, n.pos, target);
					maxDepth = Math.max(maxDepth, n.setParent(current));
					addToOpen(n);
				}
			}
		}

		// since we've got an empty open list or we've run out of search 
		// there was no path. Just return null
		if (targetNode.parent == null) {
			return null;
		}
		
		// At this point we've definitely found a path so we can uses the parent
		// references of the nodes to find out way from the target location back
		// to the start recording the nodes on the way.
		Path path = new Path();
		Node destination = targetNode;
		path.setCost((int) destination.cost);
		while (destination != sourceNode) {
			path.prependStep(destination.pos.x, destination.pos.y);
			destination = destination.parent;
		}
		// prepend the source to the path
		//path.prependStep(source.x, source.y);
		
		// thats it, we have our path 
		return path;
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
	public float getHeuristicCost(Movable mover, Vector2 nodePos, Vector2 target) {
		//return MapTools.distance(x, y, tx, ty);
		//return heuristic.getCost(map, mover, x, y, tx, ty);
		return heuristic.getCost(mover, nodePos, target);
	}
	
//	public Array<Vector2> getReachableCells(Unit mover) {
//		return getReachableCells(mover, mover.getPosition(), mover.getEnergy());
//	}
//	
//	/**
//	 * 
//	 * @param mover The mover
//	 * @param position The position of the mover (may be a other than the units position for AI calculations)
//	 * @param energy The energy the mover has left in order to move
//	 * @return An Array<Pair> containing the coordinates for all cells the mover can reach
//	 */
//	public Array<Vector2> getReachableCells(Unit mover, Vector2 position, int energy) {
//		int useableEnergy = energy;
//		Array<Vector2> reachableCells = new Array<Vector2>();
//		EngineQueue<Node> open = new EngineQueue<Node>();
//		closed.clear();
//		Node start = nodes.get(position);
//		start.depth = 0;
//		start.cost = 0;
//		open.push(start);
//		while (open.size() > 0) {
//			// poll() the open queue
//			Node current = open.poll();
//			
//			for (Vector2 n : MapTools.getNeighbours(current.x,current.y)) {
//				Node neighbor = nodes.get(n);
//				float nextStepCost = current.cost + getMovementCost(current.x, current.y, n.x, n.y, mover);
//				
//				// If the cell is beyond our reach, or otherwise blocked, ignore it
//				if (nextStepCost > useableEnergy || isNodeBlocked(n.x,n.y,mover)) continue;
//				
//				// Check to see if we have found a new shortest route to this neighbor, in
//				// which case it must be totally reconsidered
//				if (nextStepCost < neighbor.cost) {
//					if (inClosedList(neighbor)) removeFromClosed(neighbor);
//					if (open.contains(neighbor, false)) open.remove(neighbor,false);
//				}
//
//				if (!open.contains(neighbor, false) && !inClosedList(neighbor)) {
//					neighbor.cost = nextStepCost;
//					open.push(neighbor);
//				}
//			}
//			addToClosed(current);
//		}
//		
//		for (Node n : closed) {
//			if (n.x != position.x || n.y != position.y) reachableCells.add(new Vector2(n.x,n.y));
//		}
//		
//		return reachableCells;
//
//	}
	
//	public int getNumberOfTurns(int sx, int sy, int tx, int ty, Unit mover) {
//		Path path = findPath(sx, sy, tx, ty, mover, true);
//		if (path == null) return Integer.MAX_VALUE;
//		
//		int numberOfTurns = 1;
//		int energySpent = 0;
//		
//		for (int i = path.getLength() - 1; i > 0; i--) {
//			Step from = path.getStep(i);
//			Step to = path.getStep(i-1);
//			
//			if (energySpent > mover.getEnergy()) {
//				energySpent = 0;
//				numberOfTurns++;
//			}
//			energySpent += getMovementCost(from.getX(),from.getY(),to.getX(),to.getY(), mover); 
//		}
//		
//		return numberOfTurns;
//	}

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
}
