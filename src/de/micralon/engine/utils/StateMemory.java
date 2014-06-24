package de.micralon.engine.utils;

import com.badlogic.gdx.utils.ObjectMap;

public class StateMemory {
	private ObjectMap<String, State> stateMemory;
	
	public void register(String name, State state) {
		if (stateMemory == null) initStateMemory();
		stateMemory.put(name, state);
	}
	
	public State get(String name) {
		return stateMemory.get(name);
	}
	
	private void initStateMemory() {
		stateMemory = new ObjectMap<String, State>();
	}
}
