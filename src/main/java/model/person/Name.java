package model.person;

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

    public String asOutputString() {
        return firstName + lastName;
    }
}
