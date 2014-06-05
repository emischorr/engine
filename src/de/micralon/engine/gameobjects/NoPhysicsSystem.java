package de.micralon.engine.gameobjects;

import com.badlogic.gdx.math.Vector2;

import de.micralon.engine.utils.Log;

public class NoPhysicsSystem implements PhysicsSystem {
	
	@Override
	public void init() {}
	
	@Override
	public float getWidth() {
		Log.warn("NoPhysicsSystem in use!");
		return 0;
	}

	@Override
	public float getHeight() {
		Log.warn("NoPhysicsSystem in use!");
		return 0;
	}

	@Override
	public Vector2 getPosition() {
		Log.warn("NoPhysicsSystem in use!");
		return new Vector2(0,0);
	}

	@Override
	public PhysicsSystem setPosition(float x, float y) {
		Log.warn("NoPhysicsSystem in use!");
		return this;
	}

	@Override
	public PhysicsSystem setDegree(float degree) {
		Log.warn("NoPhysicsSystem in use!");
		return this;
	}

	@Override
	public float getDegree() {
		Log.warn("NoPhysicsSystem in use!");
		return 0;
	}

	@Override
	public void resetMass() {}

	@Override
	public void reset() {}

	@Override
	public void destroy() {}

	@Override
	public Vector2 getVelocity() {
		Log.warn("NoPhysicsSystem in use!");
		return null;
	}

	@Override
	public void setVelocity(Vector2 velocity) {
		Log.warn("NoPhysicsSystem in use!");
	}

	@Override
	public void setGameObject(GameObject gameObject) {
		Log.warn("NoPhysicsSystem in use!");
	}

}
