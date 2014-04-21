package de.micralon.engine.gameobjects;

import com.badlogic.gdx.math.Vector2;

public interface PhysicsSystem {
	
	public Vector2 getPosition();
	
	public PhysicsSystem setPosition(float x, float y);
	
	public PhysicsSystem setDegree(float degree);
	
	public float getDegree();

	void destroy();

}
