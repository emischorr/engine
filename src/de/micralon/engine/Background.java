package de.micralon.engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;

public class Background {
	private Stage bgStage;
	private Camera movingCam;
	private Vector3 lastPos;
	private Array<ParallaxLayer> layers = new Array<ParallaxLayer>();
	
	public Background(Camera movingCam) {
		this.movingCam = movingCam;
		lastPos = movingCam.position.cpy();
		bgStage = new Stage();
		bgStage.setViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
	}
	
	public void addLayer(Drawable drawable, float ratio) {
		ParallaxLayer layer = new ParallaxLayer(drawable, ratio);
		layer.setSize(1024, 624);
		layers.add(layer);
		bgStage.addActor(layer);
	}
	
	public void update() {
		float xDiff = movingCam.position.x - lastPos.x;
		float yDiff = movingCam.position.y - lastPos.y;
		for (ParallaxLayer layer : layers) {
			if (layer.getX() + layer.getWidth() < 0) { // right end of the image reached left viewport border -> reset to left
				layer.setPosition(layer.getX() + layer.getWidth(), layer.getY());
			}
			else if (layer.getX() > 0) { // left end of the image reached right viewport border -> reset to right
				layer.setPosition(layer.getX() - layer.getWidth(), layer.getY());
			} else {
				layer.move(xDiff*-1, yDiff*-1);
			}
		}
		bgStage.act();
		lastPos = movingCam.position.cpy();
	}
	
	public void draw() {
		bgStage.draw();
	}
	
}
