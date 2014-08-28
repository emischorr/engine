package de.micralon.engine.entity.components;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.utils.IntMap;

import ashley.core.Component;
import ashley.utils.Pool.Poolable;

public class AnimationComponent extends Component implements Poolable {
	public IntMap<Animation> animations = new IntMap<Animation>();

	public AnimationComponent() {
		animations = null;
	}
	
	public AnimationComponent(AnimationComponent other) {
		animations = other.animations;
	}
	
	@Override
	public void reset() {
		animations = null;
	}
}
