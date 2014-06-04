package de.micralon.engine.gameobjects;

import com.badlogic.gdx.math.Vector2;

public interface PhysicsSystem {
	
	public void init();
	
	public float getWidth();
	
	public float getHeight();
	
	public Vector2 getPosition();
	
	public PhysicsSystem setPosition(float x, float y);
	
	public PhysicsSystem setDegree(float degree);
	
	public float getDegree();
	
	public Vector2 getVelocity();
	
	public void setVelocity(Vector2 velocity);
	
	public void resetMass();
	
	public void reset();

	public void destroy();

}
