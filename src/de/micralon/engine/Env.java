package de.micralon.engine;

import com.badlogic.gdx.math.Vector2;

public class Env {

	public static EngineGame game;
	
	// Physics
	public static Vector2 gravity;
	public static int velocityIterations;
	public static int positionIterations;
	
	private static Settings settings;
	
	public static void init(EngineGame game) {
		Env.game = game;
		
		settings = new Settings("data/config/globals.xml");
		
		gravity = settings.getVector("gravity", new Vector2(0, -9.8f));
		velocityIterations = settings.getInt("velocityIterations", 6);
		positionIterations = settings.getInt("positionIterations", 10);
	}
	
}
