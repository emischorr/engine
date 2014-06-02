package de.micralon.engine;

import box2dLight.RayHandler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;

import de.micralon.engine.map.Tile;
import de.micralon.engine.postprocessing.Processor;
import de.micralon.engine.text.Text;

public class GameRenderer {
	private GameWorld world;
	private Batch batch;
    private OrthographicCamera camera;
    private Box2DDebugRenderer debugRenderer;
    private ShapeRenderer shapeRenderer;
    
    private final RayHandler rayHandler;
    
    public boolean drawText = true;
    
    public boolean drawBodies = true;
	public boolean drawJoints = false;
	public boolean drawAAAB = false;
	public boolean drawInactiveBodies = false;
	public boolean drawVelocities = false;
	public boolean drawContacts = false;
	
	private Processor processor;
	
	public GameRendererOptions options;
	
	public GameRenderer(GameWorld world, Batch batch) {
		this(world, batch, new GameRendererOptions());
	}
    
    public GameRenderer(GameWorld world, Batch batch, GameRendererOptions options) {
    	this.world = world;
    	this.batch = batch;
    	this.options = options;
    	
    	// we obtain a reference to the game stage camera. The camera is scaled to box2d meter units
        camera = (OrthographicCamera) world.stage.getCamera();
    		
        debugRenderer = new Box2DDebugRenderer(drawBodies, drawJoints, drawAAAB, drawInactiveBodies, drawVelocities, drawContacts);
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.identity();
        shapeRenderer.setColor(0, 1, 0, 0.5f);
        
        rayHandler = world.lightManager.rayHandler;
        
        processor = new Processor();
    }
    
    public void addPostEffect(ShaderProgram shader) {
    	processor.addShader(shader);
    }
    
    public void render() {
    	render(true);
    }
    
    public void render(boolean updateLight) {
    	Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT );
		
        camera.update();
        
        rayHandler.setCombinedMatrix(camera.combined);
        
//        processor.capture();
        
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
        
//        processor.renderTo(batch);
        
        
        // box2d debug rendering
        if (options.drawDebug) {
        	debugRenderer.render(world.box2dWorld, camera.combined);
        }
        
        // TODO: optimize -> use separate flag
        if (options.drawDebug) {
	    	shapeRenderer.setProjectionMatrix(camera.combined);
	        shapeRenderer.identity();
			shapeRenderer.begin(ShapeType.Line);
			for (Actor actor : world.physics.getChildren()) {
				if (actor instanceof Tile) {
					((Tile)actor).drawDebug(shapeRenderer);
				}
			}
			shapeRenderer.end();
        }
        
        if (drawText) {
        	batch.begin();
        	for (Text text : world.texts) {
        		text.draw(batch);
        	}
        	batch.end();
        }
    }
    
    public TextureRegion getFrame() {
    	processor.capture();
    	render();
    	processor.endCapture();
    	return processor.dump();
    }
}
