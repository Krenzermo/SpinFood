package model.kitchen;

/**
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
        return text;
    }
}
