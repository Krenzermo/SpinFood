package model;

public record Kitchen(double longitude, double latitude, int story) implements Comparable<Kitchen>{
    @Override
    public String toString() {
        return "Longitude: " + longitude + ", Latitude: " + latitude + ", Story: " + story;
    }

    @Override
    public int compareTo(Kitchen o) {
        return 0; //TODO Calc distances of both kitchens to the after party. The Kitchen with the shorter distance, is the smaller one.
    }

    @Override
    public boolean equals(Object obj) {
        Kitchen other;
        if (obj instanceof Kitchen) {
            other = (Kitchen) obj;
        } else {
            return false;
        }
        return  this.longitude == other.longitude &&
                this.latitude == other.latitude &&
                this.story == other.story;
    }
}
