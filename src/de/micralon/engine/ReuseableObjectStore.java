package de.micralon.engine;

import com.badlogic.gdx.Gdx;

public abstract class ReuseableObjectStore<T extends Reuseable> extends ObjectStore<T> {

	private T tempT;
	
	/**
	 * Get a new or already freed Object
	 * @return a object
	 */
	public T get() {
		if (freeObjects.size > 0) {
			tempT = freeObjects.removeIndex(0);
			tempT.reuse();
		} else {
			tempT = create();
		}
		usedObjects.add(tempT);
		Gdx.app.log("ObjectStore", "get(). Size: "+this.toString());
		return tempT;
	}
	
}
