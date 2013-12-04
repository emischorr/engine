package de.micralon.engine.scripting;

import com.badlogic.gdx.utils.Array;

import de.micralon.engine.GameObject;
import de.micralon.engine.scripting.commands.New;

public class Scene {
	protected final Array<Command> commands = new Array<Command>();

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
	
	protected final void add(Command command) {
		commands.add(command);
	}
	
	protected void add(GameObject object) {
        commands.add(new New(object));
	}
}
