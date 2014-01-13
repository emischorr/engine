package de.micralon.engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;

import de.micralon.engine.background.Background;
import de.micralon.engine.manager.ContactManager;
import de.micralon.engine.manager.EffectManager;
import de.micralon.engine.manager.LightManager;
import de.micralon.engine.manager.ObjectManager;
import de.micralon.engine.manager.PlayerManager;
import de.micralon.engine.net.Network.ObjectsData;
import de.micralon.engine.net.NetworkNode;
import de.micralon.engine.text.Text;

public abstract class GameWorld {
	public LightManager lightManager;
	public World box2dWorld;
	public Stage stage;
	public CameraHelper cameraHelper;
	public Array<Text> texts = new Array<Text>();
	
	public static GameWorld ctx;
	
	private static final float DEFAULT_GRAVITY = -9.8f;
	
    private final static int VELOCITY_ITERS = 6;
    private final static int POSITION_ITERS = 2;
	
	private static int WORLD_WIDTH = 50;
	private static int WORLD_HEIGHT = 50;
	
    private long gameTime;
    
	private Level level;
    
	public Background background;
	
	public Group bg = new Group();
	public Group physics = new Group();
	public Group fg = new Group();
	
	public Player localPlayer;
	
	private ObjectManager objectManager;
	public EffectManager effectManager;
	public PlayerManager playerManager;
//	public HashMap<Integer, Player> players = new HashMap<Integer, Player>();

	// temp vars
	Array<Contact> contacts;
	
	
	public GameWorld() {
		ctx = this;
		init(false);
	}
	
	/**
	 * Initialize world. DON'T FORGET to call this in your world implementation
	 * @param trackUpdates
	 */
	protected final void init(boolean trackUpdates) {
		box2dWorld = new World(new Vector2(0, getGravity()), true);
		lightManager = new LightManager(box2dWorld);
		new ContactManager(this);
		
		stage = new Stage(); // create the game stage
        
        cameraHelper = new CameraHelper(stage);
        cameraHelper.setViewSize(18, 12); // view in meters
        
        cameraHelper.applyCulling(bg);
        cameraHelper.applyCulling(physics);
        cameraHelper.applyCulling(fg);
        stage.addActor(bg);
        stage.addActor(physics);
        stage.addActor(fg);
        
        background = new Background(stage.getCamera());
        
        objectManager = new ObjectManager(trackUpdates);
        effectManager = new EffectManager();
        playerManager = new PlayerManager();
	}
	
	public boolean load(String levelName) {
		try {
			level = (Level)Class.forName(levelName).getConstructor().newInstance();
			level.load();
			Gdx.app.log("GameWorld", "Loaded level "+level);
			return true;
		} catch (Exception e) {
			Gdx.app.log("GameWorld", "Error loading level "+level);
			e.printStackTrace();
			return false;
		}
	}
	
	public Level currentLevel() {
		return level;
	}
	
	/**
	 * GameTime in milliseconds
	 * @return the actual game time in ms
	 */
	public long getGameTime() {
		return gameTime;
	}
	
	public ObjectManager getObjectManager() {
		return objectManager;
	}
	
	/**
	 * Sets the world size in meters
	 * @param width
	 * @param height
	 */
	public void setWorldSize(int width, int height) {
		WORLD_WIDTH = width;
		WORLD_HEIGHT = height;
		
		cameraHelper.updateCameraBorders(WORLD_WIDTH, WORLD_HEIGHT);
	}
	
	public int getWorldWidth() {
		return WORLD_WIDTH;
	}
	
	public int getWorldHeight() {
		return WORLD_HEIGHT;
	}
	
	/**
	 * Only called by server
	 */
	public synchronized void addPlayer(Player player, boolean local) {
		// add player to list
		playerManager.addPlayer(player);
		// add player to local world
		if (local) {
			localPlayer = player;
		}
	}
	
	public void removePlayer(int playerID) {
		playerManager.removePlayer(playerID);
	}
	
	public void addObject(GameObject obj) {
		addObject(obj, physics);
	}
	
	public void addObject(GameObject obj, Group group) {
		group.addActor(obj);
		objectManager.add(obj);
	}
	
	public void removeObject(GameObject obj) {
		physics.removeActor(obj);
		objectManager.remove(obj);
	}
	
	public void update(float deltaTime) {
		gameTime = (long) (gameTime + deltaTime*1000);
		cameraHelper.updateCameraPosition();
		background.update();
		
		// since Box2D 2.2 we need to reset the friction of any existing contacts
		contacts = box2dWorld.getContactList();
		for (int i = 0; i < box2dWorld.getContactCount(); i++) {
			Contact contact = contacts.get(i);
			contact.resetFriction();
		}
		
		// update level script
		if (level != null) level.update(deltaTime);
		
		// TODO: try to move this at the end of the method
		// update box2d world
		box2dWorld.step(deltaTime, VELOCITY_ITERS, POSITION_ITERS);
		
		stage.act(deltaTime); // update game stage
		objectManager.update(); // delete objects and update state
		effectManager.update(deltaTime); // delete effects
		
		for (Text text : texts) {
			text.update(deltaTime);
		}
	}
	
	public void sync(NetworkNode node) {
		node.sendMessage(objectManager.getUpdateData());
	}
	
	public void updateObjects(ObjectsData data) {
		objectManager.syncObjects(data);
	}
	
	public float getGravity() {
		return DEFAULT_GRAVITY;
	}
	
	public void dispose() {
		lightManager.dispose();
		objectManager.dispose();
		effectManager.dispose();
		box2dWorld.dispose();
		stage.dispose();
	}
	
}
