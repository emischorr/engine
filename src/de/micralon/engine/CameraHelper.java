package de.micralon.engine;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.Cullable;

public class CameraHelper {
	private Stage stage;
	private OrthographicCamera camera;
	private Vector2 camPos = new Vector2();
	private int VIEW_WIDTH = 18;
	private int VIEW_HEIGHT = 12;
	private Rectangle cullingArea;
	private int lowerX, upperX, lowerY, upperY;
	private int CAMERA_RIGHT_LIMIT;
    private int CAMERA_LEFT_LIMIT;
    private int CAMERA_UP_LIMIT;
    private int CAMERA_DOWN_LIMIT;
	private GameObject target;
	public boolean followTarget = true;
	
	private boolean culling = true;
	public float zoom;
	
	public CameraHelper(Stage stage) {
		this.stage = stage;
		this.camera = (OrthographicCamera) stage.getCamera();
		
		cullingArea = new Rectangle();
	}
	
	public void setViewSize(int width, int height) {
		VIEW_WIDTH = width;
		VIEW_HEIGHT = height;
		
		updateCameraBorders();
		
		stage.setViewport(VIEW_WIDTH, VIEW_HEIGHT, false);
		cullingArea.set(0, 0, VIEW_WIDTH, VIEW_HEIGHT);
	}
	
	public float getZoom() {
		return camera.zoom;
	}
	
	public void setZoom(float zoom) {
		camera.zoom = zoom;
	}
	
	public void setTarget(GameObject object) {
		this.target = object;
	}
	
	public Rectangle getCullingArea() {
		return cullingArea;
	}
	
	public void applyCulling(Cullable cullable) {
		cullable.setCullingArea(cullingArea);
	}
	
	public void moveLeft(float distance) {
		setPosition(camera.position.x - distance, camera.position.y);
	}
	
	public void moveRight(float distance) {
		setPosition(camera.position.x + distance, camera.position.y);
	}
	
	public void moveUp(float distance) {
		setPosition(camera.position.x, camera.position.y + distance);
	}
	
	public void moveDown(float distance) {
		setPosition(camera.position.x, camera.position.y - distance);
	}
	
	public void setPosition(Vector3 pos) {
		setPosition(pos.x, pos.y);
	}
	
	public void setPosition(Vector2 pos) {
		setPosition(pos.x, pos.y);
	}
	
	public void setPosition(float x, float y) {
		// limit the view on the level borders
		if (x > CAMERA_RIGHT_LIMIT)
			x = CAMERA_RIGHT_LIMIT;
		else if (x < CAMERA_LEFT_LIMIT)
			x = CAMERA_LEFT_LIMIT;
		
		if (y > CAMERA_UP_LIMIT)
			y = CAMERA_UP_LIMIT;
		else if (y < CAMERA_DOWN_LIMIT)
			y = CAMERA_DOWN_LIMIT;
		
		camera.position.set(x, y, 0);
	}
	
	public void updateCameraPosition() {
		if (followTarget && target != null && target.getPos() != null) {
			// update position
			setPosition(target.getPos());
			// update cullingArea
			if (culling) cullingArea.setPosition(camPos.x - VIEW_WIDTH/2, camPos.y - VIEW_HEIGHT/2);
		}
	}
	
	public void updateCameraBorders(int X, int Y) {
		upperX = X;
		upperY = Y;
		updateCameraBorders();
	}
	
	private void updateCameraBorders() {
		CAMERA_RIGHT_LIMIT = upperX - VIEW_WIDTH/2 + 1;
	    CAMERA_LEFT_LIMIT = lowerX + VIEW_WIDTH/2 - 1;
	    CAMERA_UP_LIMIT = upperY - VIEW_HEIGHT/2 + 1;
	    CAMERA_DOWN_LIMIT = lowerY + VIEW_HEIGHT/2 - 1;
	}
}
