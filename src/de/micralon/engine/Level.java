package de.micralon.engine;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

import de.micralon.engine.scripting.Scene;


public abstract class Level extends Scene {
	
	public void load() {}
	
	protected final void buildMap(String mapName) {
		TiledMap map = new TmxMapLoader().load("maps/"+mapName+".tmx");
		MapBuilder mapBuilder = new MapBuilder(GameWorld.ctx, "maps/materials.xml", 2f);
		mapBuilder.build(map);
	}
}
