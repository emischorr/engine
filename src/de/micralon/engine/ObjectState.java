package de.micralon.engine;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class ObjectState {
	public Vector2 position = new Vector2(0,0);
	public float rotation;
	public float width;
	public float height;
	
	public BodyType type = BodyType.StaticBody;
	public float linearDamping = 0;  
    public float angularDamping = 0;
    
    public ObjectState(ObjectState anotherState) {
    	this.position = anotherState.position;
    	this.rotation = anotherState.rotation;
    	this.width = anotherState.width;
    	this.height = anotherState.height;
    	this.type = anotherState.type;
    	this.linearDamping = anotherState.linearDamping;
    	this.angularDamping = anotherState.angularDamping;
    }
    
    public ObjectState(BodyType type, float width, float height) {
    	this(type, width, height, 0, 0);
    }
	
	public ObjectState(BodyType type, float width, float height, float linearDamping, float angularDamping) {
		this.type = type;
		this.width = width;
		this.height = height;
		this.linearDamping = linearDamping;
		this.angularDamping = angularDamping;
	}
}
