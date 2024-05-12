package model.person;

/**
 * @author Davide Piacenza
 *
 * @param firstName the participants first name
 * @param lastName the participants last name
 */
public record Name(String firstName, String lastName) {
    @Override
    public String toString() {
        return firstName + " " + lastName;
    }
}
