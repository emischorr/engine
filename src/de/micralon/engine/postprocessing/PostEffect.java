package de.micralon.engine.postprocessing;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public abstract class PostEffect {
	private ShaderProgram shader;

	public void render(Texture input) {
		shader.begin();
		
		shader.end();
	}
}
