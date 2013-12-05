package de.micralon.engine.systems;

import com.badlogic.gdx.utils.ObjectMap;

import de.micralon.engine.GameWorld;

public class StatisticSystem {
	private GameWorld world;
	private long levelStartTime;
	private ObjectMap<Integer, Integer> playerPoints = new ObjectMap<Integer, Integer>();
	private ObjectMap<Integer, Integer> playerKills = new ObjectMap<Integer, Integer>();
	private ObjectMap<Integer, Integer> playerDeaths = new ObjectMap<Integer, Integer>();
	
	// temp vars
	int levelSeconds;
	
	public StatisticSystem(GameWorld world) {
		this.world = world;
	}
	
	public void startLevel() {
		levelStartTime = world.getGameTime();
	}
	
	public int getLevelTime() {
		return (int) ((levelStartTime - world.getGameTime()) / 1000);
	}
	
	public String getLevelTimeAsString() {
		levelSeconds = getLevelTime();
		return String.valueOf(levelSeconds/60) + ":" + String.valueOf(levelSeconds%60);
	}
	
	public ObjectMap<Integer, Integer> getPlayerPoints() {
		return playerPoints;
	}
	
	public int getPlayerPoints(int playerID) {
		return playerPoints.get(playerID) != null ? playerPoints.get(playerID) : 0;
	}
	
	public void addPoints(int playerID, int points) {
		playerPoints.put(playerID, getPlayerPoints(playerID)+points);
	}
	
	public ObjectMap<Integer, Integer> getPlayerKills() {
		return playerKills;
	}
	
	public int getPlayerKills(int playerID) {
		return playerKills.get(playerID) != null ? playerKills.get(playerID) : 0;
	}
	
	public void addKill(int playerID) {
		playerKills.put(playerID, getPlayerKills(playerID)+1);
	}
	
	public ObjectMap<Integer, Integer> getPlayerDeaths() {
		return playerDeaths;
	}
	
	public int getPlayerDeaths(int playerID) {
		return playerDeaths.get(playerID) != null ? playerDeaths.get(playerID) : 0;
	}
	
	public void addDeath(int playerID) {
		playerDeaths.put(playerID, getPlayerDeaths(playerID)+1);
	}
}
