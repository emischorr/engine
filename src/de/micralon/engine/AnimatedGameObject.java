package de.micralon.engine;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Scaling;

public abstract class AnimatedGameObject extends GameObject {
	private transient Animation currentAnimation;
	private float stateTime = 0;
	
	protected AnimatedGameObject(BodyType type, float bodyWidth, float bodyHeight, float linearDamping, float angularDamping) {
		this(type, bodyWidth, bodyHeight, linearDamping, angularDamping, Scaling.stretch);
	}
	
	public AnimatedGameObject(BodyType type, float bodyWidth, float bodyHeight, float linearDamping, float angularDamping, Scaling scaling) {
		super(type, bodyWidth, bodyHeight, linearDamping, angularDamping, scaling);
	}
	
	protected void setAnimation(Animation animation) {
		if (currentAnimation != animation) {
			currentAnimation = animation;
			stateTime = 0;
		}
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
		stateTime += delta;
	}
	
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		if (currentAnimation != null) {
			batch.draw(currentAnimation.getKeyFrame(stateTime, true), getPos().x - bodyWidth/2 + textureOffsetX, getPos().y - bodyHeight/2 + textureOffsetY, bodyWidth, bodyHeight);
		} else {
			super.draw(batch, parentAlpha);
		}
	}
	
	protected Animation createAnimation(Array<AtlasRegion> animationFrames, float frameDuration) {
		return new Animation(frameDuration, animationFrames);
	}
	
	protected Animation createAnimation(Texture animationTexture, int frameCols, int frameRows,  float frameDuration) {
        TextureRegion[][] tmp = TextureRegion.split(animationTexture, animationTexture.getWidth() / frameCols, animationTexture.getHeight() / frameRows);
        TextureRegion[] animationFrames = new TextureRegion[frameCols * frameRows];
        
        int index = 0;
        for (int i = 0; i < frameRows; i++) {
                for (int j = 0; j < frameCols; j++) {
                	animationFrames[index++] = tmp[i][j];
                }
        }
        
		return new Animation(frameDuration, animationFrames);
	}

}
