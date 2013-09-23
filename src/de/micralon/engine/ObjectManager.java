package de.micralon.engine;

import com.badlogic.gdx.utils.Array;

public class ObjectManager  {
	private transient Array<GameObject<?>> objects = new Array<GameObject<?>>();
	private transient Array<GameObject<?>> deleteList = new Array<GameObject<?>>();

	public void add(GameObject<?> obj) {
		objects.add(obj);
	}
	
	/**
	 * Remove a object from the world. 
	 * NOTE: this adds the object to a list of objects to delete. The object gets actually deleted when safe to do so (ie. outside of a world step).
	 * @param obj GameObject to remove
	 */
	public void remove(GameObject<?> obj) {
		objects.removeValue(obj, true);
		deleteList.add(obj);
	}
	
	public Array<?> getObjectsByClass(Class<?> klass) {
		Array<GameObject<?>> result = new Array<GameObject<?>>();
		for (GameObject<?> obj : objects) {
			if (klass.isInstance(obj)) {
				result.add(obj);
			}
		}
		return result;
	}
	
	public Array<GameObject<?>> getObjects() {
		return objects;
	}
	
	/**
	 * Updates the handled objects and actually removes objects which are marked to delete.
	 * Make sure to call this method AFTER your world step. 
	 */
	public void update() {
		// delete objects
		for (GameObject<?> obj : deleteList) {
			obj.destroy();
		}
		deleteList.clear();

		// update object state
		for (GameObject<?> obj : objects) {
			obj.update();
		}
	}

}
