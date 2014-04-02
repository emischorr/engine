package de.micralon.engine.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.Scaling;

public class IsoTile extends Tile {
	private final Polygon basePoly;

	public IsoTile(TextureRegion textureRegion, int tileX, int tileY, float tileSize) {
		super(textureRegion, tileX, tileY, tileSize);
				
		this.setPosition(tileX*tileSize+tileSize-tileY%2*tileSize*0.5f, tileY*tileSize*0.25f);
		this.setScaling(Scaling.fillX); // size the texture to match tile width  
		
		float baseVertices[] = {
			tileSize/2, tileSize/2,
			tileSize, tileSize/4,
			tileSize/2, 0,
			0, tileSize/4
		};
		basePoly = new Polygon(baseVertices);
		basePoly.setPosition(getX(), getY());
		
		final String log = tileX+"/"+tileY;
		this.addListener(new InputListener() {
			@Override
		    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
		        return true;
		    }
			
			@Override
		    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
		 	   Gdx.app.log("Tile", log);
			}
		});
	}
	
	@Override
	public Actor hit (float x, float y, boolean touchable) {
		if (touchable && this.getTouchable() != Touchable.enabled) return null;
		return basePoly.contains(x, y) ? this : null;
	}
	
	@Override
	public void drawDebug(ShapeRenderer shapeRenderer) {
		shapeRenderer.polygon(basePoly.getTransformedVertices());
	}

}
