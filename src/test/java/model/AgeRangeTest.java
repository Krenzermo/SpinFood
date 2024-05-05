package model;

import model.person.AgeRange;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for AgeRange Enum.
 */
public class AgeRangeTest {

    /**
     * Test getAgeRange method with all boundary ages and some random ages.
     */
    @Test
    public void testGetAgeRange() {
        // Test each boundary condition
        assertEquals(AgeRange.ZERO, AgeRange.getAgeRange(0));
        assertEquals(AgeRange.ZERO, AgeRange.getAgeRange(17));
        assertEquals(AgeRange.ONE, AgeRange.getAgeRange(18));
        assertEquals(AgeRange.ONE, AgeRange.getAgeRange(23));
        assertEquals(AgeRange.TWO, AgeRange.getAgeRange(24));
        assertEquals(AgeRange.TWO, AgeRange.getAgeRange(27));
        assertEquals(AgeRange.THREE, AgeRange.getAgeRange(28));
        assertEquals(AgeRange.THREE, AgeRange.getAgeRange(30));
        assertEquals(AgeRange.FOUR, AgeRange.getAgeRange(31));
        assertEquals(AgeRange.FOUR, AgeRange.getAgeRange(35));
        assertEquals(AgeRange.FIVE, AgeRange.getAgeRange(36));
        assertEquals(AgeRange.FIVE, AgeRange.getAgeRange(41));
        assertEquals(AgeRange.SIX, AgeRange.getAgeRange(42));
        assertEquals(AgeRange.SIX, AgeRange.getAgeRange(46));
        assertEquals(AgeRange.SEVEN, AgeRange.getAgeRange(47));
        assertEquals(AgeRange.SEVEN, AgeRange.getAgeRange(56));
        assertEquals(AgeRange.EIGHT, AgeRange.getAgeRange(57));

        // Testing beyond the last specified age
        assertEquals(AgeRange.EIGHT, AgeRange.getAgeRange(60));
    }

    /**
     * Test getAgeDifference method for various age ranges.
     */
    @Test
    public void testGetAgeDifference() {
        assertEquals(0, AgeRange.ONE.getAgeDifference(AgeRange.ONE));
        assertEquals(1, AgeRange.ONE.getAgeDifference(AgeRange.TWO));
        assertEquals(7, AgeRange.ONE.getAgeDifference(AgeRange.EIGHT));
        assertEquals(5, AgeRange.THREE.getAgeDifference(AgeRange.EIGHT));
        assertEquals(2, AgeRange.FIVE.getAgeDifference(AgeRange.SEVEN));
    }
}
