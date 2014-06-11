package de.micralon.engine.pathfinding;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public interface Pathfindable {
	/**
	 * Cost for mover (from source) to target
	 * @param source
	 * @param target
	 * @param mover
	 * @return
	 */
	public float getMovementCost(Vector2 source, Vector2 target, Movable mover);
	
	/**
	 * Check if tile is blocked for mover object
	 * @param target
	 * @param mover
	 * @return
	 */
	public boolean isBlocked(Vector2 target, Movable mover);
	
	public Array<Vector2> reachablePositions(Vector2 nodePos);

	public boolean toExpensive(Movable mover, float nextStepCost);
}
