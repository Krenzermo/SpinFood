package model;

/**
 * @author Finn Brecher
 */
public enum Course {
	STARTER,
	MAIN,
	DESSERT;

	@Override
	public String toString() {
		return "Course: " + this.name();
	}
}
