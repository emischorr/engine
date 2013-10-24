package de.micralon.engine;

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
		
		mode = Mode.ONLY_DEBUG;
    }
    
    public void drawExceptBackground() {
    	drawBackground = false;
		drawWorld = true;
		drawLight = true;
		drawDebug = true;
		
		mode = Mode.EXCEPT_BG;
    }
    
    public void drawExceptDebug() {
    	drawBackground = true;
		drawWorld = true;
		drawLight = true;
		drawDebug = false;
		
		mode = Mode.ONLY_DEBUG;
    }
    
    public void drawAll() {
    	drawBackground = true;
		drawWorld = true;
		drawLight = true;
		drawDebug = true;
		
		mode = Mode.ALL;
    }
}