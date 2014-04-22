package de.micralon.engine;

public class GameTimer {
	private long gameTime;
	
	/**
	 * 
	 * @return the game time in seconds
	 */
	public long getTime() {
		return gameTime/1000;
	}
	
	/**
	 * 
	 * @return the game time in milliseconds
	 */
	public long getTimeInMs() {
		return gameTime;
	}
	
	/**
	 * updates the game time
	 * @param deltaTime the time since the last update in seconds
	 */
	public final void update(float deltaTime) {
		gameTime = (long) (gameTime + deltaTime*1000);
		onUpdate();
	}
	
	protected void onUpdate() {}
	
	@Override
	public String toString() {
		return getTime() + " Seconds";
	}
}
