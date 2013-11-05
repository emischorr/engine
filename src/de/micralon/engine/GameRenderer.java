package de.micralon.engine;

import box2dLight.RayHandler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;

public class GameRenderer {
	private GameWorld world;
	private SpriteBatch batch;
	private BitmapFont font;
    private OrthographicCamera camera;
    private Box2DDebugRenderer debugRenderer;
    
    private final RayHandler rayHandler;
    
    public boolean drawTags = true;
    
    public boolean drawBodies = true;
	public boolean drawJoints = false;
	public boolean drawAAAB = false;
	public boolean drawInactiveBodies = false;
	public boolean drawVelocities = false;
	public boolean drawContacts = false;
	
	public GameRendererOptions options;
	
	public GameRenderer(GameWorld world, SpriteBatch batch) {
		this(world, batch, null, new GameRendererOptions());
	}
    
    public GameRenderer(GameWorld world, SpriteBatch batch, BitmapFont font, GameRendererOptions options) {
    	this.world = world;
    	this.batch = batch;
    	this.font = font;
    	this.options = options;
    		
        debugRenderer = new Box2DDebugRenderer(drawBodies, drawJoints, drawAAAB, drawInactiveBodies, drawVelocities, drawContacts);
      
        // we obtain a reference to the game stage camera. The camera is scaled to box2d meter units
        camera = (OrthographicCamera) world.stage.getCamera();
        
        rayHandler = world.lightManager.rayHandler;
    }
    
    public void render() {
    	render(true);
    }
    
    public void render(boolean updateLight) {
    	Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT );
		
        camera.update();
        
        rayHandler.setCombinedMatrix(camera.combined);
        
        // background rendering
        if (options.drawBackground) {
        	world.background.draw();
        }
        
        // game stage rendering
        if (options.drawWorld) {
        	world.stage.draw();
        }
        
//        world.effectManager.draw(world.stage.getSpriteBatch());
        
        // draw the light
        if (options.drawLight) {
        	if (updateLight) { rayHandler.update(); }
        	rayHandler.render();
        }
        
        // box2d debug rendering
        if (options.drawDebug) {
        	debugRenderer.render(world.box2dWorld, camera.combined);
        }
        
        if (drawTags && font != null) {
        	batch.begin();
        	for (NameTag tag : world.tags) {
        		tag.draw(batch, font);
        	}
        	batch.end();
        }
    }
    
}
