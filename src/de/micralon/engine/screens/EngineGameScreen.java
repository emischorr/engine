package de.micralon.engine.screens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import de.micralon.engine.EngineGame;
import de.micralon.engine.GameRenderer;
import de.micralon.engine.GameRendererOptions;
import de.micralon.engine.controllers.DevelopmentController;
import de.micralon.engine.controllers.GameController;
import de.micralon.engine.gui.Hud;
import de.micralon.engine.postprocessing.ShaderManager;

public abstract class EngineGameScreen<T extends EngineGame> extends AbstractScreen<T> {
	private GameRenderer renderer;
	private GameRendererOptions renderOptions = new GameRendererOptions();
	private boolean continuousRendering = true; // render mode
	private boolean stepped = false;
    private boolean screenshotTaken = false;
	private TextureRegion fboRegion;
	private ShaderManager shaders;

	public EngineGameScreen(T game) {
		super(game);
		getGui().addToInputMultiplexer(inputs);
		
		shaders = new ShaderManager();
	}
	
	public abstract GameController getGameController();
	public abstract Hud getGui();

	@Override
	public void show() {
		super.show();
		
		Gdx.input.setCatchBackKey(true);
		Gdx.graphics.setVSync(false); // switch off for performance
		Gdx.graphics.setContinuousRendering(continuousRendering);
		
		if (EngineGame.isDevelopment()) {
			renderOptions = new GameRendererOptions(false, false, true, true);
		} else {
			renderOptions = new GameRendererOptions(true, true, true, false);
		}
		
		renderer = new GameRenderer(game.getWorld(), getBatch(), renderOptions);
		
		if (EngineGame.isDevelopment()) {
			inputs.addProcessor(new DevelopmentController(game.getWorld().cameraHelper, renderOptions));
		}
		
		// TODO: shaders currently brake the lights... :-(
		renderer.addPostEffect(shaders.get(ShaderManager.DEFAULT_SHADER));
	}
	
	@Override
	public void render(float deltaTime) {
		stepped = false;
		
		if (!game.paused) {
			screenshotTaken = false;
			super.getTable().setVisible(false); // we do not want touch events on the table
			getGameController().processInput();
	
			if (game.updatePhysics(deltaTime) > 0) {
				getGui().update(deltaTime); // update GUI
				stepped = true;
			}
			if (continuousRendering || stepped) {
				Gdx.gl.glClearColor(0, 0, 0, 1);
				Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT );
				
				renderer.render(); // draw the box2d world
				if (game.getPlayer().alive) getGui().draw(); // draw the GUI
			}
			if (!continuousRendering) Gdx.graphics.requestRendering();
		} else { // Show pause menu
			if (!screenshotTaken) {
				fboRegion = renderer.getFrame();
				screenshotTaken = true;
			}
			
			Gdx.gl.glClearColor(0, 0, 0, 1);
			Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT );
			
			getBatch().begin();
			//draw our offscreen FBO texture to the screen with the given alpha
			getBatch().setColor(1f, 1f, 1f, 0.5f);
			getBatch().draw(fboRegion, 0, 0);
			getBatch().end();
			
			super.getTable().setVisible(true);
			stage.draw();
			
			if (game.isHost()) { //TODO: move this to game
				//TODO: if host of network game send pause info and last world state
			}
		}
		
		if (EngineGame.isDevelopment()) {
			getBatch().begin();
			getFont().setColor(Color.RED);
			getFont().draw(getBatch(), "FPS: " + Gdx.graphics.getFramesPerSecond(), 5, Gdx.graphics.getHeight()-5);
			getBatch().end();
		}
	}
	
	@Override
	public void pause() {
		//TODO: config switch to allow pause on lost focus
		if (Gdx.app.getType() == Application.ApplicationType.Desktop) {
			game.paused = true;
		}
	}
	
	@Override
	public void resume() {
//		paused = false;
	}
	
	@Override
	public void hide() {
		super.hide();
		Gdx.input.setCatchBackKey(false);
		dispose();
	}
	
	@Override
	public void dispose() {
		super.dispose();
	}
}
