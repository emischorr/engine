package de.micralon.engine.entity.factory;

import java.lang.reflect.Constructor;

import ashley.core.Component;
import ashley.core.Entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonValue.JsonIterator;
import com.badlogic.gdx.utils.ObjectMap;

import de.micralon.engine.Env;
import de.micralon.engine.utils.Log;

public class EntityFactory {
	private static final String TYPES_FILE = "data/entities/types.json"; 

	private ObjectMap<String, Entity> types = new ObjectMap<String, Entity>();
	private ObjectMap<Class<? extends Component>, ComponentReader> readers = new ObjectMap<Class<? extends Component>, ComponentReader>();
	private ObjectMap<Class<? extends Component>, Constructor<? extends Component>> constructors = new ObjectMap<Class<? extends Component>, Constructor<? extends Component>>();
	
	public void registerReader(ComponentReader reader) {
		Class<? extends Component> c = reader.getComponentClass();
		
		Log.info("EntityFactory: registering reader for component " + c.getSimpleName());
		
		if (readers.containsKey(c)) {
			Log.error("EntityFactory: reader for component " + c.getSimpleName() + "already exists");
			return;
		}
		
		// Check whether or not the component class is copy-constructable
		try {
			Constructor<? extends Component> constructor = c.getConstructor(c);
			constructors.put(c, constructor);
		}
		catch (NoSuchMethodException e) {
			Log.error("EntityFactory: component does not have a copy constructor " + c.getSimpleName());
			return;
		}
		
		readers.put(c, reader);
	}
	
	public void loadArchetypes() {
		try {
			JsonReader reader = new JsonReader();
			JsonValue root = reader.parse(Gdx.files.internal(TYPES_FILE));
			JsonIterator entitiesIt = root.iterator();
			
			while (entitiesIt.hasNext()) {
				JsonValue entity = entitiesIt.next();
				loadEntityType(Gdx.files.internal(entity.asString()));
			}
			
		} catch (Exception e) {
			Log.error("EntityFactory: failed to load entity types from " + TYPES_FILE);
		}
	}
	
	@SuppressWarnings("unchecked")
	private void loadEntityType(FileHandle file) {
		Log.info("EntityFactory: loading entity type " + file.name());
		
		try {
			JsonReader reader = new JsonReader();
			JsonValue root = reader.parse(file);
			JsonIterator componentIt = root.iterator();
			
			Entity entity = new Entity();
			
			while (componentIt.hasNext()) {
				JsonValue componentValue = componentIt.next();
				String name = componentValue.get("class").asString();
				Class<? extends Component> componentClass = (Class<? extends Component>)Class.forName(name);
				
				if (componentClass == null) {
					Log.error("EntityFactory: no component class " + name);
					continue;
				}
				
				ComponentReader componentReader = readers.get(componentClass);
				
				if (componentReader == null) {
					Log.error("EntityFactory: component reader not found for class " + name);
					continue;
				}
				
				Component component = componentReader.read(componentValue);
				
				if (component != null) {
					Log.info("EntityFactory: adding component "+name+" to entity");
					entity.add(component);
				}
			}
			
			types.put(file.nameWithoutExtension(), entity);
			
		} catch (Exception e) {
			Log.error("EntityFactory: error reading entity type from " + file.name());
		}
	}
	
	public Entity createEntity(String typeName) {
		Log.info("EntityFactory: creating new entity from type " + typeName);
		
		Entity type = types.get(typeName);
		
		if (type == null) {
			Log.error("type " + typeName + " does not exist ");
		}
		
		Entity newEntity = new Entity();
		
		//TODO: get components and add them to entity
		
		Log.info("EntityFactory: adding entity of type " + typeName + " to engine ");
		Env.game.getEngine().addEntity(newEntity);
		
		return newEntity;
	}
	
	public static interface ComponentReader {
		public Class<? extends Component> getComponentClass();
		public Component read(JsonValue value);
	}
}
