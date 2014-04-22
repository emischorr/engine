package de.micralon.engine.map;

import com.badlogic.gdx.utils.ObjectMap;

public class TileStack {
	private final GameMap map;
	private final ObjectMap<Integer, Tile> tiles = new ObjectMap<Integer, Tile>();
	
	public TileStack(GameMap map) {
		this.map = map;
	}
	
	public void addTile(Tile tile, String layer) {
		addTile(tile, map.getLayerId(layer));
	}
	
	public void addTile(Tile tile, int layer) {
		tiles.put(layer, tile);
	}
	
	public Tile getTile(String layer) {
		return tiles.get(map.getLayerId(layer));
	}
	
	public Tile getTile(int key) {
		return tiles.get(key);
	}

	public boolean isEmpty() {
		return tiles.size == 0;
	}
}
