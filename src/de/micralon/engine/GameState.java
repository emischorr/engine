package de.micralon.engine;

import com.badlogic.gdx.utils.Array;

public class GameState<WORLD extends AbstractGameWorld<?>> implements GameStateInterface {
	public transient WORLD world;
	private float gravity = -9.8f;
	
	private ObjectManager objectManager;
	
	public Array<Player> players = new Array<Player>();
	
	public GameState() {
		objectManager = new ObjectManager();
	}
	
	@Override
	public float getGravity() {
		return gravity;
	}
	
	@Override
	public void addObject(GameObject<?> obj) {
		objectManager.add(obj);
		world.physics.addActor(obj);
	}
	
	@Override
	public void removeObject(GameObject<?> obj) {
		objectManager.remove(obj);
	}
	
	@Override
	/**
	 * Only called by server
	 */
	public synchronized void addPlayer(Player player, boolean local) {
		player.ID = players.size;
		players.add(player);
		// add player to local world
		if (local) {
			world.localPlayer = player;
		}
	}
	
	@Override
	public void update() {
		objectManager.update();
	}

}
