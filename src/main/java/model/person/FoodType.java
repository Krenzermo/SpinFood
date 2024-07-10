package model.person;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;

import java.util.function.Function;

/**
 * The FoodType of a Participant
 *
 * @author Davide Piacenza
 * @author Daniel Hinkelmann
 * @author Finn Brecher
 */
public enum FoodType {
    MEAT(0, foodType -> foodType.value, "MEAT"),
    NONE(0, foodType -> foodType.value == 0 ? 0 : foodType.value - 2, "ANY"),
    VEGAN(4, foodType -> foodType == FoodType.NONE ? 2 : Math.abs(4 - foodType.value), "VEGAN"),
    VEGGIE(3, foodType -> foodType == FoodType.NONE ? 1 : Math.abs(3 - foodType.value), "VEGGIE");



    private final String otherName;
    private final int value;
    public final Function<FoodType, Integer> deviation;

    /**
     * Constructor for FoodType enum.
     *
     * @param value Numeric value representing the food type.
     * @param function Function to calculate deviation from another food type.
     * @param otherName Alternate name for the food type.
     */
    FoodType(int value, Function<FoodType, Integer> function, String otherName) {
        this.value = value;
        this.deviation = function;
        this.otherName = otherName;
    }

    /**
     * Returns the FoodType enum constant based on the given numeric value.
     *
     * @param value Numeric value representing the food type.
     * @return Corresponding FoodType enum constant.
     */
    public static FoodType herbiFromValue(int value) {
        return value == 4 ? VEGAN : VEGGIE;
    }

    @Override
    public String toString() {
        return "FoodType: " + this.name();
    }

    /**
     * Retrieves the numeric value of the food type.
     *
     * @return Numeric value of the food type.
     */
    public int getValue() {
        return value;
    }

    /**
     * Retrieves the alternate name of the food type.
     *
     * @return Alternate name of the food type.
     */
    public String getOtherName() {
        return otherName;
    }

    /**
     * Returns an observable value of the food type name.
     *
     * @return Observable value of the food type name.
     */
    public ObservableValue<String> asObservable() {
        return new SimpleStringProperty(this.name());
    }
}
