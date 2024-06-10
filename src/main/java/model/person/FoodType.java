package model.person;

import java.util.function.Function;

/**
 * @author Davide Piacenza
 * @author Daniel Hinkelmann
 * @author Finn Brecher
 */
public enum FoodType {
    NONE(0, foodType -> 0, "ANY"),
    VEGAN(2, foodType -> foodType == FoodType.NONE ? 1 : Math.abs(2 - foodType.value), "VEGAN"),
    VEGGIE(1, foodType -> foodType == FoodType.NONE ? 1 : Math.abs(1 - foodType.value), "VEGGIE"),

    MEAT(0, foodType -> Math.abs(-foodType.value), "MEAT");

    private final String otherName;
    private final int value;
    public final Function<FoodType, Integer> deviation;

    FoodType(int value, Function<FoodType, Integer> function, String otherName) {
        this.value = value;
        this.deviation = function;
        this.otherName = otherName;
    }

    public static FoodType herbiFromValue(int value) {
        return value == 2 ? VEGAN : VEGGIE;
    }

    @Override
    public String toString() {
        return "FoodType: " + this.name();
    }

    public int getValue() {
        return value;
    }

    public String getOtherName() {
        return otherName;
    }
}
