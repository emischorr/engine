package de.micralon.engine.utils;

import com.badlogic.gdx.Gdx;

public class Log {
	public static String log_topic = "EngineGame";
	
	public static void setTopic(String topic) {
		log_topic = topic;
	}
	
	public static void info(String message) {
		Gdx.app.log( log_topic, message );
	}
}
