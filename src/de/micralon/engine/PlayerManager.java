package de.micralon.engine;

import com.badlogic.gdx.utils.Array;

public class PlayerManager {
	private Array<Player> players;
	
	public PlayerManager() {
		
	}
	
	public void addPlayer(Player player) {
		players.add(player);
	}
	
	public void removePlayer(Player player) {
		players.removeValue(player, true);
	}
}
