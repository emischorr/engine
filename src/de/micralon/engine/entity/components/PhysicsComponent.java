package de.micralon.engine.entity.components;

import com.badlogic.gdx.physics.box2d.Body;

import de.micralon.engine.Env;
import de.micralon.engine.physics.PhysicsData;

import ashley.core.Component;
import ashley.utils.Pool.Poolable;

public class PhysicsComponent extends Component implements Poolable {
	public Body body;
	public PhysicsData data;
	
	public PhysicsComponent() {
		body = null;
		data = null;
	}
	
	public PhysicsComponent(PhysicsComponent other) {
		data = other.data;
		body = data.createBody(null);
	}
	
	@Override
	public void reset() {
		if (body != null) {
			Env.game.getWorld().physicsWorld.destroyBody(body);	
		}
		
		body = null;
		data = null;
	}
}
