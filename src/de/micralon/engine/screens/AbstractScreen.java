package de.micralon.engine.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import de.micralon.engine.EngineGame;
//import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import de.micralon.engine.GameAssets;

public class AbstractScreen<T extends EngineGame> implements Screen {
	protected T game;
	protected InputMultiplexer inputs;
	
    private Table table;
    private Skin skin;
    protected Stage stage;
    
    private ObjectMap<Integer, Actor> menuEntries = new ObjectMap<Integer, Actor>();
    private int selectedEntry;
    
    // temp vars
    private Actor tmpActor;
	
	public AbstractScreen(T game) {
		this.game = game;
		this.stage = new Stage( new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()) );
		inputs = new InputMultiplexer();
	}
	
	// Lazily loaded collaborators

    public BitmapFont getFont() {
        return GameAssets.font;
    }

    public Batch getBatch() {
        return stage.getBatch();
    }
    
    protected Skin getSkin() {
        if( skin == null ) {
            FileHandle skinFile = Gdx.files.internal( EngineGame.assetRootFolder+"skin/uiskin.json" );
            skin = new Skin( skinFile );
        }
        return skin;
    }
    
    protected Table getTable() {
    	return getTable(true);
    }

    protected Table getTable(Boolean fill) {
        if( table == null ) {
            table = new Table( getSkin() );
            table.setFillParent( fill );
            if( EngineGame.isDevelopment() ) {
                table.debug();
            }
            stage.addActor( table );
        }
        return table;
    }
    
    protected void setBackground(Texture bgTexture) {
    	stage.addActor(new Image(bgTexture));
    }
    
    public void add(Actor actor) {
    	stage.addActor(actor);
    }
    
    protected void addMenuEntry(Actor actor) {
    	menuEntries.put(menuEntries.size, actor);
    }
    
    public Actor getSelectedEntry() {
    	return menuEntries.get(selectedEntry);
    }
    
    public boolean isSelectedEntry(Actor entry) {
    	return menuEntries.get(selectedEntry).equals(entry);
    }
    
    protected void selectEntry(Actor actor) {
    	selectedEntry = menuEntries.findKey(actor, true);
    }
    
    public void nextEntry() {
    	if (selectedEntry == menuEntries.size - 1) {
    		selectedEntry = 0;
    	} else {
    		selectedEntry++;
    	}
    }
    
    public void previousEntry() {
    	if (selectedEntry == 0) {
    		selectedEntry = menuEntries.size - 1;
    	} else {
    		selectedEntry--;
    	}
    }

	@Override
	public void render(float delta) {
		if (stage != null) {
	        stage.act( delta );
	        tmpActor = getSelectedEntry();
	        if (tmpActor != null && Gdx.app.getType() != ApplicationType.Android) tmpActor.setColor(0, 1, 1, 1);
	
	        Gdx.gl.glClearColor( 0f, 0f, 0f, 1f );
	        Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT );
	
	        stage.draw();
	        // draw the table debug lines
	        if( EngineGame.isDevelopment() ) Table.drawDebug( stage );
	        
	        if (tmpActor != null && Gdx.app.getType() != ApplicationType.Android) tmpActor.setColor(1, 1, 1, 1);
		}
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, false); // do not center camera
	}

	@Override
	public void show() {
		if (stage != null) {
			inputs.addProcessor( stage );
		}
		Gdx.input.setInputProcessor( inputs );
		selectedEntry = 0; // reset selected entry
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
	}

	@Override
	public void dispose() {
		if (stage != null) {
			stage.dispose();
		}
	}

}
