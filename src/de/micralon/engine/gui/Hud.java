package de.micralon.engine.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;

import de.micralon.engine.AbstractGameWorld;

public class Hud {
	private Stage stage; // stage that holds the GUI. Pixel-exact size.
	private OrthographicCamera cam;
	
	private int SCREEN_WIDTH = Gdx.graphics.getWidth();
	private int SCREEN_HEIGHT = Gdx.graphics.getHeight();
	
	public Hud(AbstractGameWorld<?> world) {
		stage = new Stage();
		stage.setViewport(SCREEN_WIDTH, SCREEN_HEIGHT, false);
		
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
}
