package de.micralon.engine;

import com.badlogic.gdx.physics.box2d.World;

public class Physics {
	private final static int MAX_FPS = 30;
	private final static int MIN_FPS = 10;
	public final static float TIME_STEP = 1f / MAX_FPS;
	private final static float MAX_STEPS = 1f + MAX_FPS / MIN_FPS;
	private final static float MAX_TIME_PER_FRAME = TIME_STEP * MAX_STEPS;
	private final static int VELOCITY_ITERS = 1;
	private final static int POSITION_ITERS = 1;

	static public float physicsTimeLeft;

	static public int update(final World world, float deltaTime) {
		physicsTimeLeft += deltaTime;
		if (physicsTimeLeft > MAX_TIME_PER_FRAME) {
			physicsTimeLeft = MAX_TIME_PER_FRAME;
		}

		int steps = 0;
		while (physicsTimeLeft >= TIME_STEP) {
			world.step(TIME_STEP, VELOCITY_ITERS, POSITION_ITERS);
			physicsTimeLeft -= TIME_STEP;
			steps++;
		}
		return steps;
	}
}
