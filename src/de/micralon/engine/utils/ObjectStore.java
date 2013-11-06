package de.micralon.engine.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

public abstract class ObjectStore<T> implements Disposable {
	private int peak = 0;
	protected final Array<T> usedObjects = new Array<T>();
	protected final Array<T> freeObjects = new Array<T>();
	
	protected T tempT;
	
	protected abstract T create();
	
	/**
	 * Get a new or already freed Object
	 * @return a object
	 */
	public T get() {
		if (freeObjects.size > 0) {
			tempT = freeObjects.removeIndex(0);
		} else {
			tempT = create();
			peak++;
		}
		usedObjects.add(tempT);
		Gdx.app.log("ObjectStore", "get(). new size: "+this.toString());
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
	
	public int getPeak() {
		return peak;
	}
	
	@Override
	public String toString() {
		return "free: "+String.valueOf(freeObjects.size)+" / used: "+String.valueOf(usedObjects.size);
	}
	
	public void clear() {
		tempT = null;
		usedObjects.clear();
		freeObjects.clear();
		Gdx.app.log("ObjectStore", "cleared store");
	}
	
	@Override
	public void dispose() {
		clear();
	}
}
