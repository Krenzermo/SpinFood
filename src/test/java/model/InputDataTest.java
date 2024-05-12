package model;

import static org.junit.Assert.*;

import model.person.Participant;
import org.junit.*;

import java.util.ArrayList;

public class InputDataTest {
    private InputData inputData;

    @Before
    public void setUp() {
        // Assuming you have files setup correctly for testing purposes
        inputData = InputData.getInstance("src/test/java/testData/teilnehmerlisteTest.csv", "src/test/java/testData/partylocationTest.csv");
    }

    @Test
    public void testEventLocationLoading() {
        Location expectedLocation = new Location(50.5909317660173, 8.6746166676233); // Expected values based on test data
        assertEquals(expectedLocation, InputData.getEventLocation());
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

    @Test(expected = RuntimeException.class)
    public void testInvalidFilePath() {
        new InputData("nonexistent.txt", "nonexistentLocation.txt");
    }

    @Test(expected = RuntimeException.class)
    public void testMalformedParticipantData() {
        new InputData("src/test/resources/testData/malformedParticipants.csv", "src/test/resources/testData/partylocationTest.csv");
    }

    @Test
    public void testGetMethods() {
        assertNotNull(inputData.getEventLocationDataFilePath());
        assertNotNull(inputData.getParticipantDataFilePath());
        assertNotNull(inputData.getParticipantInputData());
        assertNotNull(inputData.getPairInputData());
    }

    @Test(expected = RuntimeException.class)
    public void testEmptyParticipantsFile() {
        new InputData("src/test/resources/testData/emptyParticipants.csv", "src/test/java/testData/partylocationTest.csv");
    }

    @Test(expected = RuntimeException.class)
    public void testEmptyLocationFile() {
        new InputData("src/test/java/testData/teilnehmerlisteTest.csv", "src/test/resources/testData/emptyLocation.csv");
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




    @After
    public void tearDown() {
        // Clean up or reset operations if necessary
    }
}
