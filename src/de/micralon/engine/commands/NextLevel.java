package de.micralon.engine.commands;

import de.micralon.engine.Level;

public class NextLevel implements Command {
	private Level<?> level;
    
    public NextLevel(Level<?> level) {
            this.level = level;
    }
    
    @Override
    public void update (float delta) {
//            GameWorld.ctx.load(level);
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
