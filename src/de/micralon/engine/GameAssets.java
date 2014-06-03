package de.micralon.engine;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.TextureLoader.TextureParameter;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.Disposable;

import de.micralon.engine.utils.Log;

public class GameAssets implements Disposable {
	public static AssetManager manager = new AssetManager();
	public static BitmapFont font;
	
	public void loadAll() {
		TextureParameter param = new TextureParameter();
		param.minFilter = TextureFilter.Nearest;
		param.genMipMaps = true;
		
		font = new BitmapFont();
		
		Log.info("Loading assets...");
		loadGameAssets();
	}
	
	public void loadGameAssets() {
		Log.warn( "GameAssets: You should override loadGameAssets() in your own class..." );
	}
	
	public <T> void load(String assetFile, Class<T> type) {
		manager.load(EngineGame.assetRootFolder+assetFile, type);
	}
	
	public <T> T get (String fileName) {
		return manager.get(EngineGame.assetRootFolder+fileName);
	}
	
	public <T> T get (String fileName, Class<T> type) {
		return manager.get(EngineGame.assetRootFolder+fileName, type);
	}
	
	@Override
	public void dispose() {
		manager.dispose();
		font.dispose();
	}
}
