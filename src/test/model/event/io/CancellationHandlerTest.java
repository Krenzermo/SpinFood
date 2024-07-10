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

public class CancellationHandlerTest {

    private CancellationHandler cancellationHandler;
    private PairList pairList;
    private GroupList groupList;
    private InputData inputData;

    @Before
    public void setUp() {
        inputData = InputData.getInstanceDebug();
        PairingWeights pairingWeights = new PairingWeights(1,1,1);
        pairList = new PairList(pairingWeights);
        GroupWeights groupWeights = new GroupWeights(1,1,1,1);
        groupList = new GroupList(pairList, groupWeights);  // Assuming some weights are needed here
        cancellationHandler = new CancellationHandler(pairList, groupList);
    }

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

    @Test
    public void testSingleCancellation() {
        Participant singleParticipant = inputData.getParticipantInputData().get(0);
        List<Participant> cancelledParticipants = new ArrayList<>();
        cancelledParticipants.add(singleParticipant);

        cancellationHandler.handleCancellation(cancelledParticipants);

        assertFalse(pairList.getSuccessors().contains(singleParticipant));
    }

    @Test
    public void testUpdateGroupsAfterCancellation() {
        List<Pair> pairs = pairList.getPairs();
        Pair pair = pairs.get(0);
        List<Participant> cancelledParticipants = pair.getParticipants();

        cancellationHandler.handleCancellation(cancelledParticipants);

        // Add checks to verify that the groups have been updated accordingly
        assertFalse(groupList.getSuccessorPairs().contains(pair));
        // Additional checks can be added here to verify group updates
    }
}
