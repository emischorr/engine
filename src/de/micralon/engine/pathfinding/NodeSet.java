package de.micralon.engine.pathfinding;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ObjectMap;

public class NodeSet {
	private ObjectMap<String, Node> nodes;
	
	public NodeSet(int initialCapacity) {
		nodes = new ObjectMap<String, Node>(initialCapacity, 1);
	}
	
	public Node get(Vector2 pos) {
		return nodes.get(key(pos));
	}
	
	public void add(Node node) {
		nodes.put(key(node), node);
	}
	
	private static String key(Node node) {
		return key(node.pos);
	}
	
	private static String key(Vector2 pos) {
		return pos.x+"/"+pos.y;
	}
}
