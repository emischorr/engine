package de.micralon.engine;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

import de.micralon.engine.map.MapBuilder;
import de.micralon.engine.map.ObjectMapper;
import de.micralon.engine.scripting.Scene;

public abstract class Level extends Scene {
	
	public void load() {}
	
	public void playerDied(int playerID) {
		GameWorld.ctx.playerManager.killPlayer(playerID);
	}
	
	protected final void buildMap(String mapName) {
		buildMap(mapName, GameWorld.ctx.getObjectMapper(), 1);
	}
	
	protected final void buildMap(String mapName, ObjectMapper objectMapper, float tileSize) {
		TiledMap map = new TmxMapLoader().load("maps/"+mapName+".tmx");
		MapBuilder mapBuilder = new MapBuilder(GameWorld.ctx, "maps/materials.xml", tileSize);
		mapBuilder.addObjectMapper(objectMapper);
		mapBuilder.build(map);
	}
}
