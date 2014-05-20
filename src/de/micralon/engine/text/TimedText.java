package de.micralon.engine.text;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import de.micralon.engine.GameWorld;

public class TimedText extends Text {
	private float duration;
	
	public TimedText(BitmapFont font, Color color, String text, float duration) {
		super(font, color, text);
		this.duration = duration;
	}

	public TimedText(BitmapFont font, Color color, String text, float duration, float x, float y) {
		super(font, color, text, x, y);
		this.duration = duration;
	}
	
	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		
		if(stateTime > duration) {
            GameWorld.ctx.texts.removeValue(this, true);
        	return;
        }
        
        if(stateTime < 0.4f) {
        	alpha = stateTime / 0.4f;
        }
        
        if(stateTime > duration - 0.2f) {                        
        	alpha = ((duration - stateTime) / 0.2f);
        }
	}

}
