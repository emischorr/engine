package de.micralon.engine.scripting.commands;

import de.micralon.engine.GameWorld;
import de.micralon.engine.scripting.Command;
import de.micralon.engine.text.Text;

public class PrintText implements Command {
	private Text text;
	
	public PrintText() {
		
	}

	@Override
	public void update(float delta) {
		GameWorld.ctx.texts.add(text);
	}

	@Override
	public boolean isDone() {
		return true;
	}

	@Override
	public Command copy() {
		// TODO Auto-generated method stub
		return null;
	}

}
