package de.micralon.engine;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectMap.Values;

public class ObjectManager  {
//	private transient Array<GameObject<?>> objects = new Array<GameObject<?>>();
	private transient ObjectMap<Long, GameObject<?>> objectMap;
	private transient Array<GameObject<?>> deleteList;
	private long objectIDseq = 0;
	
	// temp vars
	private GameObject<?> tmpObj;
	
	public ObjectManager() {
		this(32);
	}
	
	public ObjectManager(int initCapacity) {
		objectMap = new ObjectMap<Long, GameObject<?>>(initCapacity);
		deleteList = new Array<GameObject<?>>(initCapacity/2);
	}

	public void add(GameObject<?> obj) {
//		objects.add(obj);
		obj.setObjectID(objectIDseq++);
		objectMap.put(obj.getObjectID(), obj);
	}
	
	/**
	 * Remove a object from the world. 
	 * NOTE: this adds the object to a list of objects to delete. The object gets actually deleted when safe to do so (ie. outside of a world step).
	 * @param obj GameObject to remove
	 */
	public void remove(GameObject<?> obj) {
//		objects.removeValue(obj, true);
		objectMap.remove(obj.getObjectID());
		deleteList.add(obj);
	}
	
	public void changeID(long oldID, long newID) {
		tmpObj = objectMap.remove(oldID);
		objectMap.put(newID, tmpObj);
	}
	
	/**
	 * EXPERIMENTAL
	 * @param klass
	 * @return
	 */
	public Array<?> getObjectsByClass(Class<?> klass) {
		Array<GameObject<?>> result = new Array<GameObject<?>>();
		for (GameObject<?> obj : objectMap.values()) {
			if (klass.isInstance(obj)) {
				result.add(obj);
			}
		}
		return result;
	}
	
	public GameObject<?> getObject(long objectID) {
		return objectMap.get(objectID);
	}
	
	public Values<GameObject<?>> getObjects() {
		return objectMap.values();
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
		for (GameObject<?> obj : objectMap.values()) {
			obj.update();
		}
	}

}
