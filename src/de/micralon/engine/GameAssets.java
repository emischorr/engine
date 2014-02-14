package de.micralon.engine;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.TextureLoader.TextureParameter;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class GameAssets {
	public static AssetManager manager = new AssetManager();
	public static BitmapFont font;
	
	public static void loadAll() {
		TextureParameter param = new TextureParameter();
		param.minFilter = TextureFilter.Nearest;
		param.genMipMaps = true;
		
		font = new BitmapFont();
		
		loadGameAssets();
	}
	
	public static void loadGameAssets() {
		
	}
}
