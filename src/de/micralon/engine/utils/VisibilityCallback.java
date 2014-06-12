package de.micralon.engine.utils;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;

public class VisibilityCallback implements RayCastCallback {
	private Object targetObject;
	private Class<?> targetClass;
	private boolean visible = true;
	
	public VisibilityCallback() {}
	
	public VisibilityCallback(Object targetActor) {
		this.targetObject = targetActor;
	}
	
	public VisibilityCallback(Class<?> targetClass) {
		this.targetClass = targetClass;
	}

	@Override
	public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
		if ( targetObject != null && fixture.getBody().getUserData().equals(targetObject) ) {
            return 0;
        }
		
		if ( targetClass != null && targetClass.isInstance(fixture.getBody().getUserData()) ) {
            return 0;
		}

        if (fixture.getBody().getUserData() instanceof String && ((String) fixture.getBody().getUserData()).equalsIgnoreCase("WORLD")) {
			visible = false;
			return 0;
        }

        return -1;
	}
	
	public boolean isVisible() {
		return visible;
	}
	
	public void reset() {
		visible = true;
	}

}
