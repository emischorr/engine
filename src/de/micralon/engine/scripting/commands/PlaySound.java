package de.micralon.engine.scripting.commands;

import de.micralon.engine.EngineGame;
import de.micralon.engine.scripting.Command;
import de.micralon.engine.services.SoundManager.GameSound;

public class PlaySound implements Command {
	private GameSound sound;

	public PlaySound(GameSound sound) {
		this.sound = sound;
	}
	
	@Override
	public void update(float delta) {
		EngineGame.ctx.getSoundManager().play(sound);
	}

	@Override
	public boolean isDone() {
		return true;
	}

	@Override
	public Command copy() {
		return this;
	}

}
