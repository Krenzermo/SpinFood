package model.kitchen;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;

/**
 * A simple enum representing if a Participant has a Kitchen, has no kitchen, or has a kitchen
 * but only wants to use it if there's no other way.
 *
 * @author Daniel Hinkelmann
 */
public enum KitchenAvailability {
    YES("yes"),
    MAYBE("maybe"),
    NO("no");

    final String text;
    KitchenAvailability(String text) {
        this.text = text;
    }

    /**
     * method to get convert string that indicates kitchenAvailability to the KitchenAvailability enum
     * @param text to be converted to enum
     * @return the KitchenAvailabilty
     */
    public static KitchenAvailability getAvailability(String text) {
        KitchenAvailability[] values = KitchenAvailability.values();
        for (KitchenAvailability availability: values) {
            if (availability.text.equals(text)) {
                return availability;
            }
        }

        return NO;
    }

    @Override
    public String toString() {
        return "KitchenAvailability: " + this.name();
    }

    /**
     *
     * @return enum as SimpleStringProperty
     */
    public ObservableValue<String> asObservable() {
        return new SimpleStringProperty(this.name());
    }
}
