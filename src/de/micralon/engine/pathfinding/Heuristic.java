package de.micralon.engine.pathfinding;

import com.badlogic.gdx.math.Vector2;

/**
 * heuristic to control the priority during the search
 * @author Enrico Mischorr
 *
 */
public interface Heuristic {
	public float getCost(Object mover, Vector2 nodePos, Vector2 target);
}
