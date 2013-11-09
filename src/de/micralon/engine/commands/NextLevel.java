package de.micralon.engine.commands;

import de.micralon.engine.GameWorld;

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
