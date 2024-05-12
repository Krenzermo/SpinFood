package model;

import model.kitchen.Kitchen;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author Ole Krenzer
 * @author Daniel Hinkelmann
 */
public class KitchenTest {
    @Before
    public void setUp() {
        InputData inputData = InputData.getInstance("src/test/java/testData/teilnehmerlisteTest.csv", "src/test/java/testData/partylocationTest.csv");
    }
    @Test
    public void testConstruction() {
        Location location = new Location(-8.668413178974852, 50.574996578974854); // Example location values
        int story = 0;
        Kitchen kitchen = new Kitchen(location, story);
        assertEquals(location, kitchen.location());
        assertEquals(story, kitchen.story());
    }

    @Test
    public void testToString() {
        Location location = new Location(-8.668413178974852, 50.574996578974854); // Example location values
        int story = 0;
        Kitchen kitchen = new Kitchen(location, story);
        assertEquals("Location: " + location + ", Story: " + story,kitchen.toString());
    }

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

    @Test
    public void testEquals_NotAKitchen() {
        Location location1 = new Location(-8.668413178974852, 50.574996578974854); // Example location values
        int story1 = 0;
        Kitchen kitchen1 = new Kitchen(location1, story1);
        Location location2 = new Location(-8.668413178974852, 50.574996578974854); // Example location values
        assertNotEquals(kitchen1, location2);
    }

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
