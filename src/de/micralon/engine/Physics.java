package de.micralon.engine;

public class Physics {
	private final static int MAX_FPS = 30;
	private final static int MIN_FPS = 15;
	public final static float TIME_STEP = 1f / MAX_FPS;
	private final static float MAX_STEPS = 1f + MAX_FPS / MIN_FPS;
	private final static float MAX_TIME_PER_FRAME = TIME_STEP * MAX_STEPS;

	static public float physicsTimeLeft;

	/**
	 * update the physics
	 * @param world The world that should stepped over
	 * @param deltaTime Time slot for physic update
	 * @return number of physics steps executed
	 */
	static public int update(final GameWorld world, float deltaTime) {
		physicsTimeLeft += deltaTime;
		if (physicsTimeLeft > MAX_TIME_PER_FRAME) {
			physicsTimeLeft = MAX_TIME_PER_FRAME;
		}

		int steps = 0;
		while (physicsTimeLeft >= TIME_STEP) {
			world.update(TIME_STEP);
			physicsTimeLeft -= TIME_STEP;
			steps++;
		}
		return steps;
	}
}
