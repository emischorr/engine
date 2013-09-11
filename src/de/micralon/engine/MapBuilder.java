package de.micralon.engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;

public class MapBuilder {
	private GameWorld world;
	private ObjectMap<String, FixtureDef> m_materials = new ObjectMap<String, FixtureDef>();
	private float tileSize; // the size of a tile in world units (2m)
	private int tilePixel = 128; // the size of a tile in Pixel (128)
	
	// shared temp vars
	private Image image;
	
	private final float STOP_GAP = 0f;
	private final String LOG_TAG = "MapBuilder";
	
	public MapBuilder(GameWorld world, String materialsFile, float tileSize) {
		this.world = world;
		this.tileSize = tileSize;
		
		FixtureDef defaultFixture = new FixtureDef();
		defaultFixture.density = 1.0f;
		defaultFixture.friction = 0.8f;
		defaultFixture.restitution = 0.0f;
		
		m_materials.put("default", defaultFixture);
		
		if (materialsFile != null) {
			loadMaterialsFile(materialsFile);
		}
	}

	public void build(TiledMap map) {
		TiledMapTileLayer physicsLayer = (TiledMapTileLayer) map.getLayers().get("physics");
		TiledMapTileLayer bgLayer = (TiledMapTileLayer) map.getLayers().get("bg");
		TiledMapTileLayer fgLayer = (TiledMapTileLayer) map.getLayers().get("fg");
		MapLayer objectsLayer = map.getLayers().get("objects");
		MapLayer collectablesLayer = map.getLayers().get("collectables");
		FixtureDef fixtureDef;
		
//		tileSize = (Float) map.getProperties().get("tilewidth");
			
		for (int y = 0; y < physicsLayer.getHeight(); y++) {
			for (int x = 0; x < physicsLayer.getWidth(); x++) {
				
				// bg tile image
				if (bgLayer != null && bgLayer.getCell(x, y) != null) {
					createTile(bgLayer, x, y);
				}
				
				Cell cell = physicsLayer.getCell(x, y);
				
				if (cell != null && cell.getTile() != null) {
					PolygonShape shape;
					shape = new PolygonShape();
					shape.setAsBox(tileSize/2, tileSize/2);
					
					MapProperties properties = cell.getTile().getProperties();
					String material = properties.get("material", "default", String.class);
					fixtureDef = m_materials.get(material);
					
					if (fixtureDef == null) {
						Gdx.app.log(LOG_TAG, "material does not exist " + material + " using default");
						fixtureDef = m_materials.get("default");
					}
					
					fixtureDef.shape = shape;
					
					BodyDef bodyDef = new BodyDef();
					bodyDef.position.x = x*tileSize;
					bodyDef.position.y = y*tileSize;
					bodyDef.type = BodyDef.BodyType.StaticBody;
					
					Body body = world.box2dWorld.createBody(bodyDef);
					body.createFixture(fixtureDef);
					
					//TODO: add to body register to keep track of all bodies
					
					// physics tile image
					createTile(physicsLayer, x, y);
					
					fixtureDef = null;
					shape.dispose();
				}
				
				// fg tile image
				if (fgLayer != null && fgLayer.getCell(x, y) != null) {
					createTile(fgLayer, x, y);
				}
			}
		}
		
		for (MapObject obj : objectsLayer.getObjects()) {
			// TODO: move in sub class
//			if (obj instanceof RectangleMapObject && obj.getName().equalsIgnoreCase("orb")) {
//				Rectangle rect = ((RectangleMapObject)obj).getRectangle();
//				worldState.addObject(new Collectable(world, rect.getX()/tilePixel*tileSize, rect.getY()/tilePixel*tileSize));
//			}
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
	
	private void createTile(TiledMapTileLayer layer, int x, int y) {
		image = new Image(layer.getCell(x, y).getTile().getTextureRegion());
		image.setPosition(x*tileSize-tileSize*0.5f, y*tileSize-tileSize*0.5f);
		image.setSize(tileSize+STOP_GAP, tileSize+STOP_GAP);
		image.setScaling(Scaling.stretch); // stretch the texture  
		image.setAlign(Align.center);
		if (layer.getName().equalsIgnoreCase("fg")) {
			world.fg.addActor(image);
		} else if (layer.getName().equalsIgnoreCase("physics")) {
			world.physics.addActor(image);
		} else {
			world.bg.addActor(image);
		}
		image = null;
	}
}
