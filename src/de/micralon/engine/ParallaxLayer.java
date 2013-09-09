package de.micralon.engine;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class ParallaxLayer extends Image {
	public float ratioX, ratioY;
	
	public ParallaxLayer(Drawable drawable, float ratio) {
		this(drawable, ratio, ratio);
	}
	
	public ParallaxLayer(Drawable drawable, float ratioX, float ratioY) {
		super(drawable);
		this.ratioX = ratioX;
		this.ratioY = ratioY;
	}
	
	public void move(float x, float y) {
		translate(x*ratioX, y*ratioY);
	}
	
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		// draw the same on the left and right
		getDrawable().draw(batch, getX() - getWidth(), getY(), getWidth(), getHeight());
		getDrawable().draw(batch, getX() + getWidth(), getY(), getWidth(), getHeight());
	}
}
