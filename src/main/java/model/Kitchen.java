package model;

public record Kitchen(Location location, int story) {
    public Kitchen(double kitchenLongitude, double kitchenLatitude, int story) {
        this(new Location(kitchenLongitude, kitchenLatitude), story);
    }

    @Override
    public String toString() {
        return "Longitude: " + location.longitude() + ", Latitude: " + location.latitude() + ", Story: " + story;
    }
}
