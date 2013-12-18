package de.micralon.engine;

import com.badlogic.gdx.utils.Array;

public class Inventory {
	private Array<Item> items = new Array<Item>();
	private float inventorySize; // we want to be flexible -> inventorySize could be storage room or weight -> as a float
	private float usedSize;
	
	public Inventory() {
		this(0); // unlimited inventory
	}
	
	public Inventory(float size) {
		this.inventorySize = size;
		if (this.inventorySize < 0) this.inventorySize = 0;
	}
	
	public boolean addItem(Item item) {
		if (inventorySize == 0 || item.inventorySize <= inventorySize - usedSize) {
			items.add(item);
			usedSize += item.inventorySize;
			return true;
		} else {
			return false;
		}
	}
	
	public boolean removeItem(Item item) {
		if (items.removeValue(item, true)) {
			usedSize -= item.inventorySize;
			return true;
		} else {
			return false;
		}
	}
	
	public float getMaxSize() {
		return inventorySize;
	}
	
	public float getSize() {
		return usedSize;
	}
}
