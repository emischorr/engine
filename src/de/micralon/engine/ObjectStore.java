package de.micralon.engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;

public abstract class ObjectStore<T> {
	
	private Array<T> usedObjects = new Array<T>();
	private Array<T> freeObjects = new Array<T>();
	
	private T tempT;
	
	public abstract T create();
	
	public T get() {
		if (freeObjects.size > 0) {
			tempT = freeObjects.removeIndex(0);
			usedObjects.add(tempT);
			return tempT;
		} else {
			tempT = create();
			usedObjects.add(tempT);
			return tempT;
		}
	}
	
	public void free(T obj) {
		if (usedObjects.removeValue(obj, true)) {
			freeObjects.add(obj);
		} else {
			Gdx.app.log("ObjectStore", "WARNING: Object ("+obj.getClass().toString()+") could not be freed");
		}
	}
	
	public void clear() {
		tempT = null;
		usedObjects = null;
		freeObjects = null;
	}
}
