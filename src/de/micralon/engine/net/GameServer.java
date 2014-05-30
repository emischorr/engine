package de.micralon.engine.net;

import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import de.micralon.engine.EngineGame;
import de.micralon.engine.Player;

public abstract class GameServer implements NetworkNode {
	protected Server server;
	protected EngineGame game;
	
	public GameServer(EngineGame game) {
		this.game = game;
		
		server = new Server();
		Network.register(server);
		register();
	}
	
	public void start(int port) throws IOException {
		server.addListener(createListener());
		server.bind(port);
		server.start();
		Gdx.app.log("Server", "listening on TCP port "+port+"...");
	}
	
	protected abstract void register();
	protected abstract Listener createListener();
	
	@Override
	public Player getLocalPlayer() {
		return game.getPlayer();
	}
	
	@Override
	public void sendMessage(Object message) {
		Gdx.app.log("Server", "sendToAll => "+message.toString());
        server.sendToAllTCP(message);
	}
	
	public void sendMessageExcept(int connection, Object msg) {
		server.sendToAllExceptTCP(connection, msg);
	}
	
	public void sendMessageTo(int connection, Object message) {
		Gdx.app.log("Server", "sendTo => "+message.toString());
		server.sendToTCP(connection, message);
	}
	
	public void shutdown() {
		Gdx.app.log("Server", "shuting down...");
        server.close();
        server.stop();
	}
}
