package de.micralon.engine.utils;

import java.util.Random;

public class MathUtils {
	private static Random random = new Random();
	
	public static boolean chance(int possability) {
		return random.nextInt(100)+1 <= possability;
	}
	
	public static void resetRandom() {
		random = new Random();
	}
}
