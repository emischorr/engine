package de.micralon.engine.pathfinding;

import com.badlogic.gdx.math.Vector2;

public interface Pathfinder {

	public Path findPath(Movable mover, Vector2 source, Vector2 target);
	
}
