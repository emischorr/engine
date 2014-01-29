package de.micralon.engine.items;

public interface Stackable {
	public int getNumber();
	public void add(Stackable item);
	public Stackable remove(int number);
}
