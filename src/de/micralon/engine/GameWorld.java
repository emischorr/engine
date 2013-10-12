package de.micralon.engine;

import java.util.HashMap;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;

public abstract class GameWorld {
	public LightManager lightManager;
	public World box2dWorld;
	public Stage stage;
	public Array<NameTag> tags = new Array<NameTag>();
	
	private static final float DEFAULT_GRAVITY = -9.8f;
	
    private final static int VELOCITY_ITERS = 6;
    private final static int POSITION_ITERS = 2;
	
    private Rectangle cullingArea;
    
    protected static int VIEW_WIDTH = 18;
	protected static int VIEW_HEIGHT = 12;
	
	private static int WORLD_WIDTH = 50;
	private static int WORLD_HEIGHT = 50;
	
	public GameObject<?> focusObject;
	private Vector2 camPos = new Vector2();
    private static int CAMERA_RIGHT_LIMIT = WORLD_WIDTH - VIEW_WIDTH/2 + 1;
    private static int CAMERA_LEFT_LIMIT = VIEW_WIDTH/2 - 1;
    private static int CAMERA_UP_LIMIT = WORLD_HEIGHT - VIEW_HEIGHT/2 + 1;
    private static int CAMERA_DOWN_LIMIT = VIEW_HEIGHT/2 - 1;
	
    private long gameTime;
    
	public Background background;
	
	public Group bg = new Group();
	public Group physics = new Group();
	public Group fg = new Group();
	
	public Player localPlayer;
	
	private ObjectManager objectManager;
	public HashMap<Integer, Player> players = new HashMap<Integer, Player>();

	// temp vars
	Array<Contact> contacts;
	
	
	public GameWorld() {
		init();
	}
	
	private void init() {
		box2dWorld = new World(new Vector2(0, getGravity()), true);
		lightManager = new LightManager(box2dWorld);
		new ContactManager(this);
		
		stage = new Stage(); // create the game stage
        stage.setViewport(VIEW_WIDTH, VIEW_HEIGHT, false); // set the game stage viewport to the meters size
        
        cullingArea = new Rectangle(0, 0, VIEW_WIDTH, VIEW_HEIGHT);
        bg.setCullingArea(cullingArea);
        physics.setCullingArea(cullingArea);
        fg.setCullingArea(cullingArea);
        stage.addActor(bg);
        stage.addActor(physics);
        stage.addActor(fg);
        
        background = new Background(stage.getCamera());
        
        objectManager = new ObjectManager();
	}
	
	public long getGameTime() {
		return gameTime;
	}
	
	public ObjectManager getObjectManager() {
		return objectManager;
	}
	
	public void setWorldSize(int width, int height) {
		WORLD_WIDTH = width;
		WORLD_HEIGHT = height;
		
		updateCameraBorders();
	}
	
	public int getWorldWidth() {
		return WORLD_WIDTH;
	}
	
	public int getWorldHeight() {
		return WORLD_HEIGHT;
	}
	
	public void setViewSize(int width, int height) {
		VIEW_WIDTH = width;
		VIEW_HEIGHT = height;
		
		updateCameraBorders();
		
		stage.setViewport(VIEW_WIDTH, VIEW_HEIGHT, false);
		cullingArea.set(0, 0, VIEW_WIDTH, VIEW_HEIGHT);
	}
	
	private void updateCameraBorders() {
		CAMERA_RIGHT_LIMIT = WORLD_WIDTH - VIEW_WIDTH/2 + 1;
	    CAMERA_LEFT_LIMIT = VIEW_WIDTH/2 - 1;
	    CAMERA_UP_LIMIT = WORLD_HEIGHT - VIEW_HEIGHT/2 + 1;
	    CAMERA_DOWN_LIMIT = VIEW_HEIGHT/2 - 1;
	}
	
	/**
	 * Only called by server
	 */
	public synchronized void addPlayer(Player player, boolean local) {
		// set player ID
		player.ID = players.size();
		// add player to list
		players.put(player.ID, player);
		// add player to local world
		if (local) {
			localPlayer = player;
		}
	}
	
	public void removePlayer(int playerID) {
		players.remove(playerID);
	}
	
	public void addObject(GameObject<?> obj) {
		addObject(obj, physics);
	}
	
	public void addObject(GameObject<?> obj, Group group) {
		group.addActor(obj);
		objectManager.add(obj);
	}
	
	public void removeObject(GameObject<?> obj) {
		physics.removeActor(obj);
		objectManager.remove(obj);
	}
	
	public void update(float deltaTime) {
		gameTime += deltaTime;
		updateCameraPosition();
		background.update();
		
		// since Box2D 2.2 we need to reset the friction of any existing contacts
		contacts = box2dWorld.getContactList();
		for (int i = 0; i < box2dWorld.getContactCount(); i++) {
			Contact contact = contacts.get(i);
			contact.resetFriction();
		}
		
		// update box2d world
		box2dWorld.step(deltaTime, VELOCITY_ITERS, POSITION_ITERS);
		
		stage.act(deltaTime); // update game stage
		objectManager.update(); // delete objects and update state
	}
	
	private void updateCameraPosition() {
		if (focusObject != null && focusObject.getPos() != null) {
			camPos.set(focusObject.getPos());
			// limit the view on the level angles
			if (camPos.x > CAMERA_RIGHT_LIMIT)
				camPos.x = CAMERA_RIGHT_LIMIT;
			else if (camPos.x < CAMERA_LEFT_LIMIT)
				camPos.x = CAMERA_LEFT_LIMIT;
			if (camPos.y > CAMERA_UP_LIMIT)
				camPos.y = CAMERA_UP_LIMIT;
			else if (camPos.y < CAMERA_DOWN_LIMIT)
				camPos.y = CAMERA_DOWN_LIMIT;
			stage.getCamera().position.set(camPos.x, camPos.y, 0);
			// update cullingArea
			cullingArea.setPosition(camPos.x - VIEW_WIDTH/2, camPos.y - VIEW_HEIGHT/2);
		}
	}
	
	public float getGravity() {
		return DEFAULT_GRAVITY;
	}
	
	public void dispose() {
		lightManager.dispose();
		box2dWorld.dispose();
		stage.dispose();
	}
	
}
