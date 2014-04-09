package de.micralon.engine.map;

import java.util.HashSet;
import java.util.Set;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.Constructor;
import com.badlogic.gdx.utils.reflect.ReflectionException;

import de.micralon.engine.GameWorld;

public class MapBuilder {
	private final GameWorld world;
	private final ObjectMap<String, FixtureDef> m_materials = new ObjectMap<String, FixtureDef>();
	private float tileSize; // the size of a tile in world units (2m)
//	private int tilePixel = 128; // the size of a tile in Pixel (128)
	private final ObjectMap<String, Body> createdBodies = new ObjectMap<String, Body>();
	private final ObjectMap<String, Tile> createdTiles = new ObjectMap<String, Tile>();
	private ObjectMapper objectMapper;
	
	public Class<? extends IsoTile> isoTileClass = IsoTile.class;
	
	// shared temp vars
	private Tile tile;
	private Body body;
	private Fixture fix;
	private Filter filterData = new Filter();
	
//	private final float STOP_GAP = 0f;
	private final String LOG_TAG = "MapBuilder";
	private boolean mergeBodies;
	
	public MapBuilder(GameWorld world, String materialsFile) {
		this(world, materialsFile, 1, false);
	}
	
	public MapBuilder(GameWorld world, String materialsFile, float tileSize) {
		this(world, materialsFile, tileSize, false);
	}
	
	public MapBuilder(GameWorld world, String materialsFile, float tileSize, boolean mergeBodies) {
		this.world = world;
		this.tileSize = tileSize;
		this.mergeBodies = mergeBodies;
		
		FixtureDef defaultFixture = new FixtureDef();
		defaultFixture.density = 1.0f;
		defaultFixture.friction = 0.8f;
		defaultFixture.restitution = 0.0f;
		
		m_materials.put("default", defaultFixture);
		
		if (materialsFile != null) {
			loadMaterialsFile(materialsFile);
		}
	}
	
	public void setTileSize(float tileSize) {
		this.tileSize = tileSize;
	}
	
	public void setMergeBodies(boolean merge) {
		this.mergeBodies = merge;
	}
	
	public void addObjectMapper(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	public void build(TiledMap map) {
		TiledMapTileLayer physicsLayer = (TiledMapTileLayer) map.getLayers().get("physics");
		TiledMapTileLayer bgLayer = (TiledMapTileLayer) map.getLayers().get("bg");
		TiledMapTileLayer fgLayer = (TiledMapTileLayer) map.getLayers().get("fg");
		MapLayer objectsLayer = map.getLayers().get("objects");
		FixtureDef fixtureDef;
		Array<Body> mergingBodies = new Array<Body>();
		
		// get map info
		String orientation = (String) map.getProperties().get("orientation");
		int mapWidth = (Integer) map.getProperties().get("width");
		int mapHeight = (Integer) map.getProperties().get("height");
//		tileSize = (Float) map.getProperties().get("tilewidth");
		Tile.tileSize = tileSize;
		
		Cell cell;
			
		for (int y = mapHeight; y > 0 ; y--) {
			for (int x = 0; x < mapWidth; x++) {
				cell = null;
				
				// bg tile image
				if (bgLayer != null && bgLayer.getCell(x, y) != null) {
					createTile(orientation, bgLayer, x, y);
				}
				
				// physics tile
				if (physicsLayer != null) cell = physicsLayer.getCell(x, y);
				if (cell != null && cell.getTile() != null) {
					// get material info
					MapProperties properties = cell.getTile().getProperties();
					String material = properties.get("material", "default", String.class);
					// load fixture for material
					fixtureDef = m_materials.get(material);
					if (fixtureDef == null) {
						Gdx.app.log(LOG_TAG, "material does not exist " + material + " using default");
						fixtureDef = m_materials.get("default");
					}
					
					// create shape
					PolygonShape shape = new PolygonShape();
					shape.setAsBox(tileSize/2, tileSize/2);
					
					// add shape to fixture
					fixtureDef.shape = shape;
					
					if (mergeBodies) {
						// check if there are already adjacent bodies with same material
						for (Body neighbour : getAdjacentBodies(x, y)) {
							if (neighbour != null && material.equalsIgnoreCase((String) neighbour.getUserData())) {
								mergingBodies.add(neighbour);
							}
						}
					}
					
					if (mergingBodies.size > 0) {
						//TODO: merge bodies
						mergeTile(body, shape, x*tileSize, y*tileSize);
					} else {
						// create new body
						BodyDef bodyDef = new BodyDef();
						bodyDef.position.x = x*tileSize;
						bodyDef.position.y = y*tileSize;
						bodyDef.type = BodyDef.BodyType.StaticBody;
						
						body = world.box2dWorld.createBody(bodyDef);
						body.setUserData(material);
						fix = body.createFixture(fixtureDef);
					}
					
					// set filter data
					// TODO: allow custom filter
					fix.setFilterData(filterData);
					
					// add to body register to keep track of all bodies
					createdBodies.put(key(x,y), body);
					
					// physics tile image
					createTile(orientation, physicsLayer, x, y);
					
					fixtureDef = null;
					shape.dispose();
				}
				
				// fg tile image
				if (fgLayer != null && fgLayer.getCell(x, y) != null) {
					createTile(orientation, fgLayer, x, y);
				}
			}
		}
		
		if (objectsLayer != null) {
			if (objectMapper != null) {
				objectMapper.setTileSize(tileSize);
				for (MapObject obj : objectsLayer.getObjects()) {
					objectMapper.map(obj);
				}
			} else {
				Gdx.app.log(LOG_TAG, "Found object layer but no ObjectMapper defined! Use addObjectMapper() to add one.");
			}
		}
	}
	
	private void loadMaterialsFile(String materialsFile) {
		Gdx.app.log(LOG_TAG, "adding default material");
		
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.density = 1.0f;
		fixtureDef.friction = 1.0f;
		fixtureDef.restitution = 0.0f;
		m_materials.put("default", fixtureDef);
		
		Gdx.app.log(LOG_TAG, "loading materials file");
		
		try {
			XmlReader reader = new XmlReader();
			Element root = reader.parse(Gdx.files.internal(materialsFile));
			
			Array<Element> materials = root.getChildrenByName("materials");
			
			for (Element material : materials) {
				String name = material.getAttribute("name");
				
				if (name == null) {
					Gdx.app.log(LOG_TAG, "material without name");
					continue;
				}
				
				fixtureDef = new FixtureDef();
				fixtureDef.density = Float.parseFloat(material.getAttribute("density", "1.0"));
				fixtureDef.friction = Float.parseFloat(material.getAttribute("friction", "1.0"));
				fixtureDef.restitution = Float.parseFloat(material.getAttribute("restitution", "1.0"));
				Gdx.app.log(LOG_TAG, "adding material " + name);
				m_materials.put(name, fixtureDef);
			}
			
		} catch (Exception e) {
			Gdx.app.log(LOG_TAG, "error loading " + materialsFile + " " + e.getMessage());
		}
	}
	
	private void createTile(String orientation, TiledMapTileLayer layer, int x, int y) {
		if (orientation.equalsIgnoreCase("staggered")) {
			try {
				Constructor constructor = ClassReflection.getConstructor(isoTileClass, TextureRegion.class, int.class, int.class);
				tile = (Tile) constructor.newInstance(layer.getCell(x, y).getTile().getTextureRegion(), x, y);
			} catch (ReflectionException e) {
				e.printStackTrace();
			}
			addTileToLayer(tile, "physics");
		} else {
			tile = new SquareTile(layer.getCell(x, y).getTile().getTextureRegion(), x, y);
			addTileToLayer(tile, layer.getName());
		}
		// add to tile register to keep track of all tiles
		createdTiles.put(key(x,y), tile);
		tile = null;
	}
	
	private void addTileToLayer(Tile tile, String layerName) {
		if (layerName.equalsIgnoreCase("fg")) {
			world.fg.addActor(tile);
		} else if (layerName.equalsIgnoreCase("physics")) {
			world.physics.addActor(tile);
		} else {
			world.bg.addActor(tile);
		}
	}
	
	private String key(int x, int y) {
		return x+"/"+y;
	}
	
	public Tile getTile(int x, int y) {
		return createdTiles.get( key(x, y) );
	}
	
	/**
	 * Get an array of adjacent bodies (bodies that have one side in common). <br/>
	 * Only for none staggered maps!
	 * @param x coordinate of given body
	 * @param y coordinate of given body
	 * @return Array of bodies
	 */
	private Set<Body> getAdjacentBodies(int x, int y) {
		Set<Body> bodies = new HashSet<Body>();
		bodies.add(createdBodies.get( key(x+1, y) ));
		bodies.add(createdBodies.get( key(x-1, y) ));
		bodies.add(createdBodies.get( key(x, y+1) ));
		bodies.add(createdBodies.get( key(x, y-1) ));
		return bodies;
	}
	
	private void mergeTile(Body body, PolygonShape shape, float x, float y) {
		shape.setAsBox(tileSize/2, tileSize/2, new Vector2(x, y), 0);
		body.createFixture(shape, 0);
	}

}
