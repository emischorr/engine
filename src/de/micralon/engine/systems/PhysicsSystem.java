package de.micralon.engine.systems;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import de.micralon.engine.GameWorld;
import de.micralon.engine.builder.BodyBuilder;

public class PhysicsSystem {
	protected Body body;
	protected BodyType bodyType = BodyType.StaticBody;
	protected float linearDamping = 0;
	protected float angularDamping = 0;
	protected Filter filterData = new Filter();
	
	public PhysicsSystem() {
		this(null, 0, 0);
	}
	
	public PhysicsSystem(BodyType bodyType, float linearDamping, float angularDamping) {
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
}
