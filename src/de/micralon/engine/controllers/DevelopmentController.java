package de.micralon.engine.controllers;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;

import de.micralon.engine.CameraHelper;
import de.micralon.engine.GameRendererOptions;

public class DevelopmentController extends InputAdapter {
	private CameraHelper cameraHelper;
	private GameRendererOptions renderOptions;
	
	public DevelopmentController(CameraHelper cameraHelper, GameRendererOptions renderOptions) {
		this.cameraHelper = cameraHelper;
		this.renderOptions = renderOptions;
	}

	public boolean keyUp (int keycode) {
		if (keycode == Keys.R) {
			//TODO: reset world/screen
		}
		
		if (keycode == Keys.K) {
			cameraHelper.followTarget = !cameraHelper.followTarget;
		}
		
		if (keycode == Keys.MINUS) { // zoom camera out
			cameraHelper.setZoom(cameraHelper.getZoom() + 0.2f);
		}
		if (keycode == Keys.PLUS) { // zoom camera in
			cameraHelper.setZoom(cameraHelper.getZoom() - 0.2f);
		}
		if (keycode == Keys.NUM_0) { // default zoom
			cameraHelper.setZoom(1);
		}
		
		if (keycode == Keys.NUM_4) {
			cameraHelper.moveLeft(0.5f);
		}
		
		if (keycode == Keys.NUM_6) {
			cameraHelper.moveRight(0.5f);
		}
		
		if (keycode == Keys.NUM_8) {
			cameraHelper.moveUp(0.5f);
		}
		
		if (keycode == Keys.NUM_2) {
			cameraHelper.moveDown(0.5f);
		}
		
		if (keycode == Keys.M) {
			renderOptions.switchMode();
		}
		
		return false;
	}	
}
