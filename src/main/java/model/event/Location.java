package model.event;

import model.person.Participant;

/** A simple recode that holds a Location on planet earth in (latitude, longitude) notation.
 *
 * @author Finn Brecher
 * @author Daniel Hinkelmann
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

	/** Creates an output String for this Location. First longitude then latitude
	 *
	 * @return The output String of this Location
	 */
	public String asOutputString() {
		return longitude + ";" + latitude;
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
