package model.kitchen;

import model.event.io.InputData;
import model.event.Location;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Kitchen class.
 *
 * Tests the functionality of the Kitchen class, including construction, toString, equals, and compareTo methods.
 *
 * Authors:
 * - Ole Krenzer
 * - Daniel Hinkelmann
 */
public class KitchenTest {

    /**
     * Sets up the test environment before all tests.
     */
    @BeforeAll
    public static void setUp() {
        InputData inputData = InputData.getInstanceDebug();
    }

    /**
     * Tests the construction of a Kitchen object.
     */
    @Test
    public void testConstruction() {
        Location location = new Location(-8.668413178974852, 50.574996578974854); // Example location values
        int story = 0;
        Kitchen kitchen = new Kitchen(location, story);
        assertEquals(location, kitchen.location());
        assertEquals(story, kitchen.story());
    }

    /**
     * Tests the toString method of a Kitchen object.
     */
    @Test
    public void testToString() {
        Location location = new Location(-8.668413178974852, 50.574996578974854); // Example location values
        int story = 0;
        Kitchen kitchen = new Kitchen(location, story);
        assertEquals(location + ", Story: " + story, kitchen.toString());
    }
    /**
     * Tests the equals method for two equal Kitchen objects.
     */
    @Test
    public void testEquals_True() {
        Location location1 = new Location(-8.668413178974852, 50.574996578974854); // Example location values
        int story1 = 0;
        Kitchen kitchen1 = new Kitchen(location1, story1);
        Location location2 = new Location(-8.668413178974852, 50.574996578974854); // Example location values
        int story2 = 0;
        Kitchen kitchen2 = new Kitchen(location2, story2);
        assertEquals(kitchen1, kitchen2);
    }

    /**
     * Tests the equals method for two Kitchen objects with different locations.
     */
    @Test
    public void testEquals_DifferentLocation() {
        Location location1 = new Location(-8.668413178974852, 50.574996578974854); // Example location values
        int story1 = 0;
        Kitchen kitchen1 = new Kitchen(location1, story1);
        Location location2 = new Location(8.668413178974852, 52.574996578974854); // Example location values
        int story2 = 0;
        Kitchen kitchen2 = new Kitchen(location2, story2);
        assertNotEquals(kitchen1, kitchen2);
    }

    /**
     * Tests the equals method for two Kitchen objects with different stories from the same location.
     */
    @Test
    public void testEquals_DifferentStory() {
        Location location1 = new Location(-8.668413178974852, 50.574996578974854); // Example location values
        int story1 = 0;
        Kitchen kitchen1 = new Kitchen(location1, story1);
        Location location2 = new Location(-8.668413178974852, 50.574996578974854); // Example location values
        int story2 = 1;
        Kitchen kitchen2 = new Kitchen(location2, story2);
        assertNotEquals(kitchen1, kitchen2);
    }

    /**
     * Tests the equals method for a Kitchen object and a non-Kitchen object.
     */
    @Test
    public void testEquals_NotAKitchen() {
        Location location1 = new Location(-8.668413178974852, 50.574996578974854); // Example location values
        int story1 = 0;
        Kitchen kitchen1 = new Kitchen(location1, story1);
        Location location2 = new Location(-8.668413178974852, 50.574996578974854); // Example location values
        assertNotEquals(kitchen1, location2);
    }

    /**
     * Tests the compareTo method for two Kitchen objects with the same location and story.
     */
    @Test
    public void testCompareTo_SameLocation() {
        Location location1 = new Location(1, 0); // Example location values
        int story1 = 0;
        Kitchen kitchen1 = new Kitchen(location1, story1);
        Location location2 = new Location(1, 0); // Example location values
        int story2 = 0;
        Kitchen kitchen2 = new Kitchen(location2, story2);
        assertEquals(0, kitchen1.compareTo(kitchen2),0);
    }

    /**
     * Tests the compareTo method when the first Kitchen object is closer to the event location.
     * Asserts that the compareTo method reaches the same result as the getDistance method from the Location class
     */
    @Test
    public void testCompareTo_Kitchen1Closer() {
        Location location1 = new Location(10, 10); // Example location values
        int story1 = 0;
        Kitchen kitchen1 = new Kitchen(location1, story1);
        Location location2 = new Location(0, 0); // Example location values
        int story2 = 0;
        Kitchen kitchen2 = new Kitchen(location2, story2);
        double distance = (location1.getDistance(InputData.getInstance().getEventLocation())
                - location2.getDistance(InputData.getInstance().getEventLocation()));
        assertEquals(distance, kitchen1.compareTo(kitchen2),0);
    }

    /**
     * Tests the compareTo method when the second Kitchen object is closer to the event location.
     * Asserts that the compareTo method reaches the same result as the getDistance method from the Location class
     */
    @Test
    public void testCompareTo_Kitchen2Closer() {
        Location location1 = new Location(0, 0); // Example location values
        int story1 = 0;
        Kitchen kitchen1 = new Kitchen(location1, story1);
        Location location2 = new Location(1, 0); // Example location values
        int story2 = 0;
        Kitchen kitchen2 = new Kitchen(location2, story2);
        double distance = (location1.getDistance(InputData.getInstance().getEventLocation())
                - location2.getDistance(InputData.getInstance().getEventLocation()));
        assertEquals(distance, kitchen1.compareTo(kitchen2),0);
    }

}
