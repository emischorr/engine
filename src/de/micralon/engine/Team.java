package de.micralon.engine;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Array;

public class Team {
	private final Array<Player> members = new Array<Player>();
	private final String name;
	private final Color color;
	public int ID;
	
	public Team(String name, Color color) {
		this.name = name;
		this.color = color;
	}
	
	public void addPlayer(Player player) {
		player.team = this;
		members.add(player);
	}
	
	public Color getColor() {
		return color.cpy();
	}
	
	public String getName() {
		return name;
	}
	
	public boolean isMember(int playerID) {
		for (Player player : members) {
			if (player.ID == playerID) {
				return true;
			}
		}
		return false;
	}
}
