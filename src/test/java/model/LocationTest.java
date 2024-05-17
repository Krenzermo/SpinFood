package model;

import model.event.Location;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Ole Krenzer
 */
public class LocationTest {

    @Test
    public void testGetDistance() {
        Location location1 = new Location(0, 0);
        Location location2 = new Location(3, 4);

        double expectedDistance = 5; // distance between (0,0) and (3,4) is 5 units
        double actualDistance = location1.getDistance(location2);

        assertEquals(expectedDistance, actualDistance, 0);
    }

    @Test
    public void testGetDistance_SameLocation() {
        Location location1 = new Location(3, 4);
        Location location2 = new Location(3, 4);

        double expectedDistance = 0;
        double actualDistance = location1.getDistance(location2);

        assertEquals(expectedDistance, actualDistance, 0);
    }

}
