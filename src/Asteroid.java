/**
 * Keeps track of asteroid properties and calculates current asteroid orientation.
 */
public final class Asteroid {
	private final int offset;
	private final int timePerAsteroidCycle;
	
	public Asteroid(final int offset, final int timePerAsteroidCycle) {
		this.offset = offset;
		this.timePerAsteroidCycle = timePerAsteroidCycle;
	}
	
	int currentOrientation(final int time) {
		return (time + offset) % timePerAsteroidCycle;
	}
}