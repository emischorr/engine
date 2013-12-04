package de.micralon.engine.text;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class MovingText extends Text {

	public MovingText(BitmapFont font, Color color, String text, float x, float y) {
		super(font, color, text, x, y);
	}
	
	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		
		y+=deltaTime * 30; // move text upwards...
	}

}
