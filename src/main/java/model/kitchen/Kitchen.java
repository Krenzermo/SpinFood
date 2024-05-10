package model.kitchen;

import model.InputData;
import model.Location;

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
