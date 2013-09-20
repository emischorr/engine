package de.micralon.engine;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.utils.Scaling;

import de.micralon.engine.builder.BodyBuilder;

public abstract class GameObject<WORLD extends GameWorld> extends Image implements Reuseable {
	protected transient WORLD world;
	private transient Body body;
	private final Scaling scaling;
	protected float textureOffsetX = 0, textureOffsetY = 0;
	public Fixture fix;
	private final Filter filterData = new Filter();
	
	public ObjectState state;
	
	public GameObject(WORLD world, ObjectState state) {
		this(world, state, Scaling.stretch);
	}
	
	protected GameObject(WORLD world, ObjectState state, Scaling scaling) {
		super();
		this.world = world;
		this.state = state;
		this.scaling = scaling;
		createBody();
	}
	
	public final void createBody() {
		createBody(false);
	}
	
	public final void createBody(boolean force) {
		if (force || body == null) { // create Body only once
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
	
	public final void setFilterData(short category) {
		setFilterData(category, (short) ~category);
	}
	
	public final void setFilterData(short category, short mask) {
		filterData.categoryBits = category;
		filterData.maskBits = mask;
		fix.setFilterData(filterData);
	}
	
	public Body getBody() {
		return body;
	}
	
	public void contactWith(GameObject<?> obj, Contact contact) {}
	
	public void destroy() {
		if (body != null) {
			world.box2dWorld.destroyBody(body);
			body = null;
		}
		remove();
	}
	
	@Override
	public void reuse() {
		state = new ObjectState();
		createBody(true);
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
