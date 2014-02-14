package de.micralon.engine;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

import de.micralon.engine.services.MusicManager;
import de.micralon.engine.services.PreferencesManager;
import de.micralon.engine.services.SoundManager;
import de.micralon.engine.utils.Log;

public class EngineGame extends Game {
    public static EngineGame ctx;
    public static boolean DEV, FPS;
    
    protected Player player;
    
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
    
    public Player getPlayer() {
    	if (player == null) {
    		player = new Player("Player");
    	}
    	return player;
    }
    
	@Override
	public void create() {
		ctx = this;
		
		Log.setTopic(EngineGame.class.getSimpleName());
		Log.info( "Creating game on " + Gdx.app.getType() );
		initServices();
		
		if (EngineGame.isDevelopment()) {
			Gdx.app.setLogLevel(Application.LOG_DEBUG);
		} else {
			Gdx.app.setLogLevel(Application.LOG_NONE);
		}
		
		GameAssets.loadAll();
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
    public void resize(int width, int height)
    {
        super.resize( width, height );
        Log.info( "Resizing game to: " + width + " x " + height );
    }

	@Override
    public void render()
    {
    	try {
	        super.render();
    	} catch (Exception e) {
    		e.printStackTrace();
    		dispose();
    	}
    }
    
    @Override
    public void dispose()
    {
    	super.dispose();
    	
    	// dispose some services
        musicManager.dispose();
        soundManager.dispose();
    }
}
