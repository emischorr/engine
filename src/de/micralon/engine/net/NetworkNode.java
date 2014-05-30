package de.micralon.engine.net;

import de.micralon.engine.Player;

public interface NetworkNode {
	public void sendMessage(Object message);
	public Player getLocalPlayer();
	public void shutdown();
}
