package de.micralon.engine;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
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
	
	public void updateCameraPosition() {
		if (followTarget && target != null && target.getPos() != null) {
			camPos.set(target.getPos());
			// limit the view on the level angles
			if (camPos.x > CAMERA_RIGHT_LIMIT)
				camPos.x = CAMERA_RIGHT_LIMIT;
			else if (camPos.x < CAMERA_LEFT_LIMIT)
				camPos.x = CAMERA_LEFT_LIMIT;
			if (camPos.y > CAMERA_UP_LIMIT)
				camPos.y = CAMERA_UP_LIMIT;
			else if (camPos.y < CAMERA_DOWN_LIMIT)
				camPos.y = CAMERA_DOWN_LIMIT;
			camera.position.set(camPos.x, camPos.y, 0);
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
