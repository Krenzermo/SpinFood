package model.person;

/**
 * @author Davide Piacenza
 * @author Daniel Hinkelmann
 * @author Finn Brecher
 */
public enum FoodType {
    VEGAN,
    VEGGIE,
    MEAT,
    NONE;

    @Override
    public String toString() {
        return "FoodType: " + this.name();
    }
}
