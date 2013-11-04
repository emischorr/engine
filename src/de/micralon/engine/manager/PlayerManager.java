package de.micralon.engine.manager;

import com.badlogic.gdx.utils.Array;

import de.micralon.engine.Player;

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
