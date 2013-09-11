package de.micralon.engine;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Scaling;

public abstract class AnimatedGameObject<WORLD extends GameWorld> extends GameObject<WORLD> {
	private transient Animation currentAnimation;
	private float stateTime = 0;

	public AnimatedGameObject(WORLD world, ObjectState state) {
		super(world, state);
	}
	
	public AnimatedGameObject(WORLD world, ObjectState state, Scaling scaling) {
		super(world, state, scaling);
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
			batch.draw(currentAnimation.getKeyFrame(stateTime, true), state.position.x - state.width/2 + textureOffsetX, state.position.y - state.height/2 + textureOffsetY, state.width, state.height);
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
