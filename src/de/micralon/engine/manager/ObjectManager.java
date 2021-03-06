package de.micralon.engine.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectMap.Values;

import de.micralon.engine.Destructible;
import de.micralon.engine.gameobjects.GameObject;
import de.micralon.engine.net.Network.ObjectData;
import de.micralon.engine.net.Network.ObjectsData;

public class ObjectManager implements Disposable {
//	private transient Array<GameObject<?>> objects = new Array<GameObject<?>>();
	private transient ObjectMap<Long, GameObject> objectMap;
	private transient Array<GameObject> deleteList;
//	private transient Array<BodyBuilder> bodyBuildList;
	private transient Array<Long> updateList;
	private long objectIDseq = 1;
	
	private boolean trackUpdates = false;
	
	private static final int DEFAULT_CAPACITY = 32;
	
	// temp vars
	private GameObject tmpObj;
	private ObjectsData objectsData;
	
	public ObjectManager() {
		this(DEFAULT_CAPACITY);
	}
	
	public ObjectManager(int initCapacity) {
		this(initCapacity, false);
	}
	
	public ObjectManager(boolean trackUpdates) {
		this(DEFAULT_CAPACITY, trackUpdates);
	}
	
	public ObjectManager(int initCapacity, boolean trackUpdates) {
		this.trackUpdates = trackUpdates;
		
		objectMap = new ObjectMap<Long, GameObject>(initCapacity);
		deleteList = new Array<GameObject>(initCapacity/2);
//		bodyBuildList = new Array<BodyBuilder>(initCapacity/2);
		if (trackUpdates) updateList = new Array<Long>(initCapacity);
	}
	
//	public BodyBuilder newBody() {
//		BodyBuilder bodyBuilder = new BodyBuilder(GameWorld.ctx.box2dWorld);
//		bodyBuildList.add(bodyBuilder);
//		return bodyBuilder;
//	}

	public void add(GameObject obj) {
//		objects.add(obj);
		obj.setObjectID(objectIDseq++, true);
		objectMap.put(obj.getObjectID(), obj);
	}
	
	/**
	 * Remove a object from the world. 
	 * NOTE: this adds the object to a list of objects to delete. The object gets actually deleted when safe to do so (ie. outside of a world step).
	 * @param obj GameObject to remove
	 */
	public void remove(GameObject obj) {
//		objectMap.remove(obj.getObjectID());
//		deleteList.add(obj);
		deleteList.add(objectMap.remove(obj.getObjectID()));
	}
	
	public void changeID(long oldID, long newID) {
		tmpObj = objectMap.remove(oldID);
		objectMap.put(newID, tmpObj);
	}
	
	public void needUpdate(GameObject obj) {
		if (trackUpdates) updateList.add(obj.getObjectID());
	}
	
	public ObjectsData getUpdateData() {
		objectsData = new ObjectsData();
		if (trackUpdates) { // delta update
			for (Long id : updateList) {
				objectsData.objects.add(objectMap.get(id).getData());
			}
			updateList.clear();
		} else { // Full object data update
			for (GameObject obj : objectMap.values()) {
				objectsData.objects.add(obj.getData());
			}
		}
		return objectsData;
	}
	
	public void syncObjects(ObjectsData data) {
		for (ObjectData objData : data.objects) {
			objectMap.get(objData.objectID).setData(objData);
		}
	}
	
	/**
	 * EXPERIMENTAL
	 * @param klass
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> Array<T> getObjectsByClass(Class<T> klass) {
		Array<T> result = new Array<T>();
		for (GameObject obj : objectMap.values()) {
			if (klass.isInstance(obj)) {
				result.add((T) obj);
			}
		}
		return result;
	}
	
	public GameObject getObject(long objectID) {
		return objectMap.get(objectID);
	}
	
	public Values<GameObject> getObjects() {
		return objectMap.values();
	}
	
	/**
	 * Updates the handled objects and actually removes objects which are marked to delete.
	 * Make sure to call this method AFTER your world step. 
	 */
	public void update() {
		// delete objects
		for (GameObject obj : deleteList) {
			if (obj != null) obj.destroy(); // destroy GameObject i.e. destroy body, free lights, ...
		}
		deleteList.clear();
		
		for (GameObject obj : objectMap.values()) {
			if (obj != null) {
				// update object state
				obj.update();
				// check for destroyed objects
				if (obj instanceof Destructible && !((Destructible) obj).isDestroyed() && ((Destructible) obj).getDamageModel().getHealth() <= 0) {
					((Destructible) obj).setDestroyed();
				}
			} else {
				// TODO: what is wrong here... why is obj == null?!
				Gdx.app.log("ObjectManager", "WARNING: obj is null!");
				return;
			}
		}
		
		// create new bodies
//		for (BodyBuilder bodyBuilder : bodyBuildList) {
//			bodyBuilder.build();
//		}
	}

	@Override
	public void dispose() {
		objectMap.clear();
		if (trackUpdates) updateList.clear();
		deleteList.clear();
	}

}
