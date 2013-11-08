package de.micralon.engine.commands;

public interface Command {
	public void update(float delta);
    public boolean isDone();
    public Command copy();
}
