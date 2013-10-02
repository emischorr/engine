package de.micralon.engine;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class NameTag {
	private GameWorld world;
	public String text;
	public Color color;
	private Vector3 pos = new Vector3();
	public boolean visible = true;
	public boolean pointer = false;
	
	// temp vars
	private Color tmpColor;
	
	public NameTag(GameWorld world, String text, Color color) {
		super();
		world.tags.add(this);
		this.world = world;
		this.text = text;
		this.color = color;
	}
	
	public void updatePos(Vector2 newPos) {
		updatePos(newPos.x, newPos.y);
	}
	
	public void updatePos(float posX, float posY) {
		pos.x = posX;
		pos.y = posY;
		world.stage.getCamera().project(pos);
	}
	
	public void draw(SpriteBatch batch, BitmapFont font) {
		if (visible) {
			if (pointer) {
				// TODO: if true show tag also if out of view at the border of viewport to indicate to the user the direction of this tagged object
			}
			tmpColor = font.getColor();
			font.setColor(color);
            font.draw(batch, text, pos.x, pos.y);
            font.setColor(tmpColor);
		}
	}
}
