package de.micralon.engine.utils;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

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
	
	/**
	 * Calculates coordinate-pairs belonging to the selection. <br/>
	 * The selection is a rectangle based on the start and end point. <br/>
	 * Coordinates are expected to represent tiles on a staggered isometric map. While the returned coordinates represent tiles in a Cartesian map.<br/>
	 * Use {@link MapUtils#normalToStaggerd} to convert the result back to a staggered isometric representation.
	 * @param start Coordinate
	 * @param end Coordinate
	 * @return an array of coordinates belonging to the selection rectangle (Cartesian map representation)
	 */
	public static Array<Vector2> getSelectionCoordsStaggered(Vector2 start, Vector2 end) {
		return getSelectionCoords(MapUtils.staggeredToNormal(start), MapUtils.staggeredToNormal(end));
	}
	
	/**
	 * Calculates coordinate-pairs belonging to the selection. <br/>
	 * The selection is a rectangle based on the start and end point. <br/>
	 * Coordinates are expected to represent tiles on a Cartesian map.
	 * @param start Coordinate
	 * @param end Coordinate
	 * @return an array of coordinates belonging to the selection rectangle
	 */
	public static Array<Vector2> getSelectionCoords(Vector2 start, Vector2 end) {
		Array<Vector2> selection = new Array<Vector2>();
		for (float x = start.x; hasNextTile(start.x, end.x, x); nextTile(start.x, end.x, x)) {
			for (float y = start.y; hasNextTile(start.y, end.y, y); nextTile(start.y, end.y, y)) {
				selection.add(new Vector2(x, y));
			}
		}
		return selection;
	}
	
	private static void nextTile(float start, float end, float a) {
		if (start > end) {
			a--;
		} else {
			a++;
		}
	}
	
	private static boolean hasNextTile(float start, float end, float a) {
		if (start > end) {
			return a > end;
		} else {
			return a < end;
		}
	}
	
}
