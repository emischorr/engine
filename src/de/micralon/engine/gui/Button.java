package de.micralon.engine.gui;

import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class Button extends Image {
	private boolean isTouched = false;

	public Button(int posX, int posY, Drawable drawable) {
		super(drawable);
		setTouchable(Touchable.enabled);
		setPosition(posX, posY);
		addListener(new ButtonInputListener(this));
	}
	
	public void touched(Boolean touch) {
		isTouched = touch;
	}
	
	public boolean isTouched() {
		return isTouched;
	}
}
