package de.micralon.engine;

import java.util.Random;

import com.badlogic.gdx.utils.Array;

import de.micralon.engine.gameobjects.GameObject;

public class SpawnPointCluster implements SpawnPointInterface {
	private Random random = new Random();
	private Array<SpawnPoint> spawnpoints = new Array<SpawnPoint>();
	
	public void addSpawnPoint(SpawnPoint spawnpoint) {
		spawnpoints.add(spawnpoint);
	}
	
	public void respawn(int playerID) {
		respawn(GameWorld.ctx.playerManager.getPlayer(playerID).character);
	}
	
	public void respawn(GameObject obj) {
		obj.reuse();
		spawn(obj);
	}
	
	public void respawnIn(int playerID, float time) {
		respawnIn(GameWorld.ctx.playerManager.getPlayer(playerID).character, time);
	}
	
	public void respawnIn(GameObject obj, float time) {
		obj.reuse();
		spawnIn(obj, time);
	}
	
	@Override
	public void spawn(GameObject obj) {
		spawnpoints.get(random.nextInt(spawnpoints.size)).spawn(obj);
	}
	
	public void spawnIn(GameObject obj, float time) {
		spawnpoints.get(random.nextInt(spawnpoints.size)).spawnIn(obj, time);
	}
	
	public void update() {
		for (SpawnPoint spawnpoint : spawnpoints) {
			spawnpoint.update();
		}
	}
	
	public void clear() {
		spawnpoints.clear();
	}

}
