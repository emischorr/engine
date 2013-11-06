package de.micralon.engine;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

import de.micralon.engine.services.MusicManager;
import de.micralon.engine.services.PreferencesManager;
import de.micralon.engine.services.SoundManager;

public class EngineGame extends Game {
	// constant useful for logging
    public static final String LOG = EngineGame.class.getSimpleName();
    // whether we are in development mode
    public static final boolean DEV_MODE = true;
    public static final boolean SHOW_FPS = false;
    
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
    
	@Override
	public void create() {
		Gdx.app.log( LOG, "Creating game on " + Gdx.app.getType() );
		
		initServices();
	}
	
	protected void initServices() {
		// create the preferences manager
        preferencesManager = new PreferencesManager();
		
		// create the music manager
        musicManager = new MusicManager();
        musicManager.setVolume( preferencesManager.getVolume() );
        if( DEV_MODE ) {
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
        Gdx.app.log( LOG, "Resizing game to: " + width + " x " + height );
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
