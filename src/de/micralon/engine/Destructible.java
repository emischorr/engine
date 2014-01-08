package de.micralon.engine;

import com.badlogic.gdx.physics.box2d.Body;

/**
 * Interface for destructible models/items.
 */
public interface Destructible {
	/**
     * Get damage model of this object
     * @return Damage model
     */
    DamageModel getDamageModel();
    
	/**
     * Set body as destroyed
     *
     * This information can be used for sprite rendering.
     * WARNING: this method is probably called from a callback inside world's step function so better do not change physics here
     */
    void setDestroyed();

    /**
     * Get destroyed status
     */
    boolean isDestroyed();

    /**
     * Get Box2D body
     *
     * This is used to remove body from the world
     * @return Body.
     */
    Body getBody();
    
    int getScore();
}
