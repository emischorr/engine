package de.micralon.engine.map;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.Scaling;

public class IsoTile extends Tile {
	protected static Polygon basePoly;
	private Polygon renderPoly;
	public static float yOffset;

	public IsoTile(TextureRegion textureRegion, int tileX, int tileY) {
		super(textureRegion, tileX, tileY);
				
		this.setPosition(tileX*tileSize+tileSize-tileY%2*tileSize*0.5f, tileY*tileSize*0.25f);
		this.setScaling(Scaling.fillX); // size the texture to match tile width  
		
		initBasePolygon();
	}
	
	@Override
	public Actor hit (float x, float y, boolean touchable) {
		if (touchable && this.getTouchable() != Touchable.enabled) return null;
		return basePoly.contains(x, y-yOffset) ? this : null;
	}
	
	@Override
	public void drawDebug(ShapeRenderer shapeRenderer) {
		initRenderPoly();
		shapeRenderer.polygon(renderPoly.getTransformedVertices());
	}
	
	protected void initBasePolygon() {
		if (basePoly == null) {
			basePoly = newBasePolygon();
		}
	}
	
	protected Polygon newBasePolygon() {
		float baseVertices[] = {
				tileSize/2, tileSize/2,
				tileSize, tileSize/4,
				tileSize/2, 0,
				0, tileSize/4
			};
		return new Polygon(baseVertices);
	}
	
	protected void initRenderPoly() {
		if (renderPoly == null) {
			renderPoly = new Polygon(basePoly.getVertices());
			renderPoly.setPosition(getX(), getY()+yOffset);
		}
	}

}
