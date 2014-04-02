package de.micralon.engine;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

import de.micralon.engine.map.MapBuilder;
import de.micralon.engine.map.ObjectMapper;
import de.micralon.engine.scripting.Scene;

public abstract class Level extends Scene {
	protected MapBuilder mapBuilder;
	
	public Level() {
		mapBuilder = new MapBuilder(GameWorld.ctx, EngineGame.assetRootFolder+"maps/materials.xml");
	}
	
	public void load() {}
	
	public void playerDied(int playerID) {
		GameWorld.ctx.playerManager.killPlayer(playerID);
	}
	
	protected final void buildMap(String mapName) {
		buildMap(mapName, GameWorld.ctx.getObjectMapper(), 1, false);
	}
	
	protected final void buildMap(String mapName, ObjectMapper objectMapper, float tileSize) {
		buildMap(mapName, objectMapper, tileSize, false);
	}
	
	protected final void buildMap(String mapName, ObjectMapper objectMapper, float tileSize, boolean mergeBodies) {
		TiledMap map = new TmxMapLoader().load(EngineGame.assetRootFolder+"maps/"+mapName+".tmx");
		mapBuilder.setTileSize(tileSize);
		mapBuilder.setMergeBodies(mergeBodies);
		mapBuilder.addObjectMapper(objectMapper);
		mapBuilder.build(map);
	}
}
