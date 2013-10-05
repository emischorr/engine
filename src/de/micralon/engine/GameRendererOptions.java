package de.micralon.engine;

public class GameRendererOptions {
	public boolean drawBackground = true;
    public boolean drawWorld = true;
    public boolean drawLight = true;
    public boolean drawDebug = false;
    public boolean drawTags = true;
    
    public GameRendererOptions() {}
    public GameRendererOptions(boolean drawBackground, boolean drawWorld, boolean drawLight, boolean drawDebug) {
    	this.drawBackground = drawBackground;
    	this.drawWorld = drawWorld;
    	this.drawLight = drawLight;
    	this.drawDebug = drawDebug;
    }
}