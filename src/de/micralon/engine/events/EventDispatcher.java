package de.micralon.engine.events;

import com.badlogic.gdx.utils.ObjectMap;

public class EventDispatcher {
	private ObjectMap<String, EventListenerCollection> listeners = new ObjectMap<String, EventListenerCollection>();
	
	public void register(String eventName, EventListener listener) {
		register(eventName, listener, false);
	}
	
	public void register(String eventName, EventListener listener, boolean autoRemove) {
		if (!listeners.containsKey(eventName)) {
			listeners.put(eventName, new EventListenerCollection());
		}
		listeners.get(eventName).addListener(listener, autoRemove);
	}
	
	public EventListener remove(String eventName, EventListener listener) {
		if (!listeners.containsKey(eventName)) {
            return null;
        }
		return listeners.get(eventName).removeListener(listener);
	}
	
	public void dispatch(Event event) {
		
	}
	
	public void clear() {
		listeners.clear();
	}
}
