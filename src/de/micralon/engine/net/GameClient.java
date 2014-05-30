package de.micralon.engine.net;

import java.io.IOException;
import java.net.InetAddress;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Listener;

import de.micralon.engine.EngineGame;
import de.micralon.engine.Player;

public abstract class GameClient implements NetworkNode {
	protected EngineGame game;
	private Client client;
	public String remoteIP;
	public Random random = new Random();
	
	public GameClient(EngineGame game) {
		this.game = game;
		
		client = new Client();
        client.start();
        
        Network.register(client);
        register();
        
        client.addListener(createListener());
	}
	
	protected abstract void register();
	protected abstract Listener createListener();
	
	public Array<String> searchServer() {
		Array<String> ips = new Array<String>();
		List<InetAddress> servers = client.discoverHosts(Network.portUdp, 5000); //The call will block for up to 5000 milliseconds, waiting for a response.
		Gdx.app.log("Client", "Found servers: "+servers.toString());
		for (InetAddress server : servers) {
			ips.add(server.getHostAddress());
		}
		return ips;
	}
	
	public void connect(String host) {
        try {
           	client.connect(5000, host, Network.port);
        } catch (IOException e) {
            // e.printStackTrace();
            Gdx.app.log("Client", "Can't connect to " + host);
        }
	}
	
	@Override
	public void sendMessage(Object message) {
        if (client.isConnected()) {
        	Gdx.app.log("Client", "sendMessage => "+message.toString());
            client.sendTCP(message);
        }
	}
	
	@Override
	public Player getLocalPlayer() {
		return game.getPlayer();
	}
	
	public void ping() {
        if (client.isConnected()) {
            client.updateReturnTripTime();
        }
	}
	
	public Color getColor() {
		return new Color(random.nextFloat()*0.5f+0.5f,random.nextFloat()*0.5f+0.5f,random.nextFloat()*0.5f+0.5f,1);
	}
	
	public void shutdown() {
        client.stop();
        client.close();
	}
}
