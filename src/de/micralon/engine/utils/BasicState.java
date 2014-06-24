package de.micralon.engine.utils;

public abstract class BasicState implements State {

	@Override
	public void onEnter(SFSM stateMachine) {}

	@Override
	public void onExit(SFSM stateMachine) {}

	@Override
	public void onSuspend(SFSM stateMachine) {}

	@Override
	public void onWakeup(SFSM stateMachine) {}

}
