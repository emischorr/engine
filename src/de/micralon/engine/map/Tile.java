package de.micralon.engine.map;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Align;

public abstract class Tile extends Image {
	public final int tileX, tileY;
	
	public Tile(TextureRegion textureRegion, int tileX, int tileY, float tileSize) {
		super(textureRegion);
		this.tileX = tileX;
		this.tileY = tileY;
		
		this.setSize(tileSize, tileSize);
		this.setAlign(Align.center);
	}
	
	public void drawDebug(ShapeRenderer shapeRenderer) {}
}
