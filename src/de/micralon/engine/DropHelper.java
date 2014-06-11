package de.micralon.engine;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;

public class DropHelper {
	public static final int DROP_BASE_VALUE = 100;
	private static float chance;
	private static Random random = new Random();
	
	public static Array<Dropable> drop() {
		return drop(DROP_BASE_VALUE, drops, false);
	}
	
	public static Array<Dropable> drop(int dropValue) {
		return drop(dropValue, drops, false);
	}
	
	public static Array<Dropable> drop(int dropValue, Array<Drop> drops, boolean multipleDrop) {
		Array<Dropable> items = new Array<Dropable>();
		if (drops != null) {
			for (Drop drop : drops) {
				calcChance(dropValue, drop);
				// do we have luck?
				if ( random.nextInt(100)+1 <= chance ) {
					Gdx.app.log("DropHelper", "Got lucky ("+chance+"%). droping...");
					items.add(drop.item);
					if (!multipleDrop) break;
					dropValue -= chance;
				}
			}
		}
		return items;
	}
	
	protected static void calcChance(float dropValue, Drop drop) {
		// calculate chance to drop this
		chance = dropValue/(DROP_BASE_VALUE*drop.valueLevel);
 		if (chance < drop.minChance) chance = drop.minChance;
		if (chance > drop.maxChance) chance = drop.maxChance; // limit chance
	}
	
	public static Array<Drop> drops = new Array<Drop>();
	
	public static void newRandom() {
		random = new Random();
	}
	
	static {
//		drops.add(new Drop());
//		drops.add(new Drop());
//		...
	}
	
	public static class Drop {
		public Dropable item;
		public float maxChance = 100;
		public float minChance = 0;
		public int valueLevel = 1;
		
		public Drop(Dropable item) {
			this.item = item;
		}
		
		public Drop(Dropable item, float minChance, float maxChance) {
			this.item = item;
			this.maxChance = maxChance;
			this.minChance = minChance;
		}
		
		public Drop(Dropable item, float minChance, float maxChance, int valueLevel) {
			this.item = item;
			this.maxChance = maxChance;
			this.minChance = minChance;
			this.valueLevel = valueLevel;
		}
	}
}
