package de.micralon.engine.utils;

import com.badlogic.gdx.Gdx;

public class Log {
	public static String log_topic = "EngineGame";
	
	public static void setTopic(String topic) {
		log_topic = topic;
	}
	
	//TODO: create log levels
	
	public static void debug(String message) {
		Gdx.app.log( log_topic, message );
	}
	
	public static void info(String message) {
		Gdx.app.log( log_topic, message );
	}
	
	public static void warn(String message) {
		Gdx.app.log( log_topic, message );
	}
	
	public static void error(String message) {
		Gdx.app.log( log_topic, message );
	}
}
