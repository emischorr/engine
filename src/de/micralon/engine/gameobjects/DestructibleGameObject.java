package de.micralon.engine.gameobjects;

import com.badlogic.gdx.utils.Scaling;

import de.micralon.engine.DamageModel;
import de.micralon.engine.Destructible;
import de.micralon.engine.GameWorld;

public abstract class DestructibleGameObject extends GameObject implements Destructible {
	protected DamageModel damageModel;
	protected boolean destroyed = false;
	
	public DestructibleGameObject() {
		super();
	}
	
	protected DestructibleGameObject(PhysicsSystem physics, float bodyWidth, float bodyHeight, float linearDamping, float angularDamping) {
		super(physics, bodyWidth, bodyHeight, linearDamping, angularDamping);
	}
	
	protected DestructibleGameObject(PhysicsSystem physics, float bodyWidth, float bodyHeight, float linearDamping, float angularDamping, Scaling scaling) {
		super(physics, bodyWidth, bodyHeight, linearDamping, angularDamping, scaling);
	}
	
	@Override
	public DamageModel getDamageModel() {
		return damageModel;
	}
	
	@Override
	public boolean isDestroyed() {
		return destroyed;
	}
	
	@Override
	public void setDestroyed() {
		if (!destroyed) {
			destroyed = true;
			GameWorld.ctx.removeObject(this);
		}
	}
}
