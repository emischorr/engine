package de.micralon.engine.scripting.commands;

import de.micralon.engine.GameWorld;
import de.micralon.engine.scripting.Command;

public class NextLevel implements Command {
	private String level;
    
    public NextLevel(String level) {
    	this.level = level;
    }
    
    @Override
    public void update (float delta) {
    	GameWorld.ctx.load(level);
    }

    @Override
    public boolean isDone () {
    	return true;
    }

    @Override
    public Command copy () {
    	return this;
    }
}
