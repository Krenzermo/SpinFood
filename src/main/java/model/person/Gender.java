package model.person;

/**
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
