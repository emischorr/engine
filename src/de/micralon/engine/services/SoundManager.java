package de.micralon.engine.services;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Disposable;

import de.micralon.engine.utils.LRUCache;
import de.micralon.engine.utils.LRUCache.CacheEntryRemovedListener;
import de.micralon.engine.services.SoundManager.GameSound;

public class SoundManager implements CacheEntryRemovedListener<GameSound,Sound>, Disposable {
	private final String LOG_TAG = "SoundManager";

	// The available sound files.
    public enum GameSound
    {
        CLICK( "sound/click.wav" );

        private final String fileName;

        private GameSound(String fileName) {
            this.fileName = fileName;
        }

        public String getFileName() {
            return fileName;
        }
    }
    
	// The volume to be set on the sound.
    private float volume = 1f;
    //Whether the sound is enabled.
    private boolean enabled = true;
    // The sound cache.
    private final LRUCache<GameSound,Sound> soundCache;
	
	public SoundManager() {
        soundCache = new LRUCache<SoundManager.GameSound,Sound>( 10 );
        soundCache.setEntryRemovedListener( this );
    }
	
	// Plays the specified sound.
    public void play( GameSound sound ) {
        // check if the sound is enabled
        if( ! enabled ) return;

        // try and get the sound from the cache
        Sound soundToPlay = soundCache.get( sound );
        if( soundToPlay == null ) {
            FileHandle soundFile = Gdx.files.internal( sound.getFileName() );
            soundToPlay = Gdx.audio.newSound( soundFile );
            soundCache.add( sound, soundToPlay );
        }

        // play the sound
        Gdx.app.log( LOG_TAG, "Playing sound: " + sound.name() );
        soundToPlay.play( volume );
    }

    // Sets the sound volume which must be inside the range [0,1].
    public void setVolume( float volume ) {
        Gdx.app.log( LOG_TAG, "Adjusting sound volume to: " + volume );

        // check and set the new volume
        if( volume < 0 || volume > 1f ) {
            throw new IllegalArgumentException( "The volume must be inside the range: [0,1]" );
        }
        this.volume = volume;
    }

    // Enables or disabled the sound.
    public void setEnabled( boolean enabled ) {
        this.enabled = enabled;
    }

    // EntryRemovedListener implementation

    @Override
    public void notifyEntryRemoved( GameSound key, Sound value ) {
        Gdx.app.log( LOG_TAG, "Disposing sound: " + key.name() );
        value.dispose();
    }
	
	@Override
	public void dispose() {
		Gdx.app.log( LOG_TAG, "Disposing sound manager" );
        for( Sound sound : soundCache.retrieveAll() ) {
            sound.stop();
            sound.dispose();
        }
	}

}
