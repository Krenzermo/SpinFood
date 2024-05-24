package model.event;

import static org.junit.jupiter.api.Assertions.*;

import model.event.collection.Pair;
import model.kitchen.Kitchen;
import model.person.Participant;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

/**
 * Test class for InputData
 *
 * This class contains unit tests for the InputData class, which ensures that participants, pairs, and event locations are
 * correctly loaded from the provided data files, and verifies that successors are appropriately handled when kitchens are overused.
 *
 * @Author: Davide Piacenza
 * @Author: Daniel Hinkelmann
 */
public class InputDataTest {
    private static InputData inputData;

    @BeforeAll
    public static void setUp() {
        inputData = InputData.getInstance();
    }

    /**
     * Tests if the event location is correctly loaded from the data file.
     */
    @Test
    public void testEventLocationLoading() {
        Location expectedLocation = new Location(50.5909317660173, 8.6746166676233); // Expected values based on test data
        assertEquals(expectedLocation, InputData.getInstance().getEventLocation());
    }

    /**
     * Tests if participants are correctly loaded from the data file.
     */
    @Test
    public void testParticipantsLoading() {
        ArrayList<Participant> participants = inputData.getParticipantInputData();
        assertFalse(participants.isEmpty());
        assertEquals("Person1", participants.get(0).getName().firstName());
    }

    /**
     * Tests if pairs are correctly loaded from the data file.
     */
    @Test
    public void testPairsLoading() {
        ArrayList<Pair> pairs = inputData.getPairInputData();
        assertFalse(pairs.isEmpty());
        assertTrue(pairs.get(0).signedUpTogether);
    }

    /**
     * Tests if getter methods return non-null values.
     */
    @Test
    public void testGetMethods() {
        assertNotNull(inputData.getEventLocationDataFilePath());
        assertNotNull(inputData.getParticipantDataFilePath());
        assertNotNull(inputData.getParticipantInputData());
        assertNotNull(inputData.getPairInputData());
    }

    /**
     * Tests if pairs are correctly assigned and their participants are distinct.
     */
    @Test
    public void testCorrectPairAssignment() {
        ArrayList<Pair> pairs = inputData.getPairInputData();
        for (Pair pair : pairs) {
            assertTrue(pair.signedUpTogether);
            assertNotEquals(pair.getParticipants().get(0).getId(), pair.getParticipants().get(1).getId());
        }
    }

    /**
     * Tests if participants have valid data values.
     */
    @Test
    public void testDataValueValidation() {
        ArrayList<Participant> participants = inputData.getParticipantInputData();
        for (Participant participant : participants) {
            assertNotNull(participant.getGender());
            assertNotNull(participant.getFoodType());
            assertTrue(participant.getAge().value > 0);  // Assuming age should be positive
        }
    }

    /**
     * Tests if participants with overused kitchens are correctly added to the successor list.
     */
    @Test
    public void testParticipantSuccessorLoading() {
        ArrayList<Participant> successorParticipants = inputData.getParticipantSuccessorList();
        assertFalse(successorParticipants.isEmpty());
        for (Participant participant : successorParticipants) {
            assertTrue(participant.isHasKitchen() != KitchenAvailability.NO);
        }
    }

    /**
     * Tests if pairs with overused kitchens are correctly added to the successor list.
     */
    @Test
    public void testPairSuccessorLoading() {
        ArrayList<Pair> successorPairs = inputData.getPairSuccessorList();
        assertFalse(successorPairs.isEmpty());
        for (Pair pair : successorPairs) {
            assertNotNull(pair.getKitchen());
            assertTrue(pair.signedUpTogether);
        }
    }

    /**
     * Tests if participants and pairs are correctly assigned to the successor list when their kitchens are used more than three times.
     */
    @Test
    public void testCorrectSuccessorAssignment() {
        ArrayList<Participant> successorParticipants = inputData.getParticipantSuccessorList();
        ArrayList<Pair> successorPairs = inputData.getPairSuccessorList();

        // Check that all successors have kitchens that are used more than 3 times
        for (Participant participant : successorParticipants) {
            Kitchen kitchen = participant.getKitchen();
            assertNotNull(kitchen);
            assertTrue(inputData.getKitchenCountMap().get(kitchen) > 3);
        }

        for (Pair pair : successorPairs) {
            Kitchen kitchen = pair.getKitchen();
            assertNotNull(kitchen);
            assertTrue(inputData.getKitchenCountMap().get(kitchen) > 3);
        }
    }
}
