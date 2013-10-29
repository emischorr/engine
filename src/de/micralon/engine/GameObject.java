package de.micralon.engine;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Scaling;

import de.micralon.engine.builder.BodyBuilder;
import de.micralon.engine.net.Network.ObjectData;

public abstract class GameObject<WORLD extends GameWorld> extends Image implements Reuseable {
	protected transient WORLD world;
	private transient Body body;
	private final Scaling scaling;
	protected float textureOffsetX = 0, textureOffsetY = 0;
	public Fixture fix;
	private final Filter filterData = new Filter();
	
	private long objectID;
	private boolean trackUpdates = true; // set this to false if you do not care synchronizing position data
	
	private Array<LastingEffect> lastingEffects = new Array<LastingEffect>();
	
	// set these characteristics in sub classes according to your needs
	protected BodyType type = BodyType.StaticBody;
	protected float linearDamping = 0;
	protected float angularDamping = 0;
	protected float bodyWidth;
	protected float bodyHeight;
	
	// temp vars
	private ObjectData data;
	
	public GameObject(WORLD world) {
		this(world, null, 1, 1, 0, 0, Scaling.stretch);
	}
	
	protected GameObject(WORLD world, BodyType type, float bodyWidth, float bodyHeight, float linearDamping, float angularDamping) {
		this(world, type, bodyWidth, bodyHeight, linearDamping, angularDamping, Scaling.stretch);
	}
	
	protected GameObject(WORLD world, BodyType type, float bodyWidth, float bodyHeight, float linearDamping, float angularDamping, Scaling scaling) {
		super();
		this.world = world;
		this.scaling = scaling;
		
		if (type != null) this.type = type;
		this.bodyWidth = bodyWidth;
		this.bodyHeight = bodyHeight;
		this.linearDamping = linearDamping;
		this.angularDamping = angularDamping;
		
		createBody();
		createShape();
	}
	
	public final void createBody() {
		createBody(false);
	}
	
	public final void createBody(boolean force) {
		if (force || body == null) { // create Body only once
			BodyBuilder bodyBuilder = new BodyBuilder(world.box2dWorld);
			body = bodyBuilder
					.type(type)
					.linearDamping(linearDamping)
					.angularDamping(angularDamping)
					.position(0, 0)
					.userData(this)
					.build();
			
			needUpdate();
		}
	}
	
	protected void createShape() {}
	
	public long getObjectID() {
		return objectID;
	}
	
	public final void setObjectID(long objectID) {
		setObjectID(objectID, false);
	}
	
	/**
	 * Sets the objectID. DON'T CALL THIS!
	 * @param objectID
	 * @param force
	 */
	public final void setObjectID(long objectID, boolean force) {
		if (force) {
			this.objectID = objectID;
		} else {
			world.getObjectManager().changeID(this.objectID, objectID);
		}
	}
	
	public float getBodyWidth() {
		return bodyWidth;
	}
	
	public float getBodyHeight() {
		return bodyHeight;
	}
	
	public void setDegree(float degree) {
		getBody().setTransform(getPos(), MathUtils.degreesToRadians*degree);
		needUpdate();
	}
	
	public float getDegree() {
		return body.getAngle() * MathUtils.radiansToDegrees;
	}
	
	public Vector2 getPos() {
		if (body != null) {
			return body.getPosition();
		} else {
			return null;
		}
	}
	
	public void setPos(float x, float y) {
		body.setTransform(x, y, 0);
		needUpdate();
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
	
	public void setFilterData(Filter filter) {
		fix.setFilterData(filter);
	}
	
	public Filter getFilterData() {
		return filterData;
	}
	
	public Body getBody() {
		return body;
	}
	
	public void update() {}
	
	public void contactWith(GameObject<?> obj, Contact contact) {}
	public void endContactWith(GameObject<?> obj, Contact contact) {}
	
	/**
	 * Don't call this directly from inside the physics step
	 */
	public void destroy() {
		if (body != null) {
			world.box2dWorld.destroyBody(body);
			body = null;
		}
		remove();
	}
	
	@Override
	public void reuse() {
		createBody(true);
		createShape();
	}
	
	public void addLastingEffect(LastingEffect effect) {
		lastingEffects.add(effect);
	}
	
	@Override  
    public void act(float delta) {
        super.act(delta);
        needUpdate();
        updateLastingEffects(delta);
    }
	
	public void draw(SpriteBatch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
	}
	
	public ObjectData getData() {
		data = new ObjectData(objectID, getPos(), getDegree());
		return data;
	}
	
	public void setData(ObjectData data) {
		getBody().setTransform(data.position, MathUtils.degreesToRadians*data.rotation);
		updateImage();
	}
	
	/**
	 * Set trackUpdates to false if you do not want to synchronize position and rotation state over a network. 
	 * Default: true
	 * Note: Also check that ObjectManger is set to track updates
	 * @param trackUpdates whether updates are tracked or not
	 */
	protected final void trackUpdates(boolean trackUpdates) {
		this.trackUpdates = trackUpdates;
	}
	
	private final void needUpdate() {
		if (trackUpdates) world.getObjectManager().needUpdate(this); // notify manager about update
		updateImage(); // make the actor follow the box2d body
	}
	
	private final void updateImage() {
		setOrigin(bodyWidth*0.5f, bodyHeight*0.5f);
		setRotation(MathUtils.radiansToDegrees * body.getAngle());
		setPosition(body.getPosition().x-bodyWidth*0.5f+textureOffsetX, body.getPosition().y-bodyHeight*0.5f+textureOffsetY); // set the actor position at the box2d body position
		setSize(bodyWidth, bodyHeight); // scale actor to body's size  
		setScaling(scaling); // stretch the texture  
		setAlign(Align.center);
	}
	
	private final void updateLastingEffects(float delta) {
		for (LastingEffect effect : lastingEffects) {
			effect.update(this, delta);
			if (effect.isTimedOut()) lastingEffects.removeValue(effect, true);
		}
	}
}
