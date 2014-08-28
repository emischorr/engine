package de.micralon.engine.entity.components;

import com.badlogic.gdx.math.Vector2;

import ashley.core.Component;
import ashley.utils.Pool.Poolable;

public class TransformComponent extends Component implements Poolable {
	public Vector2 position;
	public float scale;
	public float angle;
	
	public TransformComponent() {
		position = new Vector2();
		reset();
	}
	
	public TransformComponent(TransformComponent other) {
		position = new Vector2(other.position);
		scale = other.scale;
		angle = other.angle;
	}
	
	@Override
	public void reset() {
		position.set(0.0f, 0.0f);
		scale = 1.0f;
		angle = 0.0f;
	}
}
