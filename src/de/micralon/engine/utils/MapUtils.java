package de.micralon.engine.utils;

import com.badlogic.gdx.math.Vector2;

import de.micralon.engine.map.IsoTile;

public class MapUtils {
	
	// temp
	private static Vector2 temp = new Vector2();
	
	public static Vector2 staggeredToNormal(IsoTile tile) {
		return staggeredToNormal(tile.tileX, tile.tileY);
	}
	public static Vector2 staggeredToNormal(Vector2 staggered) {
		return staggeredToNormal(staggered.x, staggered.y);
	}
	public static Vector2 staggeredToNormal(float staggeredX, float staggeredY) {
		temp.x = (float) (Math.floor(staggeredY / 2) + staggeredX);
		temp.y = (float) (Math.floor(staggeredY / 2) + (staggeredY % 2) - staggeredX);
		return temp;
	}
	
	public static Vector2 normalToStaggerd(IsoTile tile) {
		return normalToStaggerd(tile.tileX, tile.tileY);
	}
	public static Vector2 normalToStaggerd(Vector2 staggered) {
		return normalToStaggerd(staggered.x, staggered.y);
	}
	public static Vector2 normalToStaggerd(float normalX, float normalY) {
		temp.x = (float) (normalX - Math.floor((normalX + normalY)/2));
		temp.y = normalX + normalY;
		return temp;
	}
	
}
