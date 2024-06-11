package model.person;

/**
 * The gender of participant.
 *
 * @author Davide Piacenza
 * @author Finn Brecher
 */
public enum Gender {
    MALE,
    FEMALE,
    OTHER;

    @Override
    public String toString() {
        return "Gender: " + this.name();
    }
}
