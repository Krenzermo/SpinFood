package model.event.io;

import static org.junit.jupiter.api.Assertions.*;

import model.event.Location;
import model.event.collection.Pair;
import model.kitchen.Kitchen;
import model.kitchen.KitchenAvailability;
import model.person.Participant;
import org.junit.jupiter.api.Assertions;
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
        Location expectedLocation = new Location(8.6746166676233,50.5909317660173); // Expected values based on test data
        Assertions.assertEquals(expectedLocation, InputData.getInstance().getEventLocation());
    }

    /**
     * Tests if participants are correctly loaded from the data file.
     */
    @Test
    public void testParticipantsLoading() {
        ArrayList<Participant> participants = inputData.getParticipantInputData();
        Assertions.assertFalse(participants.isEmpty());
        Assertions.assertEquals("Person1", participants.get(0).getName().firstName());
    }

    /**
     * Tests if pairs are correctly loaded from the data file.
     */
    @Test
    public void testPairsLoading() {
        ArrayList<Pair> pairs = inputData.getPairInputData();
        Assertions.assertFalse(pairs.isEmpty());
        Assertions.assertTrue(pairs.get(0).signedUpTogether);
    }

    /**
     * Tests if getter methods return non-null values.
     */
    @Test
    public void testGetMethods() {
        Assertions.assertNotNull(inputData.getEventLocationDataFilePath());
        Assertions.assertNotNull(inputData.getParticipantDataFilePath());
        Assertions.assertNotNull(inputData.getParticipantInputData());
        Assertions.assertNotNull(inputData.getPairInputData());
    }

    /**
     * Tests if pairs are correctly assigned and their participants are distinct.
     */
    @Test
    public void testCorrectPairAssignment() {
        ArrayList<Pair> pairs = inputData.getPairInputData();
        for (Pair pair : pairs) {
            Assertions.assertTrue(pair.signedUpTogether);
            Assertions.assertNotEquals(pair.getParticipants().get(0).getId(), pair.getParticipants().get(1).getId());
        }
    }

    /**
     * Tests if participants have valid data values.
     */
    @Test
    public void testDataValueValidation() {
        ArrayList<Participant> participants = inputData.getParticipantInputData();
        for (Participant participant : participants) {
            Assertions.assertNotNull(participant.getGender());
            Assertions.assertNotNull(participant.getFoodType());
            Assertions.assertTrue(participant.getAge().value > 0);  // Assuming age should be positive
        }
    }

    /**
     * Tests if participants with overused kitchens are correctly added to the successor list.
     */
    @Test
    public void testParticipantSuccessorLoading() {
        ArrayList<Participant> successorParticipants = inputData.getParticipantSuccessorList();
        Assertions.assertTrue(successorParticipants.isEmpty());
        for (Participant participant : successorParticipants) { // will not be executed as there are no Successors
            Assertions.assertNotSame(participant.isHasKitchen(), KitchenAvailability.NO);
        }
    }

    /**
     * Tests if pairs with overused kitchens are correctly added to the successor list.
     */
    @Test
    public void testPairSuccessorLoading() {
        ArrayList<Pair> successorPairs = inputData.getPairSuccessorList();
        Assertions.assertFalse(successorPairs.isEmpty());
        for (Pair pair : successorPairs) {
            Assertions.assertNotNull(pair.getKitchen());
            Assertions.assertTrue(pair.signedUpTogether);
        }
    }

    /**
     * Tests if participants and pairs are correctly assigned to the successor list when their kitchens are used more than three times.
     */
    @Test
    public void testCorrectSuccessorAssignment() {
        ArrayList<Participant> successorParticipants = inputData.getParticipantSuccessorList();
        ArrayList<Pair> successorPairs = inputData.getPairSuccessorList();

        Assertions.assertTrue(successorParticipants.isEmpty());
        // Check that all successors have kitchens that are used more than 3 times
        for (Participant participant : successorParticipants) {
            Kitchen kitchen = participant.getKitchen();
            Assertions.assertNotNull(kitchen);
            Assertions.assertTrue(inputData.getKitchenCountMap().get(kitchen) > 3);
        }

        Assertions.assertFalse(successorPairs.isEmpty());
        for (Pair pair : successorPairs) {
            Kitchen kitchen = pair.getKitchen();
            Assertions.assertNotNull(kitchen);
            Assertions.assertTrue(inputData.getKitchenCountMap().get(kitchen) > 3);
        }
    }
}
