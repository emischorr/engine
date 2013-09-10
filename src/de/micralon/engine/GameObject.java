package de.micralon.engine;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.utils.Scaling;

import de.micralon.engine.builder.BodyBuilder;


public abstract class GameObject<STATE extends GameState<?>> extends Image {
	protected transient AbstractGameWorld<STATE> world;
	private transient Body body;
	private final Scaling scaling;
	protected float textureOffsetX = 0, textureOffsetY = 0;
	public long lastTeleportTime = 0;
	
	public ObjectState state;
	
	public GameObject(AbstractGameWorld<STATE> world, ObjectState state) {
		this(world, state, Scaling.stretch);
	}
	
	protected GameObject(AbstractGameWorld<STATE> world, ObjectState state, Scaling scaling) {
		super();
		this.world = world;
		this.state = state;
		this.scaling = scaling;
		createBody();
	}
	
	private void createBody() {
		BodyBuilder bodyBuilder = new BodyBuilder(world.box2dWorld);
		body = bodyBuilder
				.type(state.type)
				.linearDamping(state.linearDamping)
				.angularDamping(state.angularDamping)
				.position(state.position.x, state.position.y)
				.userData(this)
				.build();
		
		updateImage();
	}
	
	public Vector2 getPos() {
		return state.position;
	}
	
	public void setPos(float x, float y) {
		state.position.x = x;
		state.position.y = y;
		body.setTransform(x, y, 0);
		updateImage();
	}
	
	public void setTextureOffset(float x, float y) {
		textureOffsetX = x;
		textureOffsetY = y;
	}
	
	public Body getBody() {
		return body;
	}
	
	public void contactWith(GameObject<?> obj, Contact contact) {}
	
	public void destroy(){
		world.box2dWorld.destroyBody(body);
		remove();
	}
	
	@Override  
    public void act(float delta) {
		updateState(); // update the state based on the movement of the body in the world
        super.act(delta);
        updateImage(); // make the actor follow the box2d body  
    }
	
	public void updateState() {
		state.position = body.getPosition();
		state.rotation = MathUtils.radiansToDegrees * body.getAngle();
	}
	
	private void updateImage() {
		setOrigin(state.width*0.5f, state.height*0.5f);
		setRotation(state.rotation);
		setPosition(state.position.x-state.width*0.5f+textureOffsetX, state.position.y-state.height*0.5f+textureOffsetY); // set the actor position at the box2d body position
		setSize(state.width, state.height); // scale actor to body's size  
		setScaling(scaling); // stretch the texture  
		setAlign(Align.center);
	}
}
