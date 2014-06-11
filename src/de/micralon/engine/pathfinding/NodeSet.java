package de.micralon.engine.pathfinding;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ObjectMap;

public class NodeSet {
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
