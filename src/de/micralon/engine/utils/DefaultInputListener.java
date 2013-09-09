package de.micralon.engine.utils;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

public class DefaultInputListener extends InputListener {
	@Override
    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
    {
        return true;
    }
}
