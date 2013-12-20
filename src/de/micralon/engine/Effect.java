package de.micralon.engine;

import com.badlogic.gdx.utils.Disposable;

public abstract class Effect implements Disposable {
	protected String effectName;
	protected boolean complete = false;

	public String getEffectName() {
		return effectName;
	}
	
	public void update(float deltaTime) {
		complete = true;
	}
	
	public boolean isComplete() {
		return complete;
	}
	
	public void dispose() {
		
	}
}
