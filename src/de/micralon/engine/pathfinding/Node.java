package de.micralon.engine.pathfinding;

import com.badlogic.gdx.math.Vector2;

/**
 * A single node in the search graph
 */
public class Node implements Comparable<Node> {
	/** The position of the node */
	public final Vector2 pos;
	/** The path cost for this node */
	public float cost;
	/** The parent of this node, how we reached it in the search */
	public Node parent;
	/** The heuristic cost of this node */
	public float heuristic;
	/** The search depth of this node */
	public int depth;
	
	/**
	 * Create a new node
	 * 
	 * @param x The x coordinate of the node
	 * @param y The y coordinate of the node
	 */
	public Node(float x, float y) {
		this(new Vector2(x, y));
	}
	
	public Node(Vector2 pos) {
		this.pos = pos;
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
			
			return (o.pos.x == pos.x) && (o.pos.y == pos.y);
		}
		
		return false;
	}
	
	public String toString() {
		return "("+pos.x+","+pos.y+")";
	}
}
