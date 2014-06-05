package de.micralon.engine.gameobjects;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;

import de.micralon.engine.GameWorld;
import de.micralon.engine.builder.BodyBuilder;

public class Box2DPhysicsSystem implements PhysicsSystem {
	protected Body body;
	protected Fixture fix;
	protected BodyType bodyType = BodyType.StaticBody;
	protected float linearDamping = 0;
	protected float angularDamping = 0;
	protected boolean fixedRotation = false;
	protected Filter filterData = new Filter();
	protected Vector2 lastPos;
	
	protected float bodyWidth, bodyHeight;
	
	public Box2DPhysicsSystem() {
		this(null);
	}
	
	public Box2DPhysicsSystem(BodyType bodyType) {
		this(bodyType, 0, 0);
	}
	
	public Box2DPhysicsSystem(BodyType bodyType, float linearDamping, float angularDamping) {
		this(bodyType, linearDamping, angularDamping, false);
	}
	
	public Box2DPhysicsSystem(BodyType bodyType, float linearDamping, float angularDamping, boolean fixedRotation) {
		if (bodyType != null) this.bodyType = bodyType;
		this.linearDamping = linearDamping;
		this.angularDamping = angularDamping;
	}
	
	@Override
	public void init() {
		initBody();
	}
	
	protected final void initBody() {
		BodyBuilder bodyBuilder = new BodyBuilder(GameWorld.ctx.physicsWorld);
		body = bodyBuilder
				.type(bodyType)
				.linearDamping(linearDamping)
				.angularDamping(angularDamping)
				.position(0, 0)
				.fixedRotation(fixedRotation)
				.userData(this)
				.build();
	}

	public final void createBody() {
		createBody(false);
	}

	public final void createBody(boolean force) {
		if (force || body == null) { // create Body only once
			initBody();
		}
	}
	
	public final Fixture createFixture(Shape shape, float density) {
		fix = body.createFixture(shape, density);
		fix.setFilterData(filterData); // set filter again
		return fix;
	}
	
	public Fixture getFixture() {
		return fix;
	}
	
	public Body getBody() {
		return body;
	}
	
	public void setSize(float width, float height) {
		bodyWidth = width;
		bodyHeight = height;
	}
	
	@Override
	public float getWidth() {
		return bodyWidth;
	}
	
	@Override
	public float getHeight() {
		return bodyHeight;
	}
	
	@Override
	public Vector2 getPosition() {
		if (body != null) {
			return body.getPosition();
		} else {
			return lastPos;
		}
	}
	
	@Override
	public Box2DPhysicsSystem setPosition(float x, float y) {
		body.setTransform(x,  y, body.getAngle());
		return this;
	}
	
	@Override
	public Box2DPhysicsSystem setDegree(float degree) {
		body.setTransform(body.getPosition(), MathUtils.degreesToRadians*degree);
		return this;
	}
	
	@Override
	public float getDegree() {
		return body.getAngle() * MathUtils.radiansToDegrees;
	}
	
	public final void setFilterData(short category, short mask) {
		filterData.categoryBits = category;
		filterData.maskBits = mask;
		if (fix != null) fix.setFilterData(filterData);
	}
	
	public void setFilterData(Filter filter) {
		filterData = filter;
		if (fix != null) fix.setFilterData(filterData);
	}
	
	public Filter getFilterData() {
		return filterData;
	}
	
	@Override
	public void resetMass() {
		body.resetMassData();
	}
	
	@Override
	public void reset() {
		createBody(true);
	}

	@Override
	public void destroy() {
		if (body != null) {
			lastPos = body.getPosition();
			GameWorld.ctx.physicsWorld.destroyBody(body);
		}
		body = null;
	}

	@Override
	public Vector2 getVelocity() {
		return body.getLinearVelocity();
	}

	@Override
	public void setVelocity(Vector2 velocity) {
		body.setLinearVelocity(velocity);
	}
}
