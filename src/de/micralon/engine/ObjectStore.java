package de.micralon.engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;

public abstract class ObjectStore<T> {
	
	protected final Array<T> usedObjects = new Array<T>();
	protected final Array<T> freeObjects = new Array<T>();
	
	protected T tempT;
	
	public abstract T create();
	
	/**
	 * Get a new or already freed Object
	 * @return a object
	 */
	public T get() {
		if (freeObjects.size > 0) {
			tempT = freeObjects.removeIndex(0);
		} else {
			tempT = create();
		}
		usedObjects.add(tempT);
		Gdx.app.log("ObjectStore", "get(). Size: "+this.toString());
		return tempT;
	}
	
	public void free(T obj) {
		if (usedObjects.removeValue(obj, true)) {
			freeObjects.add(obj);
		} else {
			Gdx.app.log("ObjectStore", "WARNING: Object ("+obj.getClass().toString()+") could not be freed");
		}
	}
	
	public Array<T> getObjects() {
		return usedObjects;
	}
	
	@Override
	public String toString() {
		return String.valueOf(freeObjects.size)+"/"+String.valueOf(usedObjects.size);
	}
	
	public void clear() {
		tempT = null;
		usedObjects.clear();
		freeObjects.clear();
		Gdx.app.log("ObjectStore", "cleared store");
	}
}
