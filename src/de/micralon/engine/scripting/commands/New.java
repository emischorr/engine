package de.micralon.engine.scripting.commands;

import de.micralon.engine.GameObject;
import de.micralon.engine.GameWorld;
import de.micralon.engine.scripting.Command;

public class New implements Command {
	private GameObject object;

	public New(GameObject object) {
		this.object = object;
	}

	@Override
	public void update(float delta) {
		GameWorld.ctx.addObject(object);
	}

	@Override
	public boolean isDone() {
		return true;
	}

	@Override
	public Command copy() {
//		return new New(object.copy());
		return null;
	}

}
