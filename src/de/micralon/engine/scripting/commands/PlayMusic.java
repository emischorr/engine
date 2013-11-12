package de.micralon.engine.scripting.commands;

import de.micralon.engine.EngineGame;
import de.micralon.engine.scripting.Command;
import de.micralon.engine.services.MusicManager.GameMusic;

public class PlayMusic implements Command {
	private GameMusic music;
	
	public PlayMusic(GameMusic music) {
		this.music = music;
	}

	@Override
	public void update(float delta) {
		EngineGame.ctx.getMusicManager().play(music);
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
