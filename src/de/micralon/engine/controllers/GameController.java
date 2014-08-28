package de.micralon.engine.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Input.Keys;

import de.micralon.engine.Env;

public abstract class GameController extends InputMultiplexer {
    private long pausedTime;
    private final long pauseCooldown = 500;

	public void processInput() {
		if ((Gdx.input.isKeyPressed(Keys.ESCAPE) || Gdx.input.isKeyPressed(Keys.BACK)) && pausedTime + pauseCooldown <= System.currentTimeMillis()) {
			if (Env.game.paused) {
				Env.game.paused = false;
			} else {
				Env.game.paused = true;
			}
			pausedTime = System.currentTimeMillis();
		}
	}

}
