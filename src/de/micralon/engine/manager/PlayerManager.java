package de.micralon.engine.manager;

import java.util.HashMap;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

import de.micralon.engine.GameWorld;
import de.micralon.engine.Player;
import de.micralon.engine.Team;

public class PlayerManager {
	private HashMap<Integer, Player> players = new HashMap<Integer, Player>();
	private ObjectMap<Integer, Team> teams = new ObjectMap<Integer, Team>();
	private int nextPlayerID;
	private int nextTeamID;
	
	// temp vars
	Player player;
	
	public PlayerManager() {
		
	}
	
	public void addPlayer(Player player) {
		player.ID = ++nextPlayerID;
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
	
	public void addTeam(Team team) {
		team.ID = ++nextTeamID;
		teams.put(team.ID, team);
	}
	
	public Team getTeam(Integer key) {
		return teams.get(key);
	}
	
	public Team getTeam(String name) {
		for (Team team : teams.values()) {
			if (team.getName().equals(name)) {
				return team;
			}
		}
		return null;
	}
	
	public Array<Team> getTeams() {
		return teams.values().toArray();
	}
	
	public HashMap<Integer, Player> getData() {
		//TODO: include teams here
		return players;
	}
	
	public void setData(HashMap<Integer, Player> players) {
		this.players = players;
		for (Player player : players.values()) {
			if (nextPlayerID < player.ID) nextPlayerID = player.ID;
		}
	}
}
