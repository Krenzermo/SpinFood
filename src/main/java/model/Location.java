package model;

import model.person.Participant;

/**
 * @author Finn Brecher
 *
 * @param latitude the latitude
 * @param longitude the longitude
 */
public record Location(double latitude, double longitude) {
	public double getDistance(Location other) {
		return Math.sqrt(
				Math.pow(Math.abs(this.latitude-other.latitude), 2)
						+ Math.pow(Math.abs(this.longitude-other.longitude), 2));
	}

	@Override
	public String toString() {
		return "@Location{" + "latitude: " + latitude + ", longitude: " + longitude + "}";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		Location other = (Location) obj;
		return this.latitude == other.latitude && this.longitude == other.longitude;
	}
}
