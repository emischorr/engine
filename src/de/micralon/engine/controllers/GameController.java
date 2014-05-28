package de.micralon.engine.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Input.Keys;

import de.micralon.engine.EngineGame;

public abstract class GameController extends InputMultiplexer {
	private EngineGame game;
    private long pausedTime;
    private final long pauseCooldown = 500;
    
    public GameController(EngineGame game) {
    	this.game = game;
    }

	public void processInput() {
		if ((Gdx.input.isKeyPressed(Keys.ESCAPE) || Gdx.input.isKeyPressed(Keys.BACK)) && pausedTime + pauseCooldown <= System.currentTimeMillis()) {
			if (game.paused) {
				game.paused = false;
			} else {
				game.paused = true;
			}
			pausedTime = System.currentTimeMillis();
		}
	}

}
