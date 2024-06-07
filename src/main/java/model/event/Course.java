package model.event;

/**
 * @author Finn Brecher
 */
public enum Course {
	STARTER(1),
	MAIN(2),
	DESSERT(3);

	private int value;

	Course(int value) {
		this.value = value;
	}

	public int getAsInt() {
		return value;
	}

	@Override
	public String toString() {
		return "Course: " + this.name();
	}
}
