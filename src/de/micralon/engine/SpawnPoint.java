package de.micralon.engine;

public class SpawnPoint implements SpawnPointInterface {
	public float posX, posY;
	
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
	
	public void respawn(int playerID) {
		respawn(GameWorld.ctx.playerManager.getPlayer(playerID).character);
	}
	
	public void respawn(GameObject obj) {
		obj.reuse();
		spawn(obj);
	}

}
