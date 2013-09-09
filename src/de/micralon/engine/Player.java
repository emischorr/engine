package de.micralon.engine;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class Player {
	public String name;
	public Color color;
	
	public transient Camera camera;
	
	public transient GameObject<?> character;
	
	public Player() {}
	
	public Player(String name) {
		this.name = name;
		
		//TODO: set correct viewport
		camera = new OrthographicCamera(10, 10);
	}
}
