package model.kitchen;

import model.event.InputData;
import model.event.Location;

/**
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
        return "Location: " + location + ", Story: " + story;
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
