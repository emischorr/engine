package de.micralon.engine.map;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Align;

public abstract class Tile extends Image {
	public static float tileSize = 1;
	
	protected final int tileX, tileY;
	
	public Tile(TextureRegion textureRegion, int tileX, int tileY) {
		this(textureRegion, tileX, tileY, tileSize);
	}
	
	public Tile(TextureRegion textureRegion, int tileX, int tileY, float newTileSize) {
		super(textureRegion);
		this.tileX = tileX;
		this.tileY = tileY;
		
		Tile.tileSize = newTileSize;
		
		this.setSize(tileSize, tileSize);
		this.setAlign(Align.center);
	}
	
	public String identifier() {
		return tileX+"/"+tileY;
	}
	
	public Integer coordX() {
		return tileX;
	}
	
	public Integer coordY() {
		return tileY;
	}
	
	public Vector2 getCoords() {
		return new Vector2(tileX, tileY);
	}
	
	public void drawDebug(ShapeRenderer shapeRenderer) {}
}
