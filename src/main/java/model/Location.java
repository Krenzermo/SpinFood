package model;

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
}
