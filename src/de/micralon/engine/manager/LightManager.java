package de.micralon.engine.manager;

import box2dLight.ConeLight;
import box2dLight.DirectionalLight;
import box2dLight.Light;
import box2dLight.PointLight;
import box2dLight.RayHandler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.physics.box2d.World;

import de.micralon.engine.ObjectStore;

public class LightManager {
	public RayHandler rayHandler;
	private ObjectStore<PointLight> pointLights;
	private ObjectStore<ConeLight> coneLights;
	
	private static final int RAY_NUMS = 500;
	
	private DirectionalLight dl;
	
	//temp
	private PointLight tempPL;
	private ConeLight tempCL;
	
	public LightManager(World box2dWorld) {
//		RayHandler.setGammaCorrection(true);
        RayHandler.useDiffuseLight(true);
		rayHandler = new RayHandler(box2dWorld);
		rayHandler.setAmbientLight(0.4f, 0.4f, 0.4f, 0.3f);
		rayHandler.setCulling(true);            
        rayHandler.setBlur(true);
        rayHandler.setBlurNum(1);
        rayHandler.setShadows(true);
        rayHandler.setCulling(true);
	}
	
	public PointLight getPointLight(Color color, float distance) {
		if (pointLights == null) initPointLightStore();
		tempPL = pointLights.get();
		tempPL.setColor(color);
		tempPL.setDistance(distance);
		tempPL.setPosition(0, 0);
		tempPL.setStaticLight(true); // use static light as default
		return tempPL;
	}
	
	public ConeLight getConeLight(Color color, float distance) {
		if (coneLights == null) initConeLightStore();
		tempCL = coneLights.get();
		tempCL.setColor(color);
		tempCL.setDistance(distance);
		tempCL.setPosition(0, 0);
		tempCL.setStaticLight(true); // use static light as default
		return tempCL;
	}
	
	public DirectionalLight getDirectionalLight(Color color, float directionDegree) {
		if (dl != null) {
			return dl;
		} else {
			dl = new DirectionalLight(rayHandler, RAY_NUMS, color, directionDegree);
			dl.setStaticLight(false);
			return dl;
		}
	}
	
	public void freeLight(Light light) {
		light.setActive(false);
		if (light instanceof PointLight) {
			pointLights.free((PointLight) light);
		} else if (light instanceof ConeLight) {
			coneLights.free((ConeLight) light);
		}
	}
	
	public void clearLights() {
		pointLights.clear();
		coneLights.clear();
		dl.setActive(false);
	}
	
	public void dispose() {
		try {
			clearLights();
			rayHandler.dispose();
		} catch (Exception e) {
			Gdx.app.log("LightManger", "Error on disposing");
		}
	}
	
	private void initPointLightStore() {
		pointLights = new ObjectStore<PointLight>() {
        	public PointLight create() {
        		return new PointLight(rayHandler, RAY_NUMS);
        	}
        };
	}
	
	private void initConeLightStore() {
		coneLights = new ObjectStore<ConeLight>() {
        	public ConeLight create() {
        		return new ConeLight(rayHandler, RAY_NUMS, new Color(0.75f, 0.75f, 0.5f, 0.75f), 15f, 0f, 0f, 0f, 45f);
        	}
        };
	}
	
}
