    package model.kitchen;

    import javafx.beans.property.SimpleStringProperty;
    import javafx.beans.value.ObservableValue;
    import model.event.io.InputData;
    import model.event.Location;

    /**
     * A simple record, representing a Kitchen of a Participant.
     *
     * @author Davide Piacenza
     * @author Daniel Hinkelmann
     * @author Finn Brecher
     * @author Ole Krenzer
     *
     * @param location The location of the kitchen
     * @param story the story of the kitchen
     */
    public record Kitchen(Location location, int story){

        @Override
        public String toString() {
            return location + ", Story: " + story;
        }

        /** Return an output String for this Kitchen which basically is the output String of it's
         * {@link Location}
         *
         * @return The output String of the Kitchen
         */
        public String asOutputString() {
            return location.asOutputString();
        }

        public ObservableValue<String> asObservable() {
            return new SimpleStringProperty(location.longitude() + ", " + location.latitude());
        }

        public double compareTo(Kitchen o) {
            return (location.getDistance(InputData.getInstance().getEventLocation())
                    - o.location.getDistance(InputData.getInstance().getEventLocation()));
        }

        @Override
        public boolean equals(Object obj) {
            Kitchen other;
            if (obj instanceof Kitchen) {
                other = (Kitchen) obj;
            } else {
                return false;
            }
            return  this.location.longitude() == other.location.longitude() &&
                    this.location.latitude() == other.location.latitude() &&
                    this.story == other.story;
        }
    }
