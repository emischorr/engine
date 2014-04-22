package de.micralon.engine.gameobjects;

import com.badlogic.gdx.math.Vector2;

public class BasicPhysicsSystem implements PhysicsSystem {
	protected final Vector2 position = new Vector2(0,0);
	protected float degree;

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

}
