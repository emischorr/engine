package de.micralon.engine.events;

public class EventListenerEntry {
	public final EventListener eventListener;
	public final boolean autoRemove;
	
	public EventListenerEntry(EventListener eventListener, boolean autoRemove) {
		this.eventListener = eventListener;
		this.autoRemove = autoRemove;
	}
}
