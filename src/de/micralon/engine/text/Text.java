package de.micralon.engine.text;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import de.micralon.engine.GameWorld;

public class Text {
	protected BitmapFont font;
	protected Color color;
	protected String text;
	protected float x, y, alpha;
	
	protected float stateTime;
	
	// temp vars
	private TextBounds bounds;
	
	public Text(BitmapFont font, Color color, String text, float x, float y) {
		GameWorld.ctx.texts.add(this);
		this.font = font;
		this.color = color;
		this.text = text;
		this.x = x;
		this.y = y;
		
		bounds = this.font.getBounds(text);
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public void update(float deltaTime) {
		stateTime += deltaTime;
	}

	public void draw (SpriteBatch batch) {
//        bounds = Assets.font.getBounds(text);
        font.setColor(color.r, color.g, color.b, alpha);
        font.draw(batch, text, x - (int)(bounds.width / 2), y + (int)(bounds.height / 2));
	}
}
