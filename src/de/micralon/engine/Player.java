package de.micralon.engine;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class Player {
	public int ID;
	public String name;
	public Color color = Color.RED;
	
	public transient Camera camera;
	
	public transient GameObject<?> character;
	
	public Player() {}
	
	public Player(String name) {
		this(0, name);
	}
	
	public Player(int ID, String name) {
		this.ID = ID;
		this.name = name;
		
		//TODO: set correct viewport
		camera = new OrthographicCamera(10, 10);
	}
}
