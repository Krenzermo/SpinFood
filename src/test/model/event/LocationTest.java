package model.event;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Location class.
 *
 * Tests the calculation of distances between different Location instances.
 *
 * @author Ole Krenzer
 */
public class LocationTest {

    /**
     * Tests the getDistance method for two different locations with concrete values.
     */
    @Test
    public void testGetDistance() {
        Location location1 = new Location(0, 0);
        Location location2 = new Location(3, 4);

        double expectedDistance = 5; // distance between (0,0) and (3,4) is 5 units
        double actualDistance = location1.getDistance(location2);

        assertEquals(expectedDistance, actualDistance, 0);
    }

    /**
     * Tests the getDistance method for two identical locations.
     */
    @Test
    public void testGetDistance_SameLocation() {
        Location location1 = new Location(3, 4);
        Location location2 = new Location(3, 4);

        double expectedDistance = 0;
        double actualDistance = location1.getDistance(location2);

        assertEquals(expectedDistance, actualDistance, 0);
    }

    /**
     * Tests the getDistance method for two locations with minimal distance.
     */
    @Test
    public void testGetDistance_SmallDistance() {
        Location location1 = new Location(0, 8.675847);
        Location location2 = new Location(0, 8.675848);

        double expectedDistance = 0.000001;
        double actualDistance = location1.getDistance(location2);

        assertEquals(expectedDistance, actualDistance, 0.00000001);
    }

}
