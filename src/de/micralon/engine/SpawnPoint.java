package de.micralon.engine;

public class SpawnPoint implements SpawnPointInterface {
	public float posX, posY;
	
	public SpawnPoint() {}
	
	public SpawnPoint(float posX, float posY) {
		this.posX = posX;
		this.posY = posY;
	}

	@Override
	public void spawn(GameObject<?> obj) {
		obj.setPos(posX, posY);
	}

}
