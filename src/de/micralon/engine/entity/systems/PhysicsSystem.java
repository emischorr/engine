package de.micralon.engine.entity.systems;

import de.micralon.engine.entity.components.PhysicsComponent;
import de.micralon.engine.entity.components.TransformComponent;
import ashley.core.Entity;
import ashley.core.Family;
import ashley.systems.IteratingSystem;

public class PhysicsSystem extends IteratingSystem {

	@SuppressWarnings("unchecked")
	public PhysicsSystem() {
		super(Family.getFamilyFor(PhysicsComponent.class, TransformComponent.class));
	}

	@Override
	public void processEntity(Entity entity, float deltaTime) {
		// TODO Auto-generated method stub
		
	}
}
