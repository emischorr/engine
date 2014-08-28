package de.micralon.engine;

import ashley.core.Engine;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

import de.micralon.engine.entity.factory.EntityFactory;
import de.micralon.engine.entity.factory.ComponentReaders.*;
import de.micralon.engine.net.NetworkNode;
import de.micralon.engine.services.MusicManager;
import de.micralon.engine.services.PreferencesManager;
import de.micralon.engine.services.SoundManager;
import de.micralon.engine.utils.Log;

public abstract class EngineGame extends Game {
	@Deprecated
    public static EngineGame ctx;
    public static boolean DEV, FPS;
    public static String assetRootFolder = "";
    public static String levelPath = "";
    
    public GameAssets assets;
    
    public boolean paused = false;
    
    private boolean stepped = false;
	private long step;
	
	private Engine engine;
    private EntityFactory entityFactory;
    
    protected Player player;
	//networking
    public enum NETWORK_STATES {none, client, server};
    protected NETWORK_STATES networkState = NETWORK_STATES.none;
	protected NetworkNode node;
    
    // Services
    private PreferencesManager preferencesManager;
    private MusicManager musicManager;
    private SoundManager soundManager;
    
    public PreferencesManager getPreferencesManager() {
        return preferencesManager;
    }
    
    public MusicManager getMusicManager() {
        return musicManager;
    }

    public SoundManager getSoundManager() {
        return soundManager;
    }
    
    public static boolean isDevelopment() {
    	return DEV;
    }
    public static void setDevelopment(boolean mode) {
    	DEV = mode;
    }
    
    public static boolean showFPS() {
    	return FPS;
    }
    public static void showFPS(boolean mode) {
    	FPS = mode;
    }
    
    protected Screen getStartScreen() {
    	Log.warn("You should override getStartScreen() in your Game!");
    	return null;
    }
    
    public abstract GameWorld getWorld();
    
    public Player getPlayer() {
    	if (player == null) {
    		player = new Player("Player");
    	}
    	return player;
    }
    
	@Override
	public void create() {
		Env.init(this);
		// deprecated
		ctx = this;
		
		Log.setTopic(EngineGame.class.getSimpleName());
		Log.info( "Creating game on " + Gdx.app.getType() );
		initServices();
		
		if (EngineGame.isDevelopment()) {
			Gdx.app.setLogLevel(Application.LOG_DEBUG);
		} else {
			Gdx.app.setLogLevel(Application.LOG_NONE);
		}
		
		if (assets == null) assets = new GameAssets();
		assets.loadAll();
		
		engine = new Engine();

        entityFactory = new EntityFactory();
        entityFactory.registerReader(new ColorComponentReader());
        entityFactory.registerReader(new PhysicsComponentReader());
        entityFactory.registerReader(new TransformComponentReader());
	}
	
	protected void initServices() {
		// create the preferences manager
        preferencesManager = new PreferencesManager();
		
		// create the music manager
        musicManager = new MusicManager();
        musicManager.setVolume( preferencesManager.getVolume() );
        if( isDevelopment() ) {
        	musicManager.setEnabled( false );
        } else {
        	musicManager.setEnabled( preferencesManager.isMusicEnabled() );
        }

        // create the sound manager
        soundManager = new SoundManager();
        soundManager.setVolume( preferencesManager.getVolume() );
        soundManager.setEnabled( preferencesManager.isSoundEnabled() );
	}
	
	@Override
    public void resize(int width, int height) {
        super.resize( width, height );
        Log.info( "Resizing game to: " + width + " x " + height );
        
        if (getScreen() == null) {
        	if (getStartScreen() != null) {
        		setScreen( getStartScreen() );
        	} else {
        		Log.warn("No startScreen definied! Quit application...");
        		Gdx.app.exit();
        	}
        }
    }

	@Override
    public void render() {
    	try {
	        super.render();
    	} catch (Exception e) {
    		e.printStackTrace();
    		dispose();
    	}
    }
	
	public float updatePhysics(float deltaTime) {
		int steps_taken = Physics.update(getWorld(), deltaTime);
		if (steps_taken > 0) {
			stepped = true;
			step++;
			// network sync
			if (isHost() && stepped && step % 100 == 0) { // Every 100th step
				getWorld().sync(node);
			}
		}
		return steps_taken;
	}
	
	// Network
	public boolean isHost() {
		return networkState == NETWORK_STATES.server;
	}
	
    public NetworkNode getNode() {
    	return node;
    }
	
    @Override
    public void dispose() {
    	super.dispose();
    	assets.dispose();
    	if (getWorld() != null) getWorld().dispose();
    	if (node != null) node.shutdown();
    	// dispose some services
        musicManager.dispose();
        soundManager.dispose();
    }

	public Engine getEngine() {
		return engine;
	}
}
