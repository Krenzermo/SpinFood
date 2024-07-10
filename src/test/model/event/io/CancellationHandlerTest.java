package model.event.io;

import model.event.collection.Pair;
import model.event.list.GroupList;
import model.event.list.PairList;
import model.event.io.InputData;
import model.event.list.weight.GroupWeights;
import model.event.list.weight.PairingWeights;
import model.person.Participant;
import model.processing.CancellationHandler;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Test class for CancellationHandler.
 * This class contains tests to verify the functionality of the CancellationHandler class.
 */
public class CancellationHandlerTest {

    private CancellationHandler cancellationHandler;
    private PairList pairList;
    private GroupList groupList;
    private InputData inputData;

    /**
     * Sets up the necessary objects before each test.
     * Initializes InputData, PairList, GroupList, and CancellationHandler.
     */
    @Before
    public void setUp() {
        inputData = InputData.getInstanceDebug();
        PairingWeights pairingWeights = new PairingWeights(1, 1, 1);
        pairList = new PairList(pairingWeights);
        GroupWeights groupWeights = new GroupWeights(1, 1, 1, 1);
        groupList = new GroupList(pairList, groupWeights);
        cancellationHandler = new CancellationHandler(pairList, groupList);
    }

    /**
     * Tests the full pair cancellation scenario.
     * Verifies that the pair is removed from the pair list and neither participant is in the successors list.
     */
    @Test
    public void testFullPairCancellation() {
        List<Pair> pairs = pairList.getPairs();
        Pair pair = pairs.get(0);
        List<Participant> cancelledParticipants = pair.getParticipants();

        cancellationHandler.handleCancellation(cancelledParticipants);

        assertFalse(pairList.contains(pair));
        assertFalse(pairList.getSuccessors().contains(pair.getParticipants().get(0)));
        assertFalse(pairList.getSuccessors().contains(pair.getParticipants().get(1)));
    }

    /**
     * Tests the partial pair cancellation scenario.
     * Verifies that the pair is removed from the pair list and the remaining participant is in the successors list.
     */
    @Test
    public void testPartialPairCancellation() {
        List<Pair> pairs = pairList.getPairs();
        Pair pair = pairs.get(0);
        Participant cancelledParticipant = pair.getParticipants().get(0);
        List<Participant> cancelledParticipants = new ArrayList<>();
        cancelledParticipants.add(cancelledParticipant);

        cancellationHandler.handleCancellation(cancelledParticipants);

        assertFalse(pairList.contains(pair));
        assertTrue(pairList.getSuccessors().contains(pair.getOtherParticipant(cancelledParticipant)));
    }

    /**
     * Tests the single participant cancellation scenario.
     * Verifies that the participant is removed from the successors list.
     */
    @Test
    public void testSingleCancellation() {
        Participant singleParticipant = inputData.getParticipantInputData().get(0);
        List<Participant> cancelledParticipants = new ArrayList<>();
        cancelledParticipants.add(singleParticipant);

        cancellationHandler.handleCancellation(cancelledParticipants);

        assertFalse(pairList.getSuccessors().contains(singleParticipant));
    }

    /**
     * Tests the update of groups after a pair cancellation.
     * Verifies that the groups are updated accordingly and the cancelled pair is not in the successor pairs list.
     */
    @Test
    public void testUpdateGroupsAfterCancellation() {
        List<Pair> pairs = pairList.getPairs();
        Pair pair = pairs.get(0);
        List<Participant> cancelledParticipants = pair.getParticipants();

        cancellationHandler.handleCancellation(cancelledParticipants);
        
        assertFalse(groupList.getSuccessorPairs().contains(pair));

    }
}
