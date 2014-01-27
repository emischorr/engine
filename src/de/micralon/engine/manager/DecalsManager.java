package de.micralon.engine.manager;

import com.badlogic.gdx.utils.Array;

import de.micralon.engine.GameDecal;

public class DecalsManager {
	private Array<GameDecal> decals = new Array<GameDecal>();
	
	public void showDecals(boolean show) {
		for(GameDecal decal : decals) {
			decal.show = show;
		}
	}
	
	public GameDecal addDecal(GameDecal decal) {
		decals.add(decal);
		return decal;
	}

	public void update(float delta) {
		for(GameDecal decal : decals) {
			decal.update(delta);
		}
	}
}
