package de.micralon.engine.text;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import de.micralon.engine.GameWorld;

public class Text {
	protected BitmapFont font;
	protected Color color;
	protected String text;
	protected float x, y, alpha = 1;
	
	protected float stateTime;
	
	private float height, width;
	
	public Text(BitmapFont font, Color color, String text) {
		this(font, color, text, Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
	}
	
	public Text(BitmapFont font, Color color, String text, float x, float y) {
		this.font = font;
		this.color = color;
		this.text = text;
		this.x = x;
		this.y = y;
		
		calcBounds();
	}
	
	public void addToWorld() {
		GameWorld.ctx.texts.add(this);
	}
	
	public void setText(String text) {
		this.text = text;
		calcBounds(); // update bounds since text length may have changed
	}
	
	public void update(float deltaTime) {
		stateTime += deltaTime;
	}

	public void draw (SpriteBatch batch) {
        font.setColor(color.r, color.g, color.b, alpha);
        font.draw(batch, text, x - (int)(width / 2), y + (int)(height / 2));
	}
	
	private void calcBounds() {
		height = this.font.getBounds(text).height;
		width = this.font.getBounds(text).width;
	}
}
