package de.micralon.engine.pathfinding;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

/**
 * A path determined by some path finding algorithm. A series of steps from
 * the starting location to the target location. This includes a step for the
 * initial location.
 * 
 * @author Enrico Mischorr
 */
public class Path {
	/** The list of steps building up this path */
	private Array<Step> steps = new Array<Step>();
	private int cost;
	
	/**
	 * Create an empty path
	 */
	public Path() {
		
	}

	/**
	 * Get the length of the path, i.e. the number of steps
	 * 
	 * @return The number of steps in this path
	 */
	public int getLength() {
		return steps.size;
	}
	
	/**
	 * Get the step at a given index in the path
	 * 
	 * @param index The index of the step to retrieve. Note this should
	 * be >= 0 and < getLength();
	 * @return The step information, the position on the map.
	 */
	public Step getStep(int index) {
		return (Step) steps.get(index);
	}
	
	public Step getLastStep() {
		return getStep(this.getLength()-1);
	}
	
	/**
	 * Get the x coordinate for the step at the given index
	 * 
	 * @param index The index of the step whose x coordinate should be retrieved
	 * @return The x coordinate at the step
	 */
	public float getX(int index) {
		return getStep(index).getX();
	}

	/**
	 * Get the y coordinate for the step at the given index
	 * 
	 * @param index The index of the step whose y coordinate should be retrieved
	 * @return The y coordinate at the step
	 */
	public float getY(int index) {
		return getStep(index).getY();
	}
	
	/**
	 * Append a step to the path.  
	 * 
	 * @param x The x coordinate of the new step
	 * @param y The y coordinate of the new step
	 */
	public void appendStep(float x, float y) {
		steps.add(new Step(x,y));
	}

	/**
	 * Prepend a step to the path.  
	 * 
	 * @param x The x coordinate of the new step
	 * @param y The y coordinate of the new step
	 */
	public void prependStep(float x, float y) {
		steps.add(new Step(x, y));
	}
	
	public void setCost(int cost) {
		this.cost = cost;
	}
	
	public int getCost() {
		return this.cost;
	}
	
	/**
	 * Check if this path contains the given step
	 * 
	 * @param x The x coordinate of the step to check for
	 * @param y The y coordinate of the step to check for
	 * @return True if the path contains the given step
	 */
	public boolean contains(float x, float y) {
		return steps.contains(new Step(x,y),false);
	}
	
	/**
	 * A single step within the path
	 * 
	 * @author Kevin Glass
	 */
	public class Step {
		private final Vector2 position = new Vector2();
		
		/**
		 * Create a new step
		 * 
		 * @param x The x coordinate of the new step
		 * @param y The y coordinate of the new step
		 */
		public Step(float x, float y) {
			this.position.x = x;
			this.position.y = y;
		}
		
		public Step(Vector2 pos) {
			this(pos.x, pos.y);
		}
		
		public Vector2 getPos() {
			return position;
		}
		
		/**
		 * Get the x coordinate of the new step
		 * 
		 * @return The x coodindate of the new step
		 */
		public float getX() {
			return position.x;
		}

		/**
		 * Get the y coordinate of the new step
		 * 
		 * @return The y coodindate of the new step
		 */
		public float getY() {
			return position.y;
		}
		
		/**
		 * @see Object#hashCode()
		 */
		@Override
		public int hashCode() {
			return position.hashCode();
		}

		/**
		 * @see Object#equals(Object)
		 */
		public boolean equals(Object other) {
			if (other instanceof Step) {
				Step o = (Step) other;
				return position.equals(o.getPos());
			}
			
			return false;
		}
	}
}
