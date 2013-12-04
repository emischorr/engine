package de.micralon.engine.text;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import de.micralon.engine.GameWorld;

public class NameTag extends Text {
	private Vector3 pos = new Vector3();
	public boolean pointer = false;
	
	public NameTag(BitmapFont font, Color color, String text) {
		super(font, color, text, 0, 0);
	}
	
	public void updatePos(Vector2 newPos) {
		updatePos(newPos.x, newPos.y);
	}
	
	public void updatePos(float posX, float posY) {
		pos.x = posX;
		pos.y = posY;
		GameWorld.ctx.stage.getCamera().project(pos);
		x = pos.x;
		y = pos.y;
	}
	
	@Override
	public void draw(SpriteBatch batch) {
		if (pointer) {
			// TODO: if true show tag also if out of view at the border of viewport to indicate to the user the direction of this tagged object
		}
		super.draw(batch);
	}
}
