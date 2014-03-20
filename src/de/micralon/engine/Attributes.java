package de.micralon.engine;

import com.badlogic.gdx.utils.ObjectMap;

public class Attributes {
	private ObjectMap<String, Float> attributes = new ObjectMap<String, Float>();
	
	public Attributes(String... attrList) {
		for (String attribute : attrList) {
			attributes.put(attribute, 0f);
		}
	}
	
	public float get(String attr) {
		return attributes.get(attr);
	}
	
	public void set(String attr, float value) {
		attributes.put(attr, value);
	}
	
	public void incr(String attr, float value) {
		attributes.put(attr, attributes.get(attr)+value);
	}
	
	public void decr(String attr, float value) {
		attributes.put(attr, attributes.get(attr)-value);
	}
	
	public boolean hasMinValue(String attr, float targetValue) {
		return attributes.get(attr) >= targetValue;
	}
}
