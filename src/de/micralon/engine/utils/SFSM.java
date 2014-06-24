package de.micralon.engine.utils;

import com.badlogic.gdx.utils.Array;

/**
 * Stack-based Finite State Machine
 * @author Enrico Mischorr
 *
 */
public class SFSM {
	protected final Array<State> states = new Array<State>();
	
	public void update() {
		if (states.size > 0) {
			currentState().update(this); // execute current state
		}
	}
	
	/**
	 * removes current state and replaces it with a new state
	 * @param state
	 */
	public void switchTo(State state) {
		if (state != null) {
			pop();
			push(state);	
		}
	}
	
	/**
	 * add a new state on top of current
	 * @param state
	 */
	public void push(State state) {
		if (state != null) {
			currentState().onSuspend(this);
			states.add(state);
			state.onEnter(this);
		}
	}
	
	/**
	 * removes current state
	 */
	public void pop() {
		states.removeIndex(states.size-1).onExit(this);
		currentState().onWakeup(this);
	}
	
	protected State currentState() {
		return states.get(states.size-1);
	}
	
	/**
	 * removes all states without callbacks
	 */
	public void clear() {
		states.clear();
	}

}
