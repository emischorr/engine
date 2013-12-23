package de.micralon.engine;

public abstract class DestructibleGameObject extends GameObject implements Destructible {
	protected DamageModel damageModel;
	protected boolean destroyed = false;
	
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
