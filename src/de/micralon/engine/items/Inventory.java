package de.micralon.engine.items;

import com.badlogic.gdx.utils.Array;

public class Inventory {
	private Array<Item> items = new Array<Item>();
	private float inventorySize; // we want to be flexible -> inventorySize could be storage room or weight -> as a float
	private float usedSize;
	
	/**
	 * An unlimited inventory
	 */
	public Inventory() {
		this(0); // unlimited inventory
	}
	
	/**
	 * A limited (by size/weight) inventory
	 * @param size Inventory size
	 */
	public Inventory(float size) {
		this.inventorySize = size;
		if (this.inventorySize < 0) this.inventorySize = 0;
	}
	
	/**
	 * Add an item to this inventory. <br />
	 * This considers the available size in this inventory (if size > 0)
	 * @param item
	 * @return true if item has been added. False if not.
	 */
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
	
	/**
	 * Get the max. size available in this inventory
	 * @return size of the inventory
	 */
	public float getMaxSize() {
		return inventorySize;
	}
	
	/**
	 * Get the size used by all the items in this inventory
	 * @return the used size
	 */
	public float getUsedSize() {
		return usedSize;
	}
}
