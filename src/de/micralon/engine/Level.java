package de.micralon.engine;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.utils.Array;

import de.micralon.engine.commands.Command;


public abstract class Level {
	protected final Array<Command> commands = new Array<Command>();
	
	public void load() {}
	
	public void update(float deltaTime) {
		while(commands.size > 0) {
            Command cmd = commands.get(0);
            cmd.update(deltaTime);
            if(cmd.isDone()) {
                    commands.removeIndex(0);
            } else {
                    break;
            }
		}
	}
	
	protected final void buildMap(String mapName) {
		TiledMap map = new TmxMapLoader().load("maps/"+mapName+".tmx");
		MapBuilder mapBuilder = new MapBuilder(GameWorld.ctx, "maps/materials.xml", 2f);
		mapBuilder.build(map);
	}
	
	protected final void add(Command command) {
		commands.add(command);
	}
}
