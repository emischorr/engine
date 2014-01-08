package de.micralon.engine;

import java.util.HashMap;
import java.util.Map.Entry;

public class SpawnPoint implements SpawnPointInterface {
	public float posX, posY;
	private HashMap<GameObject, Long> spawnQueue;
	
	public SpawnPoint() {}
	
	public SpawnPoint(float posX, float posY) {
		this.posX = posX;
		this.posY = posY;
	}

	@Override
	public void spawn(GameObject obj) {
		obj.setPos(posX, posY);
		GameWorld.ctx.addObject(obj);
	}
	
	/**
	 * Spawns a GameObject after a timer
	 * @param obj
	 * @param time in milliseconds
	 */
	public void spawnIn(GameObject obj, float time) {
		if (spawnQueue == null) initSpawnQueue();
		spawnQueue.put(obj, (long) (GameWorld.ctx.getGameTime() + time));
	}
	
	public void respawn(int playerID) {
		respawn(GameWorld.ctx.playerManager.getPlayer(playerID).character);
	}
	
	public void respawn(GameObject obj) {
		obj.reuse();
		spawn(obj);
	}
	
	public void update() {
		if (spawnQueue != null) {
			for (Entry<GameObject, Long> entry : spawnQueue.entrySet()) {
				if (entry.getValue() <= GameWorld.ctx.getGameTime()) {
					spawn(entry.getKey());
					spawnQueue.remove(entry);
				}
			}
		}
	}
	
	private void initSpawnQueue() {
		spawnQueue = new HashMap<GameObject, Long>();
	}

}
