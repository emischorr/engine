package de.micralon.engine;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import de.micralon.engine.background.Background;
import de.micralon.engine.gameobjects.GameObject;
import de.micralon.engine.manager.ContactManager;
import de.micralon.engine.manager.DecalsManager;
import de.micralon.engine.manager.EffectManager;
import de.micralon.engine.manager.LightManager;
import de.micralon.engine.manager.ObjectManager;
import de.micralon.engine.manager.PlayerManager;
import de.micralon.engine.map.GameMap;
import de.micralon.engine.map.ObjectMapper;
import de.micralon.engine.net.Network.ObjectsData;
import de.micralon.engine.net.NetworkNode;
import de.micralon.engine.pathfinding.Movable;
import de.micralon.engine.pathfinding.Pathfindable;
import de.micralon.engine.systems.GameSystem;
import de.micralon.engine.text.Text;
import de.micralon.engine.utils.Log;
import de.micralon.engine.utils.VisibilityCallback;

/**
 * The game world
 * @author Enrico Mischorr
 *
 */
public abstract class GameWorld implements Pathfindable {
	public LightManager lightManager;
	public World physicsWorld;
	public GameMap map;
	public Stage stage;
	public CameraHelper cameraHelper;
	public Array<Text> texts = new Array<Text>();
	
	public static GameWorld ctx;
	
	private static int WORLD_WIDTH = 50;
	private static int WORLD_HEIGHT = 50;
	
    protected GameTimer gameTimer = new GameTimer();
    
	private Level level;
    
	public Background background;
	
	public Group bg = new Group();
	public Group physics = new Group();
	public Group fg = new Group();
	
	public Player localPlayer;
	public Team defaultTeam;
	
	protected ObjectMapper objectMapper;
	
	private ObjectManager objectManager;
	private DecalsManager decalsManager;
	public EffectManager effectManager;
	public PlayerManager playerManager;
//	public HashMap<Integer, Player> players = new HashMap<Integer, Player>();
	protected Array<GameSystem> systems = new Array<GameSystem>();

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
		physicsWorld = new World(Env.gravity, true);
		lightManager = new LightManager(physicsWorld);
		new ContactManager(this);
		
		stage = new Stage(new ScreenViewport()); // create the game stage
        
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
        decalsManager = new DecalsManager();
        effectManager = new EffectManager();
        playerManager = new PlayerManager();
	}
	
	public boolean load(String levelName) {
		try {
			level = (Level)Class.forName(EngineGame.levelPath+levelName).getConstructor().newInstance();
			level.load();
			Log.info("Loaded level "+level);
			return true;
		} catch (Exception e) {
			Log.error("Error loading level "+level);
			Log.warn("EngineGame.levelPath is set to: "+EngineGame.levelPath);
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
		return gameTimer.getTimeInMs();
	}
	
	public ObjectManager getObjectManager() {
		return objectManager;
	}
	
	public ObjectMapper getObjectMapper() {
		return objectMapper;
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
	
	
	public final synchronized void addPlayer(Player player) {
		addPlayer(player, true);
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
		if (defaultTeam != null) {
			defaultTeam.addPlayer(player);
		}
	}
	
	public synchronized void removePlayer(int playerID) {
		playerManager.removePlayer(playerID);
	}
	
	public synchronized void createTeam(String name, Color color) {
		playerManager.addTeam(new Team(name, color));
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
	
	public void addDecal(GameDecal decal, Group group) {
		group.addActor(decalsManager.addDecal(decal));
	}
	
	public void addSystem(GameSystem system) {
		systems.add(system);
	}
	
	public void update(float deltaTime) {
		gameTimer.update(deltaTime);
		cameraHelper.updateCameraPosition();
		background.update();
		
		// since Box2D 2.2 we need to reset the friction of any existing contacts
		contacts = physicsWorld.getContactList();
		for (int i = 0; i < physicsWorld.getContactCount(); i++) {
			Contact contact = contacts.get(i);
			contact.resetFriction();
		}
		
		// update level script
		if (level != null) level.update(deltaTime);
		
		for (GameSystem system : systems) {
			system.update(deltaTime);
		}
		
		// TODO: try to move this at the end of the method
		// update box2d world
		physicsWorld.step(deltaTime, Env.velocityIterations, Env.positionIterations);
		
		stage.act(deltaTime); // update game stage
		objectManager.update(); // delete objects and update state
		decalsManager.update(deltaTime);
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
	
	@Override
	public float getMovementCost(Vector2 source, Vector2 target, Movable mover) {
		return source.dst(target);
	}

	@Override
	public boolean isBlocked(Vector2 target, Movable mover) {
		return false;
	}

	@Override
	public Array<Vector2> reachablePositions(Vector2 pos, Vector2 targetPos) {
		// Alternatives: reachableWaypoints(pos, targetPos)
		return neighbourCells(pos);
	}
	
	protected final Array<Vector2> neighbourCells(Vector2 pos) {
		Array<Vector2> reachable = new Array<Vector2>();
		reachable.add(pos.cpy().add(1, 0));
		reachable.add(pos.cpy().add(-1, 0));
		reachable.add(pos.cpy().add(0, 1));
		reachable.add(pos.cpy().add(0, -1));
		return reachable;
	}
	
	protected final Array<Vector2> reachableWaypoints(Vector2 pos, Vector2 targetPos) {
		Array<Vector2> reachable = new Array<Vector2>();
		VisibilityCallback callback = new VisibilityCallback();
		
		if (pointReachable(pos, targetPos, callback)) {
			reachable.add(targetPos);
			return reachable; // shortcut since target is already directly reachable
		}
		
		for (Vector2 waypoint : map.getWaypoints()) {
			if (pointReachable(pos, waypoint, callback)) reachable.add(waypoint);
		}
		
		return reachable;
	}
	
	private final boolean pointReachable(Vector2 pos, Vector2 targetPos, VisibilityCallback callback) {
		callback.reset();
		if (pos.dst(targetPos) > 0) physicsWorld.rayCast(callback, pos, targetPos);
		return callback.isVisible();
	}

	@Override
	public boolean toExpensive(Movable mover, float nextStepCost) {
		return false;
	}
	
	public void dispose() {
		lightManager.dispose();
		objectManager.dispose();
		effectManager.dispose();
		physicsWorld.dispose();
		stage.dispose();
	}
	
}
