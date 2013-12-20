package de.micralon.engine;

import box2dLight.RayHandler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;

import de.micralon.engine.text.Text;

public class GameRenderer {
	private GameWorld world;
	private SpriteBatch batch;
    private OrthographicCamera camera;
    private Box2DDebugRenderer debugRenderer;
    
    private final RayHandler rayHandler;
    
    public boolean drawText = true;
    
    public boolean drawBodies = true;
	public boolean drawJoints = false;
	public boolean drawAAAB = false;
	public boolean drawInactiveBodies = false;
	public boolean drawVelocities = false;
	public boolean drawContacts = false;
	
	private ShaderProgram shader;
	
	public GameRendererOptions options;
	
	public GameRenderer(GameWorld world, SpriteBatch batch) {
		this(world, batch, new GameRendererOptions());
	}
    
    public GameRenderer(GameWorld world, SpriteBatch batch, GameRendererOptions options) {
    	this.world = world;
    	this.batch = batch;
    	this.options = options;
    		
        debugRenderer = new Box2DDebugRenderer(drawBodies, drawJoints, drawAAAB, drawInactiveBodies, drawVelocities, drawContacts);
      
        // we obtain a reference to the game stage camera. The camera is scaled to box2d meter units
        camera = (OrthographicCamera) world.stage.getCamera();
        
        rayHandler = world.lightManager.rayHandler;
        
//        String vertexShader = "\n" + 
//        		"#ifdef GL_ES\n" + 
//        		"#define MED mediump\n" + 
//        		"#else  \n" + 
//        		"#define MED \n" + 
//        		"#endif\n" + 
//        		"attribute vec4 a_position; \n" + 
//        		"attribute vec2 a_texCoord0; \n" + 
//        		"varying MED vec2 v_texCoords;\n" + 
//        		"void main()\n" + 
//        		"{\n" + 
//        		"	v_texCoords = a_texCoord0;\n" + 
//        		"	gl_Position = a_position;\n" + 
//        		"}";
//		String fragmentShader = "#ifdef GL_ES\n" + 
//				"#define LOWP lowp\n" + 
//				"#define MED mediump\n" + 
//				"precision lowp float;\n" + 
//				"#else\n" + 
//				"#define LOWP  \n" + 
//				"#define MED \n" + 
//				"#endif\n" + 
//				"uniform sampler2D u_texture0;\n" + 
//				"uniform sampler2D u_texture1;\n" + 
//				"uniform float BloomIntensity;\n" + 
//				"uniform float OriginalIntensity;\n" + 
//				"\n" + 
//				"varying MED vec2 v_texCoords;\n" + 
//				"\n" + 
//				"void main()\n" + 
//				"{\n" + 
//				"	\n" + 
//				"	vec3 original = texture2D(u_texture0, v_texCoords).rgb;\n" + 
//				"	vec3 bloom = texture2D(u_texture1, v_texCoords).rgb * BloomIntensity; 	\n" + 
//				"    original = OriginalIntensity * (original - original * bloom);	 	\n" + 
//				" 	gl_FragColor.rgb =  original + bloom; 	\n" + 
//				"}";
//		
//		ShaderProgram.pedantic = false;
//		shader = new ShaderProgram(vertexShader, fragmentShader);
//		
//		if (!this.shader.isCompiled()) {
//            Gdx.app.log("Problem loading shader:", this.shader.getLog());
//        }
//		if (shader.getLog().length()!=0)
//			System.out.println(shader.getLog());
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
//        	world.stage.getSpriteBatch().setShader(shader);
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
        
        if (drawText) {
        	batch.begin();
        	for (Text text : world.texts) {
        		text.draw(batch);
        	}
        	batch.end();
        }
    }
    
}
