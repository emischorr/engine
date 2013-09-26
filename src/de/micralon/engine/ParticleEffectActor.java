package de.micralon.engine;

import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class ParticleEffectActor extends Actor {
	ParticleEffect effect;

	   public ParticleEffectActor(ParticleEffect effect) {
	      this.effect = effect;
	   }

	   public void draw(SpriteBatch batch, float parentAlpha) {
	      effect.draw(batch);
	   }

	   public void act(float delta) {
	      super.act(delta);
	      effect.setPosition(getX(), getY());
	      effect.update(delta);
	      effect.start(); //need to start the particle spawning
	   }

	   public ParticleEffect getEffect() {
	      return effect;
	   }
}
