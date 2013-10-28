package de.micralon.engine;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;


public abstract class Level<WORLD> {
	protected final WORLD world;
	
	public Level(WORLD world) {
		this.world = world;
	}
	public void load() {}
	public void update(float deltaTime) {}
	
	protected final void buildMap(GameWorld world, String mapName) {
		TiledMap map = new TmxMapLoader().load("maps/"+mapName+".tmx");
		MapBuilder mapBuilder = new MapBuilder(world, "maps/materials.xml", 2f);
		mapBuilder.build(map);
	}
}
