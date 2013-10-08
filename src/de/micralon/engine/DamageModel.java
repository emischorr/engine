package de.micralon.engine;

/**
 * Damage model abstract class for game objects/items.
 */
public abstract class DamageModel {
	protected float health;
	
	public float maxHealth() {
		return 1f;
	}
	
	/**
     * Reset health to a default value.
     */
    public void resetHealth() {
        health = maxHealth();
    }

    /**
     * Set object health.
     * @param health Health in scale 0..1
     */
    public void setHealth(float health) {
        this.health = health;
    }
    
    public void addHealth(float health) {
    	if (this.health + health < maxHealth()) {
    		this.health += health;
    	} else {
    		resetHealth();
    	}
    }

    /**
     * Get object health status.
     * @return Object health in scale 0..1
     */
    public float getHealth() {
        return health;
    }
    
    public abstract void calculateDamage(Damage dmg);
    
    /**
     * Collision force
     * @param force value of force in Box2D scale.
     */
    public abstract void hit(float force);

    @Override
    public String toString() {
        return String.format("%.2f", health);
    }
}
