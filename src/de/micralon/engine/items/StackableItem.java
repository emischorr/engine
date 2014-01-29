package de.micralon.engine.items;


//TODO: more thinking about this stackable thing required...

public class StackableItem extends Item implements Stackable {
	private int number;
	
	public StackableItem(int number) {
		this.number = number;
	}
	
	@Override
	public int getNumber() {
		return number;
	}
	
	@Override
	public void add(Stackable item) {
		if (item instanceof StackableItem) {
			number = item.getNumber();
		}
	}
	
	@Override
	public StackableItem remove(int number) {
		return new StackableItem(number);
	}
		
}
