package de.micralon.engine.map;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;

public abstract class ObjectMapper {
	protected float tileSize;
	
	// temp vars
	private Object value;
	
	public void setTileSize(float tileSize) {
		this.tileSize = tileSize;
	}
	
	public abstract void map(MapObject obj);
	
	protected String getClass(MapProperties properties) {
		return getProperty(properties, "class", "");
	}
	
	/**
	 * 
	 * @param properties
	 * @return X coordinate in world units
	 */
	protected float getX(MapProperties properties) {
		return getProperty(properties, "x", 0f)*tileSize;
	}
	
	/**
	 * 
	 * @param properties
	 * @return Y coordinate in world units
	 */
	protected float getY(MapProperties properties) {
		return getProperty(properties, "y", 0f)*tileSize;
	}
	
	@SuppressWarnings("unchecked")
	protected <T> T getProperty(MapProperties properties, String key, T defaultValue) {
		if(properties == null || key == null)
			return defaultValue;

		value = properties.get(key);

		if(value == null || value instanceof String && ((String) value).isEmpty())
			return defaultValue;

		if(defaultValue != null) {
			if(defaultValue.getClass() == Boolean.class && !(value instanceof Boolean))
				return (T) Boolean.valueOf(value.toString());

			if(defaultValue.getClass() == Integer.class && !(value instanceof Integer))
				return (T) Integer.valueOf(Float.valueOf(value.toString()).intValue());

			if(defaultValue.getClass() == Float.class && !(value instanceof Float))
				return (T) Float.valueOf(value.toString());

			if(defaultValue.getClass() == Double.class && !(value instanceof Double))
				return (T) Double.valueOf(value.toString());

			if(defaultValue.getClass() == Long.class && !(value instanceof Long))
				return (T) Long.valueOf(value.toString());

			if(defaultValue.getClass() == Short.class && !(value instanceof Short))
				return (T) Short.valueOf(value.toString());

			if(defaultValue.getClass() == Byte.class && !(value instanceof Byte))
				return (T) Byte.valueOf(value.toString());
		}

		return (T) value;
	}
}
