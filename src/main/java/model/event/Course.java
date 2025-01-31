package model.event;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;

/** A simple enum representing a Course of the evening.
 *
 * @author Daniel Hinkelmann
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

	/**
	 *
	 * @return the assigned value of the Course enum as an int
	 */

	public int getAsInt() {
		return value;
	}

	@Override
	public String toString() {
		return "Course: " + this.name();
	}

	/**
	 *
	 * @return the Course enum as SimpleStringProperty
	 */
    public SimpleStringProperty asProperty() {
		return new SimpleStringProperty(this.name());
    }
}
