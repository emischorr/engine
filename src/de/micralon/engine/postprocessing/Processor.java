package de.micralon.engine.postprocessing;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectMap.Entry;

public class Processor implements Disposable{
	private Mesh quad;
	private ObjectMap<String, Object> uniforms = new ObjectMap<String, Object>();
	private FrameBuffer fbo, fbo2, tmp;
	private final Color clearColor = Color.CLEAR;
	private int clearBits = GL20.GL_COLOR_BUFFER_BIT;
	private final float clearDepth = 1f;
	private boolean useDepth;
	
	//TODO: make array
	ShaderProgram shader;
	private Array<PostEffect> effects;
	
	// temp vars
	private String cls;
	private TextureRegion fboRegion;
	
	public Processor() {
		this(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), false);
	}
	
	public Processor(Pixmap.Format fboFormat, int fboSize, boolean useDepth) {
		this.useDepth = useDepth;
		
		fbo = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), useDepth);
		fbo2 = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), useDepth);
		createQuad();
		
		if (useDepth) {
			clearBits |= GL20.GL_DEPTH_BUFFER_BIT;
		}
	}
	
	public void setTextureFilter(TextureFilter filter) {
		fbo.getColorBufferTexture().setFilter(filter, filter);
	}
	
	@Deprecated
	public void setUniform(String name, Object value) {
		uniforms.put(name, value);
	}
	
	@Deprecated
	public void setUniform(String name, Object... values) {
		uniforms.put(name, values);
	}
	
	@Deprecated
	public void run(ShaderProgram shader) {
		fbo.begin();
			Gdx.gl.glClearColor(0, 0, 0, 1);
			Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT );
			
			shader.begin();
			fbo.getColorBufferTexture().bind();
//			shader.setUniformi("u_texture", texId);
//			shader.setUniformf("u_viewport", width, height);
			setUniforms(shader);
			quad.render(shader, GL20.GL_TRIANGLES);
			shader.end();
		fbo.end();
		swapBuffers();
	}
	
	public void addShader(ShaderProgram shader) {
		this.shader = shader;
	}
	
	public void addEffect(PostEffect effect) {
		if (effects == null) initPostEffects();
		
		effects.add(effect);
	}
	
	private void initPostEffects() {
		effects = new Array<PostEffect>();
	}

	public void capture() {
		if (shader != null) {
			swapBuffers();
			fbo.begin();
			
			if (useDepth) {
				Gdx.gl.glClearDepthf(clearDepth);
			}
	
			Gdx.gl.glClearColor(clearColor.r, clearColor.g, clearColor.b, clearColor.a);
			Gdx.gl.glClear(clearBits);
		}
	}
	
	public Texture endCapture() {
		fbo.end();
		return fbo.getColorBufferTexture();
	}
	
	public TextureRegion dump() {
		fboRegion = new TextureRegion(fbo.getColorBufferTexture());
		fboRegion.flip(false,  true);
		
		return fboRegion;
	}
	
	public void renderTo(SpriteBatch batch) {
		if (shader != null) {
			fboRegion = new TextureRegion(endCapture());
			fboRegion.flip(false,  true);
			
			applyEffects();
			
			batch.begin();
			batch.draw(fboRegion, 0, 0);
			batch.end();
		}
	}
	
	public void render(FrameBuffer dest) {
		if (shader != null) {
			endCapture();
			
			applyEffects();
			
			quad.render(shader, GL20.GL_TRIANGLES);
			
			// ensure default texture unit #0 is active
			Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0);
		}
	}
	
	public void render () {
		render(null);
	}
	
	public Texture getResult() {
		return fbo.getColorBufferTexture();
	}
	
	@Override
	public void dispose() {
		tmp.dispose();
		fbo.dispose();
		fbo2.dispose();
		quad.dispose();
	}
	
	private void swapBuffers() {
		tmp = fbo;
		fbo = fbo2;
		fbo2 = tmp;
	}
	
	private void applyEffects() {
		if (effects != null) {
			for (PostEffect effect : effects) {
				swapBuffers();
				fbo.begin();
					effect.render();
				fbo.end();
			}
		}
	}
	
	@Deprecated
	private void setUniforms(ShaderProgram shader) {
		for (Entry<String, Object> entry: uniforms.entries()) {
			if (entry.value.getClass().isArray()) {
				addUniformsArray(shader, entry.key, (Object[]) entry.value);
				continue;
			}
			else {
				addUniform(shader, entry.key, entry.value);
			}
		}
	}
	
	@Deprecated
	private void addUniform(ShaderProgram program, String key, Object value) {
		cls = value.getClass().getName();
		//TODO: change that to hashtable? Check performance first!
		if (cls.equals("java.lang.Integer"))
			program.setUniformi(key, (Integer) value);
		else if (cls.equals("java.lang.Float"))
			program.setUniformf(key, (Float) value);
//		else if (cls.equals("com.badlogic.gdx.graphics.Texture")) {
//			Texture tex = (Texture) value;
//			int texId = sm.getCurrentTextureId();
//			tex.bind(texId);
//			program.setUniformi(key, texId);
//		}
		else if (cls.equals("com.badlogic.gdx.graphics.Color")) {
			Color val = (Color) value;
			program.setUniformf(key, val.r, val.g, val.b, val.a);
		}
		else if (cls.equals("com.badlogic.gdx.math.Vector2")) {
			Vector2 val = (Vector2) value;
			program.setUniformf(key, val.x, val.y);
		}
		else if (cls.equals("com.badlogic.gdx.math.Vector3")) {
			Vector3 val = (Vector3) value;
			program.setUniformf(key, val.x, val.y, val.z);
		}
		else if (cls.equals("com.badlogic.gdx.math.Matrix3")) {
			program.setUniformMatrix(key, (Matrix3) value);
		}
		else if (cls.equals("com.badlogic.gdx.math.Matrix4")) {
			program.setUniformMatrix(key, (Matrix4) value);
		}
		else {
			throw new GdxRuntimeException("Class " + cls + " as uniform: not implemented yet!");
		}
	}
	
	@Deprecated
	private void addUniformsArray(ShaderProgram program, String key, Object[] values) {
		if (values == null || values.length == 0) return;
		cls = values[0].getClass().getName();
	}
	
	private void createQuad() {
		if (quad != null)
			return;
		quad = new Mesh(true, 4, 6, new VertexAttribute(Usage.Position, 3,
	        "a_position"), new VertexAttribute(Usage.Color, 4, "a_color"),
	        new VertexAttribute(Usage.TextureCoordinates, 2, "a_texCoords"));

	    quad.setVertices(new float[]{-1, -1, 0, 1, 1, 1, 1, 0, 1,
	                1, -1, 0, 1, 1, 1, 1, 1, 1,
	                1, 1, 0, 1, 1, 1, 1, 1, 0,
	                -1, 1, 0, 1, 1, 1, 1, 0, 0});
		quad.setIndices(new short[]{0, 1, 2, 2, 3, 0});
	    //quad.setIndices(new short[]{1, 0, 2, 3});
	}
	
	/** Restores the previously set viewport if one was specified earlier and the destination buffer is the screen */
//	private static void restoreViewport (FrameBuffer dest) {
//		if (hasViewport && dest == null) {
//			Gdx.gl.glViewport((int)viewport.x, (int)viewport.y, (int)viewport.width, (int)viewport.height);
//		}
//	}
}
