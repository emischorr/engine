package de.micralon.engine;

public class LastingEffect {
	private GameWorld world;
	private long duration;
	private long startTime = 0;
	private boolean timedOut = false;
	
	public LastingEffect(GameWorld world, long duration) {
		this.world = world;
		this.duration = duration;
	}
	
	public void startEffect(Object self) {}
	public void timeEffect(Object self) {}
	public void endEffect(Object self) {}
	
	public final void update(Object self, float delta) {
		if (!timedOut) {
			if (startTime == 0) {
				startTime = world.getGameTime();
				startEffect(self);
			}
			
			if (startTime + duration > world.getGameTime()) {
				timeEffect(self);
			} else {
				timedOut = true;
				endEffect(self);
			}
		}
	}
	
	public final boolean isTimedOut() {
		return timedOut;
	}
}
