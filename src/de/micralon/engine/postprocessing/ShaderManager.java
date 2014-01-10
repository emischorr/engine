package de.micralon.engine.postprocessing;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.ObjectMap;

public class ShaderManager implements Disposable {
	private final static String INTERNAL_SHADER_PATH = "de/micralon/engine/postprocessing/shaders";
	
	public final static String DEFAULT_SHADER = "default";
	public final static String BLUR_SHADER = "blur";
	
	private ObjectMap<String, ShaderProgram> shaders = new ObjectMap<String, ShaderProgram>();
	private String shaderDir = "shaders";
	
	// temp vars
	private ShaderProgram shader;
	private String vertexShader, fragmentShader;
	private FileHandle fhtmp;
	
	public ShaderManager() {
		load(DEFAULT_SHADER, "default.vert", "default.frag");
		load(BLUR_SHADER, "blur.vert", "blur.frag");
	}
	
	public ShaderProgram get(String key) {
		return shaders.get(key);
	}
	
	public void load(String key, String vertPath, String fragPath) {
		load(key, Gdx.files.internal(vertPath), Gdx.files.internal(fragPath));
	}
	
	public void load(String key, FileHandle vertHandle, FileHandle fragHandle) {
		vertexShader = pathFallback(vertHandle).readString();
		fragmentShader = pathFallback(fragHandle).readString();
		
		//important since we aren't using some uniforms and attributes that SpriteBatch expects
		ShaderProgram.pedantic = false;
		
		shader = new ShaderProgram(vertexShader, fragmentShader);
		if (!shader.isCompiled()) {
			System.err.println(shader.getLog());
			System.exit(0);
		}
		if (shader.getLog().length()!=0)
			System.out.println(shader.getLog());
		
		shaders.put(key, shader);
		Gdx.app.log("ShaderManager", "Shader '" + key + "' loaded");
	}
	
	public void dispose() {
		for (ShaderProgram sp: shaders.values()) {
			sp.dispose();
		}
	}
	
	private FileHandle pathFallback(FileHandle fh) {
		if (fh == null)
			return null;
		if (!fh.exists()) {
			fhtmp = Gdx.files.internal(shaderDir + "/" + fh.path());
		}
		if (!fhtmp.exists()) {
			fhtmp = Gdx.files.classpath(INTERNAL_SHADER_PATH + "/" + fh.path());
		}
		if (!fhtmp.exists()) {
			throw new GdxRuntimeException("Shader not found: " + fh.path());
		}
		return fhtmp;
	}
}
