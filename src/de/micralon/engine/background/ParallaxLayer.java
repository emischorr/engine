package de.micralon.engine.background;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class ParallaxLayer extends Actor {
	public Drawable drawable;
	public float ratioX, ratioY;
	
	public ParallaxLayer(Drawable drawable, float ratio) {
		this(drawable, Color.WHITE, ratio);
	}
	
	public ParallaxLayer(Drawable drawable, Color color, float ratio) {
		this(drawable, color, ratio, ratio);
	}
	
	public ParallaxLayer(Drawable drawable, float ratioX, float ratioY) {
		this(drawable, Color.WHITE, ratioX, ratioY);
	}
	
	public ParallaxLayer(Drawable drawable, Color color, float ratioX, float ratioY) {
		super();
		this.drawable = drawable;
		setColor(color);
		this.ratioX = ratioX;
		this.ratioY = ratioY;
	}
	
	public void move(float x, float y) {
		translate(x*ratioX, y*ratioY);
	}
	
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
//		super.draw(batch, parentAlpha);
		drawable.draw(batch, getX(), getY(), drawable.getMinWidth(), drawable.getMinHeight());
		// draw the same on the left and right
		drawable.draw(batch, getX() - getWidth(), getY(), drawable.getMinWidth(), drawable.getMinHeight());
		drawable.draw(batch, getX() + getWidth(), getY(), drawable.getMinWidth(), drawable.getMinHeight());
	}
}
