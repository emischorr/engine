package de.micralon.engine.scripting.commands;

import de.micralon.engine.scripting.Command;

public class MoveTo implements Command {
	private float x, y, speed;
	
	public MoveTo(float x, float y, float speed) {
		this.x = x;
		this.y = y;
		this.speed = speed;
	}

	@Override
	public void update(float delta) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isDone() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Command copy() {
		// TODO Auto-generated method stub
		return null;
	}

}
