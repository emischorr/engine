package de.micralon.engine.controllers;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputEvent.Type;

import de.micralon.engine.screens.AbstractScreen;

public class MenuController extends InputAdapter {
	private AbstractScreen screen;
	
	// temp vars
	InputEvent keyUpEvent;
	
	public MenuController(AbstractScreen screen) {
		this.screen = screen;
		
		keyUpEvent = new InputEvent();
		keyUpEvent.setType(Type.touchUp);
	}

	public boolean keyUp (int keycode) {
		if (keycode == Keys.UP || keycode == Keys.W) {
			screen.previousEntry();
		}
		if (keycode == Keys.DOWN || keycode == Keys.S) {
			screen.nextEntry();
		}
		if (keycode == Keys.ENTER || keycode == Keys.SPACE) {
			screen.getSelectedEntry().fire(keyUpEvent);
		}
		
		return false;
	}

}
