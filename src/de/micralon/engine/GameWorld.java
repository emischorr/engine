package de.micralon.engine;

import java.util.HashMap;
import java.util.List;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;

public abstract class GameWorld {
	public LightManager lightManager;
	public World box2dWorld;
	public Stage stage;
	
	private static final float DEFAULT_GRAVITY = -9.8f;
	
    private final static int VELOCITY_ITERS = 6;
    private final static int POSITION_ITERS = 2;
	
    private Rectangle cullingArea;
    
    protected static final int VIEW_WIDTH = 18;
	protected static final int VIEW_HEIGHT = 12;
	
	//TODO: calculate: map size * tile size
	protected static final int WORLD_WIDTH = 120;
	protected static final int WORLD_HEIGHT = 20;
	
	public GameObject<?> focusObject;
	private Vector2 camPos = new Vector2();
    private static final int CAMERA_RIGHT_LIMIT = WORLD_WIDTH - VIEW_WIDTH/2 - 1;
    private static final int CAMERA_LEFT_LIMIT = VIEW_WIDTH/2 - 1;
    private static final int CAMERA_UP_LIMIT = WORLD_HEIGHT - VIEW_HEIGHT/2 - 1;
    private static final int CAMERA_DOWN_LIMIT = VIEW_HEIGHT/2 - 1;
	
	public Background background;
	
	public Group bg = new Group();
	public Group physics = new Group();
	public Group fg = new Group();
	
	public Player localPlayer;
	
	private ObjectManager objectManager;
	public HashMap<Integer, Player> players = new HashMap<Integer, Player>();

	
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
	
	/**
	 * Only called by server
	 */
	public synchronized void addPlayer(Player player, boolean local) {
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
		objectManager.add(obj);
		physics.addActor(obj);
	}
	
	public void removeObject(GameObject<?> obj) {
		objectManager.remove(obj);
	}
	
	public void update(float deltaTime) {
		updateCameraPosition();
		background.update();
		
		// since Box2D 2.2 we need to reset the friction of any existing contacts
		List<Contact> contacts = box2dWorld.getContactList();
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
		if (focusObject != null) {
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
