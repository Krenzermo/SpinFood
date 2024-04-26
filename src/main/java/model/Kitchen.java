package model;

public record Kitchen(double longitude, double latitude, int story) {
    @Override
    public String toString() {
        return "Longitude: " + longitude + ", Latitude: " + latitude + ", Story: " + story;
    }
}
