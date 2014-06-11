package de.micralon.engine.map;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

public class GameMap {
	private final ObjectMap<String, Integer> layers = new ObjectMap<String, Integer>();
	private final ObjectMap<String, TileStack> fields = new ObjectMap<String, TileStack>();
	
	private int mapWidth, mapHeight;
	
	public GameMap(int width, int height) {
		this.mapWidth = width;
		this.mapHeight = height;
		layers.put("bg", 0);
	}
	
	public int getWidth() {
		return mapWidth;
	}
	
	public int getHeight() {
		return mapHeight;
	}
	
	public TileStack getField(String key) {
		return fields.get(key);
	}
	
	/**
	 * Creates a new field belonging to this map. <br/>
	 * <b>Must be added explicitly!</b> 
	 * @return a new TileStack
	 */
	public TileStack createField() {
		return new TileStack(this);
	}
	
	public void addField(TileStack field, String key) {
		fields.put(key, field);
	}
	
	public Tile getTile(Vector2 coords, String layer) {
		return getTile(coords.x, coords.y, layer);
	}
	
	public Tile getTile(float x, float y, String layer) {
		return fields.get(key(x, y)).getTile(layers.get(layer));
	}
	
	public Array<Tile> getLayerTiles(String layer) {
		Array<Tile> tiles = new Array<Tile>();
		for (TileStack field : fields.values()) {
			tiles.add(field.getTile(layer));
		}
		return tiles;
	}

	public int getLayerId(String layer) {
		return layers.get(layer);
	}

	/**
	 * Adds a layer with a name (if not already added).
	 * @param name the name of the new layer
	 */
	public synchronized void addLayer(String name) {
		if (!layers.containsKey(name)) layers.put(name, layers.size);
	}

	public Array<String> getLayerNames() {
		return layers.keys().toArray();
	}
	
	private String key(float x, float y) {
		return x+"/"+y;
	}
}
