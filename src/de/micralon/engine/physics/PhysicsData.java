package de.micralon.engine.physics;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.MassData;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

import de.micralon.engine.Env;

public class PhysicsData {
	BodyDef bodyDef = new BodyDef();
	MassData massData = new MassData();
	Array<FixtureDef> fixtureDefs = new Array<FixtureDef>();
	Array<String> fixtureNames = new Array<String>();
	ObjectMap<String, Integer> fixtureIdx = new ObjectMap<String, Integer>();
	
	PhysicsData() {
	}
	
	public BodyDef getBodyDef() {
		return bodyDef;
	}
	
	public MassData getMassData() {
		return massData;
	}
	
	public Array<FixtureDef> getFixtureDefs() {
		return fixtureDefs;
	}
	
	public int getFixtureIdx(String name) {
		return fixtureIdx.get(name);
	}
	
	public String getFixtureName(int index) {
		return fixtureNames.get(index);
	}
	
	public Body createBody(Object userData) {
		Body body = Env.game.getWorld().physicsWorld.createBody(bodyDef);
		body.setMassData(massData);
		body.setUserData(userData);
		
		for (int i = 0; i < fixtureDefs.size; ++i) {
			Fixture fixture = body.createFixture(fixtureDefs.get(i));
			fixture.setUserData(i);
		}
		
		return body;
	}
}
