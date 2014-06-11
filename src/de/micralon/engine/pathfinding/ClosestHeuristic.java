package de.micralon.engine.pathfinding;

import com.badlogic.gdx.math.Vector2;

public class ClosestHeuristic implements Heuristic {

	@Override
	public float getCost(Object mover, Vector2 nodePos, Vector2 target) {
		return nodePos.dst(target);
	}

}
