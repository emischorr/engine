package de.micralon.engine.text;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import de.micralon.engine.GameWorld;

public class NameTag extends Text {
	public boolean pointer = false;
	private float xOffset, yOffset;
	// temp vars
	private Vector3 pos = new Vector3();
	
	public NameTag(BitmapFont font, Color color, String text) {
		this(font, color, text, 0, 0);
	}
	
	/**
	 * Creates a text that follows an object. (Is automatically added to the world instance)
	 * @param font
	 * @param color
	 * @param text
	 * @param xOffset in meters
	 * @param yOffset in meters
	 */
	public NameTag(BitmapFont font, Color color, String text, float xOffset, float yOffset) {
		super(font, color, text, 0, 0);
		addToWorld();
		
		this.xOffset = xOffset;
		this.yOffset = yOffset;
	}
	
	public void updatePos(Vector2 newPos) {
		updatePos(newPos.x, newPos.y);
	}
	
	public void updatePos(float posX, float posY) {
		pos.x = posX + xOffset;
		pos.y = posY + yOffset;
		GameWorld.ctx.stage.getCamera().project(pos);
		x = pos.x;
		y = pos.y;
	}
	
	@Override
	public void draw(Batch batch) {
		if (pointer) {
			// TODO: if true show tag also if out of view at the border of viewport to indicate to the user the direction of this tagged object
		}
		super.draw(batch);
	}
}
