package de.micralon.engine.utils;

public interface State {
	public void update(SFSM stateMachine);
	//callbacks
	public void onEnter(SFSM stateMachine); 	// called when state is pushed to brain
	public void onExit(SFSM stateMachine); 		// called when state is removed from brain
	public void onSuspend(SFSM stateMachine);	// called when a new state is pushed on top of the current
	public void onWakeup(SFSM stateMachine);	// called when the state on top of this one is removed
}
