package de.micralon.engine.manager;

import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;

public class EffectManager implements Disposable {
	private ObjectMap<String, ParticleEffectPool> pools;
	private Array<PooledEffect> effects;
	private int peak;
	
	private static final int DEFAULT_CAPACITY = 32;
	
	// temp vars
	private PooledEffect effect;
	
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
	
	public PooledEffect getEffect(String name) {
		effect = pools.get(name).obtain();
		effects.add(effect);
		if (effects.size > peak) peak = effects.size;
		return effect;
	}
	
	public PooledEffect getEffect(String name, float xPos, float yPos) {
		effect = getEffect(name);
		effect.setPosition(xPos, yPos);
		return effect;
	}
	
	public void free(PooledEffect effect) {
		effect.free();
	}
	
	public int getPeak() {
		return peak;
	}
	
	public void update() {
		for (PooledEffect effect : effects) {
			if (effect.isComplete()) {
				effects.removeValue(effect, true);
				effect.free();
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
		pools.clear();
		effects.clear();
	}
}
