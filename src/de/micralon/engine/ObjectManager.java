package de.micralon.engine;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectMap.Values;

import de.micralon.engine.net.Network.ObjectsData;

public class ObjectManager  {
//	private transient Array<GameObject<?>> objects = new Array<GameObject<?>>();
	private transient ObjectMap<Long, GameObject<?>> objectMap;
	private transient Array<GameObject<?>> deleteList;
	private transient Array<Long> updateList;
	private long objectIDseq = 1;
	
	private boolean trackUpdates = false;
	
	// temp vars
	private GameObject<?> tmpObj;
	private ObjectsData objectsData;
	
	public ObjectManager() {
		this(32);
	}
	
	public ObjectManager(int initCapacity) {
		this(initCapacity,false);
	}
	
	public ObjectManager(int initCapacity, boolean trackUpdates) {
		this.trackUpdates = trackUpdates;
		
		objectMap = new ObjectMap<Long, GameObject<?>>(initCapacity);
		deleteList = new Array<GameObject<?>>(initCapacity/2);
		if (trackUpdates) updateList = new Array<Long>(initCapacity);
	}

	public void add(GameObject<?> obj) {
//		objects.add(obj);
		obj.setObjectID(objectIDseq++, true);
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
	
	public void needUpdate(GameObject<?> obj) {
		if (trackUpdates) updateList.add(obj.getObjectID());
	}
	
	public ObjectsData getUpdateData() {
		if (trackUpdates) {
			objectsData = new ObjectsData();
			for (Long id : updateList) {
				objectsData.objects.add(objectMap.get(id).getData());
			}
			updateList.clear();
			return objectsData;
		} else {
			return null;
		}
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
		for (GameObject<?> obj : objectMap.values()) {
			// update object state
			obj.update();
			// check for destroyed objects
			if (obj instanceof Destructible && !((Destructible) obj).isDestroyed() && ((Destructible) obj).getDamageModel().getHealth() <= 0) {
				((Destructible) obj).setDestroyed();
			}
		}
		
		// delete objects
		for (GameObject<?> obj : deleteList) {
			obj.destroy();
		}
		deleteList.clear();
	}

}
