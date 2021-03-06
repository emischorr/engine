package de.micralon.engine.gameobjects;

import com.badlogic.gdx.math.Vector2;

public class BasicPhysicsSystem implements PhysicsSystem {
	protected float width, height;
	protected final Vector2 position = new Vector2(0,0);
	protected Vector2 velocity = new Vector2(0,0);
	protected float degree;
	protected GameObject gameObject;
	
	public BasicPhysicsSystem() {
		this(1, 1);
	}
	
	public BasicPhysicsSystem(float width, float height) {
		this.width = width;
		this.height = height;
	}
	
	@Override
	public void init() {}
	
	@Override
	public float getWidth() {
		return width;
	}

	@Override
	public float getHeight() {
		return height;
	}

	@Override
	public Vector2 getPosition() {
		return position;
	}

	@Override
	public PhysicsSystem setPosition(float x, float y) {
		position.set(x, y);
		return this;
	}

	@Override
	public PhysicsSystem setDegree(float degree) {
		this.degree = degree;
		return this;
	}

	@Override
	public float getDegree() {
		return degree;
	}

	@Override
	public void resetMass() {
		// no implementation
	}

	@Override
	public void reset() {
		position.set(0, 0);
		degree = 0;
	}

	@Override
	public void destroy() {
		// no implementation
	}

	@Override
	public Vector2 getVelocity() {
		return velocity;
	}

	@Override
	public void setVelocity(Vector2 velocity) {
		this.velocity = velocity;
	}

	@Override
	public void setGameObject(GameObject gameObject) {
		this.gameObject = gameObject;
	}

}
