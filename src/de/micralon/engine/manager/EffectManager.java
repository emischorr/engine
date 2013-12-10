package de.micralon.engine.manager;

import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;

import de.micralon.engine.GameWorld;
import de.micralon.engine.ParticleEffectActor;

public class EffectManager implements Disposable {
	private ObjectMap<String, ParticleEffectPool> pools;
	private Array<PooledEffect> effects;
	private int peak;
	
	private static final int DEFAULT_CAPACITY = 32;
	
	// temp vars
	private PooledEffect effect;
	private int free = 0;
	
	public EffectManager() {
		this(DEFAULT_CAPACITY);
	}
	
	public EffectManager(int initCapacity) {
		pools = new ObjectMap<String, ParticleEffectPool>(initCapacity);
		effects = new Array<PooledEffect>(initCapacity/2);
	}
	
	public Array<String> getEffectNames() {
		return pools.keys().toArray();
	}
	
	public void addEffect(String name, ParticleEffect effect) {
		pools.put(name, new ParticleEffectPool(effect, 1, 20));
	}
	
	public void showEffect(String name, float xPos, float yPos) {
		ParticleEffectActor actor = getEffectActor(name);
		actor.setPosition(xPos, yPos);
		actor.start();
		GameWorld.ctx.bg.addActor(actor);
	}
	
	public ParticleEffectActor getEffectActor(String name) {
		effect = getEffect(name);
		return new ParticleEffectActor(effect);
	}
	
	public PooledEffect getEffect(String name, float xPos, float yPos) {
		effect = getEffect(name);
		effect.setPosition(xPos, yPos);
		return effect;
	}
	
	public PooledEffect getEffect(String name) {
		effect = pools.get(name).obtain();
		effects.add(effect);
		if (effects.size > peak) peak = effects.size;
		return effect;
	}
	
	public void free(PooledEffect effect) {
		effects.removeValue(effect, true);
		effect.free();
	}
	
	public int getPeak() {
		return peak;
	}
	
	public int getFree() {
		free = 0;
		for (ParticleEffectPool pool : pools.values()) {
			free += pool.getFree();
		}
		return free;
	}
	
	public int getCount() {
		return effects.size;
	}
	
	public void update() {
		for (PooledEffect effect : effects) {
			if (effect.isComplete()) {
				free(effect);
			}
		}
	}
	
	public void draw(SpriteBatch batch) {
		for (PooledEffect effect : effects) {
			effect.draw(batch);
		}
	}

	@Override
	public void dispose() {
		for (PooledEffect effect : effects) {
			free(effect);
		}
		effects.clear();
		pools.clear();
	}
}
