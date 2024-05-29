package model.person;

import java.util.function.Function;

/**
 * @author Davide Piacenza
 * @author Daniel Hinkelmann
 * @author Finn Brecher
 */
public enum FoodType {
    ANY(0, foodType -> 0),
    VEGAN(2, foodType -> foodType == FoodType.ANY ? 0 : 2 - Math.abs(2 - foodType.value)),
    VEGGIE(1, foodType -> foodType == FoodType.ANY ? 0 : 1 - Math.abs(1 - foodType.value)),

    MEAT(0, foodType -> Math.abs(-foodType.value));


    private int value;
    public final Function<FoodType, Integer> deviation;

    FoodType(int value, Function<FoodType, Integer> function) {
        this.value = value;
        this.deviation = function;
    }

    @Override
    public String toString() {
        return "FoodType: " + this.name();
    }
}
