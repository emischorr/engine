package de.micralon.engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter.ScaledNumericValue;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class ParticleEffectActor extends Actor {
	private boolean behind = false;
	private boolean running = false;
	private ParticleEffect effect;
	private float rotation;

	public ParticleEffectActor(ParticleEffect effect) {
		this.effect = effect;
		for (ParticleEmitter emitter : effect.getEmitters()) {
			if (emitter.isBehind()) {
				behind = true;
				break;
			}
		}
	}
	
	public boolean isBehind() {
		return behind;
	}
   
	public ParticleEffectActor(String effectFile, String imagesDir) {
		effect = new ParticleEffect();
		effect.load(Gdx.files.internal(effectFile), Gdx.files.internal(imagesDir));
	}
	
	public void start() {
		effect.start(); //need to start the particle spawning
		running = true;
	}
	
	public void stop() {
		if (running) {
			effect.allowCompletion();
			effect.reset();
//			effect.setDuration(0);
			running = false;
		}
	}
	
	public void rotate(float degrees) {
		super.rotateBy(degrees);
		ScaledNumericValue angle;
		for (ParticleEmitter emitter : effect.getEmitters()) {
			angle = emitter.getAngle();
			emitter.getAngle().setHigh((angle.getHighMin()+degrees-rotation)%360, (angle.getHighMax()+degrees-rotation)%360);
			emitter.getAngle().setLow((angle.getLowMin()+degrees-rotation)%360, (angle.getLowMax()+degrees-rotation)%360);
		}
		rotation = degrees;
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		effect.draw(batch);
	}

	public void act(float delta) {
		super.act(delta);
		effect.setPosition(getX(), getY());
		effect.update(delta);
		if (!running) {
			start();
		}
		if (effect.isComplete()) {
			remove();
		}
	}
	
	@Override
	public void setPosition(float x, float y) {
		super.setPosition(x, y);
		effect.setPosition(x, y);
	}
	
	@Override
	public boolean remove() {
		if (super.remove()) {
			stop();
			dispose();
			return true;
		} else {
			return false;
		}
	}

	public ParticleEffect getEffect() {
		return effect;
	}
	
	public void dispose() { 
		if (effect instanceof PooledEffect) { 
			GameWorld.ctx.effectManager.free((PooledEffect) effect);
			// TODO: make this work...
			// ((PooledEffect) effect).free();
		} else {
			effect.dispose();
		}
	}
}
