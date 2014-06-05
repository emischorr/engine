package de.micralon.engine.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

/**
 * Represents the gaming interface for the player
 * @author Enrico Mischorr
 *
 */
public class Hud implements Disposable {
	protected Stage stage; // stage that holds the GUI. Pixel-exact size.
	private OrthographicCamera cam;
	
	private int SCREEN_WIDTH = Gdx.graphics.getWidth();
	private int SCREEN_HEIGHT = Gdx.graphics.getHeight();
	
	public Hud() {
		stage = new Stage(new ScreenViewport());
		//stage.setViewport(new StretchViewport(SCREEN_WIDTH, SCREEN_HEIGHT));
		
		cam = (OrthographicCamera) stage.getCamera();
		cam.position.set(SCREEN_WIDTH/2, SCREEN_HEIGHT/2, 0);
	}
	
	public void addToInputMultiplexer(InputMultiplexer inputs) {
		inputs.addProcessor(stage);
	}
	
	public void update(float deltaTime) {
		cam.update();
		stage.act(deltaTime);
	}
	
	public void draw() {
		stage.draw();
	}
	
	protected int getScreenWidth() {
		return SCREEN_WIDTH;
	}
	
	protected int getScreenHeight() {
		return SCREEN_HEIGHT;
	}
	
	@Override
	public void dispose() {
		stage.dispose();
	}
}
