package de.micralon.engine;

import com.badlogic.gdx.Gdx;

public class GameRendererOptions {
	public boolean drawBackground = true;
    public boolean drawWorld = true;
    public boolean drawLight = true;
    public boolean drawDebug = false;
    public boolean drawTags = true;
    
    private Mode mode = Mode.UNDEFINED;
    private enum Mode {
    	UNDEFINED, ONLY_DEBUG, EXCEPT_BG, EXCEPT_DEBUG, ALL
    }
    
    public GameRendererOptions() {}
    public GameRendererOptions(boolean drawBackground, boolean drawWorld, boolean drawLight, boolean drawDebug) {
    	this.drawBackground = drawBackground;
    	this.drawWorld = drawWorld;
    	this.drawLight = drawLight;
    	this.drawDebug = drawDebug;
    }
    
    public void switchMode() {
    	if (mode == Mode.ONLY_DEBUG) {
    		drawExceptBackground();
    	} else if (mode == Mode.EXCEPT_BG) {
    		drawExceptDebug();
    	} else if (mode == Mode.EXCEPT_DEBUG) {
    		drawAll();
    	} else if (mode == Mode.ALL) {
    		drawOnlyDebug();
    	} else {
    		drawAll();
    	}
    }
    
    public void drawOnlyDebug() {
    	drawBackground = false;
		drawWorld = false;
		drawLight = false;
		drawDebug = true;
		
		setMode(Mode.ONLY_DEBUG);
    }
    
    public void drawExceptBackground() {
    	drawBackground = false;
		drawWorld = true;
		drawLight = true;
		drawDebug = true;
		
		setMode(Mode.EXCEPT_BG);
    }
    
    public void drawExceptDebug() {
    	drawBackground = true;
		drawWorld = true;
		drawLight = true;
		drawDebug = false;
		
		setMode(Mode.EXCEPT_DEBUG);
    }
    
    public void drawAll() {
    	drawBackground = true;
		drawWorld = true;
		drawLight = true;
		drawDebug = true;
		
		setMode(Mode.ALL);
    }
    
    private void setMode(Mode mode) {
    	this.mode = mode;
		Gdx.app.log("GameRendererOptions", "switched render mode to "+mode.toString());
    }
}