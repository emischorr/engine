package de.micralon.engine;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Array;

import de.micralon.engine.builder.BodyBuilder;

public class Trigger {
	private Body body;
	private Array<GameObject> objects = new Array<GameObject>();
	private Runnable inAction;
	private Runnable outAction;
	private boolean active = true;

	public Trigger(GameWorld world, float posX, float posY) {
		BodyBuilder bodyBuilder = new BodyBuilder(world.box2dWorld);
		body = bodyBuilder
				.type(BodyType.StaticBody)
				.position(posX, posY)
				.userData(this)
				.fixture(bodyBuilder.fixtureDefBuilder()
						.sensor())
				.build();
	}
	
	public void setInAction(Runnable action) {
		this.inAction = action;
	}
	
	public void setOutAction(Runnable action) {
		this.outAction = action;
	}
	
	public void setActive(boolean active) {
		this.active = active;
		for (GameObject obj : objects) {
			if (active) {
				triggerIn(obj);
			} else {
				triggerOut(obj);
			}
		}
	}
	
	public void triggerIn(GameObject obj) {
		objects.add(obj);
		if (active) {
			inAction.run();
		}
	}
	
	public void triggerOut(GameObject obj) {
		objects.removeValue(obj, true);
		if (active) {
			outAction.run();
		}
	}
}
