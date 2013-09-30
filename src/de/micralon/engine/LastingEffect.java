package de.micralon.engine;

public class LastingEffect {
	private float duration;
	private long startTime = 0;
	private boolean timedOut = false;
	
	public LastingEffect(float duration) {
		this.duration = duration;
	}
	
	private void effect() {}
	
	public void update(float delta) {
		if (!timedOut) {
			if (startTime == 0) startTime = System.currentTimeMillis();
			
			if (startTime + duration >= System.currentTimeMillis()) {
				effect();
			} else {
				timedOut = true;
			}
		}
	}
	
	public boolean isTimedOut() {
		return timedOut;
	}
}
