package de.micralon.engine;

import box2dLight.RayHandler;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;

public class GameRenderer {
	private GameWorld world;
    private OrthographicCamera camera;
    private Box2DDebugRenderer debugRenderer;
    
    private final RayHandler rayHandler;
    
    public boolean drawBackground = false;
    public boolean drawWorld = true;
    public boolean drawLight = false;
    public boolean drawDebug = true;
    
    public boolean drawBodies = true;
	public boolean drawJoints = false;
	public boolean drawAAAB = false;
	public boolean drawInactiveBodies = false;
	public boolean drawVelocities = false;
	public boolean drawContacts = false;
    
    public GameRenderer(GameWorld world) {
    	this.world = world;
        debugRenderer = new Box2DDebugRenderer(drawBodies, drawJoints, drawAAAB, drawInactiveBodies, drawVelocities, drawContacts);
      
        // we obtain a reference to the game stage camera. The camera is scaled to box2d meter units
        camera = (OrthographicCamera) world.stage.getCamera();
        
        rayHandler = world.lightManager.rayHandler;
    }
    
    public void render() {
    	render(true);
    }
    
    public void render(boolean updateLight) {
//    	Gdx.gl.glClearColor(0, 0, 0, 1);
//		Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT );
		
        camera.update();
        
        rayHandler.setCombinedMatrix(camera.combined);
        
        // box2d debug renderering
        if (drawDebug) {
        	debugRenderer.render(world.box2dWorld, camera.combined);
        }
        
        // background rendering
        if (drawBackground) {
        	world.background.draw();
        }
        
        // game stage rendering
        if (drawWorld) {
        	world.stage.draw();
        }
        
        // draw the light
        if (drawLight) {
        	if (updateLight) { rayHandler.update(); }
        	rayHandler.render();
        }
    }
    
    
    
}
