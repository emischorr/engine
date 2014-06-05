package de.micralon.engine.gameobjects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Scaling;

import de.micralon.engine.GameWorld;
import de.micralon.engine.LastingEffect;
import de.micralon.engine.net.Network.ObjectData;
import de.micralon.engine.utils.Reuseable;

public abstract class GameObject extends Image implements Reuseable {
	private final Scaling scaling;
	protected final PhysicsSystem physics;
	protected float textureOffsetX = 0, textureOffsetY = 0;
	
	private long objectID;
	private boolean trackUpdates = true; // set this to false if you do not care synchronizing position data
	
	private Array<LastingEffect> lastingEffects = new Array<LastingEffect>();
	
	// temp vars
	private ObjectData data;
	
	public GameObject() {
		this(new NoPhysicsSystem(), Scaling.stretch);
	}
	
	protected GameObject(PhysicsSystem physics) {
		this(physics, Scaling.stretch);
	}
	
	protected GameObject(PhysicsSystem physics, Scaling scaling) {
		super();
		this.scaling = scaling;
		this.physics = physics;
		physics.setGameObject(this);
		
		// TODO: move it out and call it outside of the world step
		//init();
	}
	
	protected final void init() {
		physics.init();
		initShape();
	}
	
	private final void initShape() {
		createShape();
		physics.resetMass(); // reset body mass
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
			GameWorld.ctx.getObjectManager().changeID(this.objectID, objectID);
		}
	}
	
	public float getBodyWidth() {
		return physics.getWidth();
	}
	
	public float getBodyHeight() {
		return physics.getHeight();
	}
	
	public void setDegree(float degree) {
		physics.setDegree(degree);
		needUpdate();
	}
	
	public float getDegree() {
		return physics.getDegree();
	}
	
	public Vector2 getPos() {
		return physics.getPosition();
	}
	
	public void setPos(Vector2 pos) {
		setPos(pos.x, pos.y);
	}
	
	public void setPos(float x, float y) {
		physics.setPosition(x, y);
		needUpdate();
	}
	
	public Vector2 getVelocity() {
		return physics.getVelocity();
	}

	public void setVelocity(Vector2 velocity) {
		physics.setVelocity(velocity);
	}
	
	public void setTextureOffset(float x, float y) {
		textureOffsetX = x;
		textureOffsetY = y;
	}
	
	public void update() {}
	
	public void contactWith(GameObject obj) {}
	public void endContactWith(GameObject obj) {}
	
	/**
	 * Don't call this directly from inside the physics step
	 * WARNING: this destroys the body
	 */
	public final void destroy() {
		beforeDestroy();
		physics.destroy();
		remove(); // remove actor
		afterDestroy();
	}
	
	public void beforeDestroy() {}
	public void afterDestroy() {}
	
	@Override
	public void reuse() {
		physics.reset();
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
//		getBody().setTransform(data.position, MathUtils.degreesToRadians*data.rotation);
		physics.setPosition(data.position.x, data.position.y).setDegree(MathUtils.degreesToRadians*data.rotation);
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
		if (trackUpdates) GameWorld.ctx.getObjectManager().needUpdate(this); // notify manager about update
		updateImage(); // make the actor follow the physic body
	}
	
	private final void updateImage() {
		setOrigin(physics.getWidth()*0.5f, physics.getHeight()*0.5f);
		setRotation(physics.getDegree());
		setPosition(physics.getPosition().x-physics.getWidth()*0.5f+textureOffsetX, physics.getPosition().y-physics.getHeight()*0.5f+textureOffsetY); // set the actor position at the box2d body position
		setSize(physics.getWidth(), physics.getHeight()); // scale actor to body's size  
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
