package de.micralon.engine.scripting;

public interface Command {
	public void update(float delta);
    public boolean isDone();
    public Command copy();
}
