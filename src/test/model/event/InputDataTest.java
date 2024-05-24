package model.event;

import static org.junit.jupiter.api.Assertions.*;

import model.event.collection.Pair;
import model.person.Participant;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

/**
 * @author Davide Piacenza
 * @author Daniel Hinkelmann
 */
public class InputDataTest {
    private static InputData inputData;

    @BeforeAll
    public static void setUp() {
        inputData = InputData.getInstance();
    }

    @Test
    public void testEventLocationLoading() {
        Location expectedLocation = new Location(50.5909317660173, 8.6746166676233); // Expected values based on test data
        assertEquals(expectedLocation, InputData.getInstance().getEventLocation());
    }

    @Test
    public void testParticipantsLoading() {
        ArrayList<Participant> participants = inputData.getParticipantInputData();
        assertFalse(participants.isEmpty());
        assertEquals("Person1", participants.get(0).getName().firstName());
    }

    @Test
    public void testPairsLoading() {
        ArrayList<Pair> pairs = inputData.getPairInputData();
        assertFalse(pairs.isEmpty());
        assertTrue(pairs.get(0).signedUpTogether);
    }

    @Test
    public void testGetMethods() {
        assertNotNull(inputData.getEventLocationDataFilePath());
        assertNotNull(inputData.getParticipantDataFilePath());
        assertNotNull(inputData.getParticipantInputData());
        assertNotNull(inputData.getPairInputData());
    }

    @Test
    public void testCorrectPairAssignment() {
        ArrayList<Pair> pairs = inputData.getPairInputData();
        for (Pair pair : pairs) {
            assertTrue(pair.signedUpTogether);
            assertNotEquals(pair.getParticipants().get(0).getId(), pair.getParticipants().get(1).getId());
        }
    }

    @Test
    public void testDataValueValidation() {
        ArrayList<Participant> participants = inputData.getParticipantInputData();
        for (Participant participant : participants) {
            assertNotNull(participant.getGender());
            assertNotNull(participant.getFoodType());
            assertTrue(participant.getAge().value > 0);  // Assuming age should be positive
        }
    }
}