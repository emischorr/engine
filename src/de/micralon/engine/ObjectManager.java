package de.micralon.engine;

import com.badlogic.gdx.utils.Array;

public class ObjectManager  {
	private transient Array<GameObject<?>> objects = new Array<GameObject<?>>();
	private transient Array<GameObject<?>> deleteList = new Array<GameObject<?>>();

	public void add(GameObject<?> obj) {
		objects.add(obj);
	}
	
	public void remove(GameObject<?> obj) {
		objects.removeValue(obj, true);
		deleteList.add(obj);
	}
	
	public void update() {
		// delete objects
		for (GameObject<?> obj : deleteList) {
			obj.destroy();
		}
		deleteList.clear();

		// update object state
		for (GameObject<?> obj : objects) {
			obj.updateState();
		}
	}

}
