package de.micralon.engine.entity.factory;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

import de.micralon.engine.Env;
import de.micralon.engine.entity.components.*;
import de.micralon.engine.entity.factory.EntityFactory.ComponentReader;
import de.micralon.engine.physics.PhysicsData;
import ashley.core.Component;

public class ComponentReaders {

    public static class ColorComponentReader implements ComponentReader {
        private Json json = new Json();

        @Override
        public Class<? extends Component> getComponentClass() {
            return ColorComponent.class;
        }

        @Override
        public Component read(JsonValue value) {
            ColorComponent component = new ColorComponent();
            component.color = json.fromJson(Color.class, value.asString());
            return component;
        }
    }

	public static class TransformComponentReader implements ComponentReader {
		@Override
		public Class<? extends Component> getComponentClass() {
			return TransformComponent.class;
		}

		@Override
		public Component read(JsonValue value) {
			TransformComponent component = new TransformComponent();
			component.position.x = value.getFloat("x", 0.0f);
			component.position.y = value.getFloat("y", 0.0f);
			component.scale = value.getFloat("scale", 1.0f);
			component.angle = value.getFloat("angle", 0.0f);
			return component;
		}
	}
	
	public static class TextureComponentReader implements ComponentReader {
		@Override
		public Class<? extends Component> getComponentClass() {
			return TextureComponent.class;
		}

		@Override
		public Component read(JsonValue value) {
			TextureComponent component = new TextureComponent();
			if (value.has("atlas") && value.has("region")) {
				TextureAtlas atlas = Env.game.assets.get(value.getString("atlas"), TextureAtlas.class);
				component.region = atlas.findRegion(value.getString("region"));	
			}
			else if (value.has("texture")) {
				component.region = new TextureRegion(Env.game.assets.get(value.getString("texture"), Texture.class));
			}
			
			return component;
		}
	}

    public static class PhysicsComponentReader implements ComponentReader {
        @Override
        public Class<? extends Component> getComponentClass() {
            return PhysicsComponent.class;
        }

        @Override
        public Component read(JsonValue value) {
            PhysicsComponent component = new PhysicsComponent();
            component.data = Env.game.assets.get(value.getString("name"), PhysicsData.class);
            return component;
        }
    }

}
