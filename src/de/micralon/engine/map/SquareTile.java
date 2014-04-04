package de.micralon.engine.map;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Scaling;

public class SquareTile extends Tile {

	public SquareTile(TextureRegion textureRegion, int tileX, int tileY) {
		super(textureRegion, tileX, tileY);
		
		this.setPosition(tileX*tileSize-tileSize*0.5f, tileY*tileSize-tileSize*0.5f);
		this.setScaling(Scaling.stretch); // stretch the texture
	}
	
	@Override
	public void drawDebug(ShapeRenderer shapeRenderer) {
		shapeRenderer.rect(getX(), getY(), getWidth(), getHeight());
	}
}
