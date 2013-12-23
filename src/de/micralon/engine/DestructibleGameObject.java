package de.micralon.engine;

import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Scaling;

public abstract class DestructibleGameObject extends GameObject implements Destructible {
	protected DamageModel damageModel;
	protected boolean destroyed = false;
	
	public DestructibleGameObject() {
		super();
	}
	
	protected DestructibleGameObject(BodyType type, float bodyWidth, float bodyHeight, float linearDamping, float angularDamping) {
		super(type, bodyWidth, bodyHeight, linearDamping, angularDamping);
	}
	
	protected DestructibleGameObject(BodyType type, float bodyWidth, float bodyHeight, float linearDamping, float angularDamping, Scaling scaling) {
		super(type, bodyWidth, bodyHeight, linearDamping, angularDamping, scaling);
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
