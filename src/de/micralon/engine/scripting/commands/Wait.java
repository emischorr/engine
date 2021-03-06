package de.micralon.engine.scripting.commands;

import de.micralon.engine.scripting.Command;

public class Wait implements Command {
	float duration;
    float stateTime;
    
    public Wait(float duration) {
    	this.duration = duration;
    }

    @Override
    public void update (float delta) {
    	stateTime += delta;
    }

    @Override
    public boolean isDone () {
    	return stateTime >= duration;
    }

    @Override
    public Command copy () {
    	return new Wait(duration);
    }
}
