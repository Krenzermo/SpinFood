package model;

import static org.junit.Assert.*;

import model.person.Participant;
import org.junit.*;

import java.util.ArrayList;

public class InputDataTest {
    private InputData inputData;

    @Before
    public void setUp() {

        inputData = InputData.getInstance("src/test/java/testData/teilnehmerlisteTest.csv", "src/test/java/testData/partylocationTest.csv");
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
