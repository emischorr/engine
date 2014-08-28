package de.micralon.engine;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map.Entry;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;

import de.micralon.engine.utils.Log;

public class Settings {
	
	private String settingsFile;
	
	private HashMap<String, String> strings = new HashMap<String, String>();
	private HashMap<String, Integer> ints = new HashMap<String, Integer>();
	private HashMap<String, Float> floats = new HashMap<String, Float>();
	private HashMap<String, Boolean> booleans = new HashMap<String, Boolean>();
	private HashMap<String, Vector2> vectors = new HashMap<String, Vector2>();
	
	public Settings() {
		this("data/settings.xml");
	}
	
	public String getFile() {
		return settingsFile;
	}
	
	public void setFile(String settingsFile) {
		this.settingsFile = settingsFile;
	}
	
	public String getString(String key, String defaultValue) {
		String string = strings.get(key);
		
		if (string != null) { return string; }
		
		Log.info(key + " not found, returning default " + defaultValue);
		return defaultValue;
	}
	
	public int getInt(String key, int defaultValue) {
		Integer i = ints.get(key);
		
		if (i != null) { return i; }
		
		Log.info(key + " not found, returning default " + defaultValue);
		return defaultValue;
	}
	
	public float getFloat(String key, float defaultValue) {
		Float f = floats.get(key);
		
		if (f != null) { return f; }
		
		Log.info(key + " not found, returning default " + defaultValue);
		return defaultValue;
	}
	
	public boolean getBoolean(String key, boolean defaultValue) {
		Boolean b = booleans.get(key);
		
		if (b != null) { return b; }
		
		Log.info(key + " not found, returning default " + defaultValue);
		return defaultValue;
	}
	
	public Vector2 getVector(String key, Vector2 defaultValue) {
		Vector2 v = vectors.get(key);
		
		if (v != null) { return new Vector2(v); }
		
		Log.info(key + " not found, returning default " + defaultValue);
		return defaultValue;
	}
	
	public void setString(String key, String value) {
		strings.put(key, value);
	}
	
	public void setInt(String key, int value) {
		ints.put(key, value);
	}
	
	public void setFloat(String key, float value) {
		floats.put(key, value);
	}
	
	public void setBoolean(String key, boolean value) {
		booleans.put(key, value);
	}
	
	public void setVector(String key, Vector2 value) {
		vectors.put(key, new Vector2(value));
	}
	
	/**
	 * @param settings file to load
	 */
	public Settings(String settings) {
		this.settingsFile = settings;
		loadSettings();
	}
	
	public void loadSettings() {
		try {
			FileHandle fileHandle = null;
			
			if (Gdx.app.getType() != Application.ApplicationType.WebGL && Gdx.files.external(settingsFile).exists()) {
				fileHandle = Gdx.files.external(settingsFile);
			} else {
				fileHandle = Gdx.files.internal(settingsFile);
			}
			
			XmlReader reader = new XmlReader();
			Element root = reader.parse(fileHandle);
			
			
			// strings
			strings.clear();
			for (Element node : root.getChildrenByName("string")) {
				String key = node.getAttribute("key");
				String value = node.getAttribute("value");
				strings.put(key, value);
				Log.info("Settings: loaded string " + key + " = " + value);
			}
			
			// integers
			ints.clear();
			for (Element node : root.getChildrenByName("int")) {
				String key = node.getAttribute("key");
				Integer value = Integer.parseInt(node.getAttribute("value"));
				ints.put(key, value);
				Log.info("Settings: loaded int " + key + " = " + value);
			}
			
			// floats
			floats.clear();
			for (Element node : root.getChildrenByName("int")) {
				String key = node.getAttribute("key");
				Float value = Float.parseFloat(node.getAttribute("value"));
				floats.put(key, value);
				Log.info("Settings: loaded float " + key + " = " + value);
			}
			
			// booleans
			booleans.clear();
			for (Element node : root.getChildrenByName("int")) {
				String key = node.getAttribute("key");
				Boolean value = Boolean.parseBoolean(node.getAttribute("value"));
				booleans.put(key, value);
				Log.info("Settings: loaded boolean " + key + " = " + value);
			}
			
			// vectors
			vectors.clear();
			for (Element node : root.getChildrenByName("int")) {
				String key = node.getAttribute("key");
				Float x = Float.parseFloat(node.getAttribute("x"));
				Float y = Float.parseFloat(node.getAttribute("y"));
				vectors.put(key, new Vector2(x, y));
				Log.info("Settings: loaded vector " + key + " = (" + x + ", " + y + ")");
			}
			
			Log.info("Settings: successfully loaded");
			
		} catch (Exception e) {
			Log.error("Settings: error while loading config file: " + settingsFile + " " + e.getMessage());
		}
	}
	
	public void saveSettings() {
		if (Gdx.app.getType() != Application.ApplicationType.WebGL) {
			Log.info("Settings: saving settings to file " + settingsFile);
			XmlWriter xml;
			
			try {
				StringWriter writer = new StringWriter();
				xml = new XmlWriter(writer);
				
				// root
				xml = xml.element("settings");
				
				
				// strings
				for (Entry<String, String> entry : strings.entrySet()) {
					xml = xml.element("string");
					xml.attribute("key", entry.getKey());
					xml.attribute("value", entry.getValue());
					xml = xml.pop();
				}
				
				// ints
				for (Entry<String, Integer> entry : ints.entrySet()) {
					xml = xml.element("int");
					xml.attribute("key", entry.getKey());
					xml.attribute("value", entry.getValue());
					xml = xml.pop();
				}
				
				// floats
				for (Entry<String, Float> entry : floats.entrySet()) {
					xml = xml.element("float");
					xml.attribute("key", entry.getKey());
					xml.attribute("value", entry.getValue());
					xml = xml.pop();
				}
				
				// booleans
				for (Entry<String, Boolean> entry : booleans.entrySet()) {
					xml = xml.element("boolean");
					xml.attribute("key", entry.getKey());
					xml.attribute("value", entry.getValue());
					xml = xml.pop();
				}
				
				// vectors
				for (Entry<String, Vector2> entry : vectors.entrySet()) {
					xml = xml.element("vector");
					xml.attribute("key", entry.getKey());
					Vector2 vector = entry.getValue();
					xml.attribute("x", vector.x);
					xml.attribute("y", vector.y);
					xml = xml.pop();
				}
				
				xml = xml.pop();
				Gdx.files.external(settingsFile).writeString(writer.toString(), true);
				xml.close();
				
				Log.info("Settings: successfully saved");
				
			} catch (Exception e) {
				Log.error("Settings: error saving file " + settingsFile);
			}
		} else {
			Log.warn("Settings: saving not supported in HTML");
		}
	}
}
