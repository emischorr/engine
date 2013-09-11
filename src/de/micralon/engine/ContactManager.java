package de.micralon.engine;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

public class ContactManager implements ContactListener {
	
	//temp vars
	Object userDataA;
	Object userDataB;
	GameObject<?> a;
	GameObject<?> b;
	
	public ContactManager(GameWorld world) {
		world.box2dWorld.setContactListener(this);
	}
	
	@Override
	public void beginContact(Contact contact) {
		userDataA = contact.getFixtureA().getBody().getUserData();
		userDataB = contact.getFixtureB().getBody().getUserData();
		
		if (userDataA instanceof Trigger) {
			((Trigger)userDataA).triggerIn((GameObject<?>)userDataB);
		} else if (userDataB instanceof Trigger) {
			((Trigger)userDataB).triggerIn((GameObject<?>)userDataA);
		} else {		
			if (userDataA != null && userDataB != null) {
				((GameObject<?>)userDataA).contactWith((GameObject<?>)userDataB, contact);
				((GameObject<?>)userDataB).contactWith((GameObject<?>)userDataA, contact);
			}
		}
	}

	@Override
	public void endContact(Contact contact) {
		userDataA = null;
		userDataB = null;
		if (contact.getFixtureA() != null) {
			userDataA = contact.getFixtureA().getBody().getUserData();
		}
		if (contact.getFixtureB() != null) {
			userDataB = contact.getFixtureB().getBody().getUserData();
		}
		
		if (userDataA != null && userDataA instanceof Trigger) {
			((Trigger)userDataA).triggerOut((GameObject<?>)userDataB);
		} else if (userDataB != null && userDataB instanceof Trigger) {
			((Trigger)userDataB).triggerOut((GameObject<?>)userDataA);
		}
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		if(impulse.getNormalImpulses()[0] > 0.01)
        {
            /* If body A was hit */
            if(contact.getFixtureA() != null)
            {
                if (GameObject.class.isInstance(contact.getFixtureA().getBody().getUserData())) {
                	a = (GameObject<?>) contact.getFixtureA().getBody().getUserData();
                    evalDamage(a, impulse);

                }
            }

            /* If body B was hit */
            if(contact.getFixtureB() != null)
            {
                if (GameObject.class.isInstance(contact.getFixtureB().getBody().getUserData())) {
                	b = (GameObject<?>) contact.getFixtureB().getBody().getUserData();
                    evalDamage(b, impulse);
                }
            }
        }
	}
	
	/**
     * Evaluate damage and score, and check if actor needs to be destroyed
     * @param object Current object
     * @param impulse Impulse
     */
    protected void evalDamage(GameObject<?> object, ContactImpulse impulse) {
        if (Destructible.class.isInstance(object)) {
            /* Hit and check final health status */
            if (((Destructible)object).getDatamageModel() == null)
                return;
            ((Destructible)object).getDatamageModel().hit(impulse.getNormalImpulses()[0]);
            if (((Destructible)object).getDatamageModel().getHealth() < 0.01f) {

                if (!((Destructible)object).isDestroyed()) {
                	// TODO: give points/XP/whatever...
                	
                    /* Destroy actor */
                    ((Destructible)object).setDestroyed();
                }
            }
        }
    }
}
