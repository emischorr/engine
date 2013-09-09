package de.micralon.engine.gui;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

public class ButtonInputListener extends InputListener {
	private Button m_button;
	
	public ButtonInputListener(Button button) {
		m_button = button;
	}
	
	@Override
    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
    {
		m_button.touched(true);
        return true;
    }
	
	public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
		m_button.touched(false);
	}
}
