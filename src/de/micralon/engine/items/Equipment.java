package de.micralon.engine.items;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

public class Equipment {
	private ObjectMap<String, Item> items = new ObjectMap<String, Item>();
	
	public Equipment(String... slots) {
		for (String slot : slots) {
			items.put(slot, null);
		}
	}
	
	public Array<Item> items() {
		return items.values().toArray();
	}
	
	public Array<String> slots() {
		return items.keys().toArray();
	}
	
	/**
	 * Equip an item to a specified slot
	 * @param slot The slot which should hold the item
	 * @param item The item which should be equipped
	 * @return previous equipped item (or null)
	 */
	public Item equip(String slot, Item item) {
		Item prevItem = items.get(slot);
		items.put(slot, item);
		return prevItem;
	}
	
	public Item clear(String slot) {
		Item prevItem = items.get(slot);
		items.put(slot, null);
		return prevItem;
	}
	
	public void clear() {
		items.clear();
	}
	
	/**
	 * Is the slot already equipped?
	 * @param slot Slot in equipment to check
	 * @return true if an item is in place otherwise false
	 */
	public boolean isEquipped(String slot) {
		return items.get(slot) != null;
	}
	
	public Item get(String slot) {
		return items.get(slot);
	}
}
