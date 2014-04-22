package de.micralon.engine.events;

import com.badlogic.gdx.utils.Array;

public class EventListenerCollection {
	private Array<EventListenerEntry> entries = new Array<EventListenerEntry>();
	
	public void addListener(EventListener listener, boolean autoRemove) {
		entries.add(new EventListenerEntry(listener, autoRemove));
	}
	
	public EventListener removeListener(EventListener listener) {
		for (EventListenerEntry entry : entries) {
			if (entry.eventListener.equals(listener)) {
				entries.removeValue(entry, true);
				return listener;
			}
		}
		return null;
	}
}
