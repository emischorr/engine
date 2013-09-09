package de.micralon.engine;

public interface GameStateInterface {
	public float getGravity();
	public void addObject(GameObject<?> obj);
	public void removeObject(GameObject<?> obj);
	public void addPlayer(Player player, boolean local);
	public void update();
}
