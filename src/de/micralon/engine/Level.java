package de.micralon.engine;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;


public abstract class Level {
	public static void load(GameWorld world) {}
	
	protected static final void buildMap(GameWorld world, String mapName) {
		TiledMap map = new TmxMapLoader().load("maps/"+mapName+".tmx");
		MapBuilder mapBuilder = new MapBuilder(world, "maps/materials.xml", 2f);
		mapBuilder.build(map);
	}
}
