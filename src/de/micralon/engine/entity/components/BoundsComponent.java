package de.micralon.engine.entity.components;

import com.badlogic.gdx.math.Rectangle;

import ashley.core.Component;
import ashley.utils.Pool.Poolable;

public class BoundsComponent extends Component implements Poolable {
	public Rectangle bounds;
	
	public BoundsComponent() {
		bounds = null;
	}
	
	public BoundsComponent(BoundsComponent other) {
		bounds = other.bounds;
	}

	@Override
	public void reset() {
		bounds = null;
	}
}
