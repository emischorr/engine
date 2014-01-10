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
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectMap.Entry;

public class Processor implements Disposable{
	private Mesh quad;
	private ObjectMap<String, Object> uniforms = new ObjectMap<String, Object>();
	private FrameBuffer fbo, fbo2, tmp;
	
	// temp vars
	private String cls;
	
	public Processor() {
		fbo = new FrameBuffer(Pixmap.Format.RGBA4444, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
		fbo2 = new FrameBuffer(Pixmap.Format.RGBA4444, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
		createQuad();
	}
	
	public void setTextureFilter(TextureFilter filter) {
		fbo.getColorBufferTexture().setFilter(filter, filter);
	}
	
	public void setUniform(String name, Object value) {
		uniforms.put(name, value);
	}
	
	public void setUniform(String name, Object... values) {
		uniforms.put(name, values);
	}
	
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
	
	public void capture() {
		
	}
	
	public void render() {
		
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
}
