package model.person;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;

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

    public ObservableValue<String> asObservable() {
        return new SimpleStringProperty(this.name());
    }
}
