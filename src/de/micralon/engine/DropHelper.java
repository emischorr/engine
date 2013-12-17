package de.micralon.engine;

import java.util.Random;

import com.badlogic.gdx.utils.Array;

public class DropHelper {
	public static final int DROP_BASE_VALUE = 100;
	
	public static Array<Dropable> drop() {
		return drop(DROP_BASE_VALUE, drops, false);
	}
	
	public static Array<Dropable> drop(int dropValue) {
		return drop(dropValue, drops, false);
	}
	
	public static Array<Dropable> drop(int dropValue, Array<Drop> drops, boolean multipleDrop) {
		Array<Dropable> items = new Array<Dropable>();
		if (drops != null) {
			Random random = new Random();
			for (Drop drop : drops) {
				// calculate chance to drop this
		 		float chance = (dropValue/DROP_BASE_VALUE/drop.valueLevel) * drop.minChance;
				if (chance > drop.maxChance) chance = drop.maxChance; // limit chance
				// do we have luck?
				if (random.nextInt(100)/100f <= chance) {
					items.add(drop.item);
					if (!multipleDrop) break;
				}
			}
		}
		return items;
	}
	
	public static Array<Drop> drops = new Array<Drop>();
	
	static {
//		drops.add(new Drop());
//		drops.add(new Drop());
//		...
	}
	
	public static class Drop {
		public Dropable item;
		public float maxChance = 100;
		public float minChance = 1;
		public int valueLevel = 1;
		
		public Drop(Dropable item) {
			this.item = item;
		}
		
		public Drop(Dropable item, float maxChance, float minChance) {
			this.item = item;
			this.maxChance = maxChance;
			this.minChance = minChance;
		}
		
		public Drop(Dropable item, float maxChance, float minChance, int valueLevel) {
			this.item = item;
			this.maxChance = maxChance;
			this.minChance = minChance;
			this.valueLevel = valueLevel;
		}
	}
}
