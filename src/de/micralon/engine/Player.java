package de.micralon.engine;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;

import de.micralon.engine.gameobjects.GameObject;

public class Player {
	public int ID;
	public String name = "Player";
	public Color color = Color.RED;
	public boolean alive = true;
	public long timeOfDeath;
	public Team team;
	
	public transient Camera camera;
	
	public transient GameObject character;
	
	public Player() {}
	
	public Player(String name) {
		this.name = name;
		
		//TODO: set correct viewport
		camera = new OrthographicCamera(10, 10);
	}
}
