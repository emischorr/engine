package de.micralon.engine.manager;

import java.util.HashMap;

import de.micralon.engine.Player;

public class PlayerManager {
	private HashMap<Integer, Player> players = new HashMap<Integer, Player>();
	private int nextID;
	
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
