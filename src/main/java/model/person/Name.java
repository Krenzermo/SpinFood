package model.person;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;

import java.util.Objects;

/**
 * The name of a participant
 *
 * @author Davide Piacenza
 *
 * @param firstName the participants first name
 * @param lastName the participants last name
 */
public record Name(String firstName, String lastName) {

    /**
     * Checks if this Name object is equal to another object.
     *
     * @param obj The object to compare.
     * @return True if the objects are equal, false otherwise.
     */
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Name other = (Name) obj;

        return this.firstName.equals(other.firstName) && this.lastName.equals(other.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName);
    }

    @Override
    public String toString() {
        return firstName + " " + lastName;
    }

    /**
     * Returns a string representation suitable for output purposes.
     *
     * @return The concatenated first name and last name.
     */
    public String asOutputString() {
        return firstName + lastName;
    }

    /**
     * Returns an observable value of the full name.
     *
     * @return Observable value representing the full name as SimpleStringProperty.
     */
    public ObservableValue<String> asObservable() {
        return new SimpleStringProperty(firstName + " " + lastName);
    }
}
