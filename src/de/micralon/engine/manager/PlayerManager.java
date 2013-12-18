package de.micralon.engine.manager;

import java.util.HashMap;

import de.micralon.engine.GameWorld;
import de.micralon.engine.Player;

public class PlayerManager {
	private HashMap<Integer, Player> players = new HashMap<Integer, Player>();
	private int nextID;
	
	// temp vars
	Player player;
	
	public PlayerManager() {
		
	}
	
	public void addPlayer(Player player) {
		player.ID = ++nextID;
		players.put(player.ID, player);
	}
	
	public Player getPlayer(int id) {
		return players.get(id);
	}
	
	public void removePlayer(int id) {
		players.remove(id);
	}
	
	public void removePlayer(Player player) {
		removePlayer(player.ID);
	}
	
	public int playerCount() {
		return players.size();
	}
	
	public void killPlayer(int id) {
		player = players.get(id);
		if (player.alive) {
			player.alive = false;
			player.timeOfDeath = GameWorld.ctx.getGameTime();
		}
	}
	
	public boolean revivePlayer(int id, long time) {
		player = players.get(id);
		if (!player.alive && player.timeOfDeath + time <= GameWorld.ctx.getGameTime()) {
			player.alive = true;
			return true;
		} else {
			return false;
		}
	}
	
	public HashMap<Integer, Player> getData() {
		return players;
	}
	
	public void setData(HashMap<Integer, Player> players) {
		this.players = players;
		for (Player player : players.values()) {
			if (nextID < player.ID) nextID = player.ID;
		}
	}
}
