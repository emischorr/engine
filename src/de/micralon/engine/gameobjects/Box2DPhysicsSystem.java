package de.micralon.engine.gameobjects;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Filter;
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
	protected Filter filterData = new Filter();
	protected Vector2 lastPos;
	
	public Box2DPhysicsSystem() {
		this(null, 0, 0);
	}
	
	public Box2DPhysicsSystem(BodyType bodyType, float linearDamping, float angularDamping) {
		if (bodyType != null) this.bodyType = bodyType;
		this.linearDamping = linearDamping;
		this.angularDamping = angularDamping;
		
		initBody();
	}
	
	protected final void initBody() {
		BodyBuilder bodyBuilder = new BodyBuilder(GameWorld.ctx.box2dWorld);
		body = bodyBuilder
				.type(bodyType)
				.linearDamping(linearDamping)
				.angularDamping(angularDamping)
				.position(0, 0)
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
		fix.setFilterData(filterData);
	}
	
	public void setFilterData(Filter filter) {
		fix.setFilterData(filter);
	}
	
	public Filter getFilterData() {
		return filterData;
	}

	@Override
	public void destroy() {
		lastPos = body.getPosition();
		GameWorld.ctx.box2dWorld.destroyBody(body);
		body = null;
	}
}
