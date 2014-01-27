package de.micralon.engine;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Disposable;

public abstract class GameDecal extends Image implements Disposable {
	public boolean show = true;
	
	public GameDecal(Drawable drawableRegion, float posX, float posY) {
		super();
		setDrawable(drawableRegion);
		setPosition(posX, posY);
	}
	
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		if (show) {
			super.draw(batch, parentAlpha);
		}
	}
	
	public void update(float delta) {};
	
	@Override
	public void dispose() {
		
	}
}
