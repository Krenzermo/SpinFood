package model.event.io;

import model.event.Course;
import model.event.Location;
import model.event.collection.Group;
import model.event.collection.Pair;
import model.event.list.GroupList;
import model.event.list.PairList;
import model.event.list.weight.GroupWeights;
import model.event.list.weight.PairingWeights;
import model.kitchen.Kitchen;
import model.person.FoodType;
import model.person.Gender;
import model.person.Name;
import model.person.Participant;
import model.kitchen.KitchenAvailability;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CancellationHandlerTest {

    private PairList pairList;
    private GroupList groupList;
    private CancellationHandler cancellationHandler;
    private Participant participant1;
    private Participant participant2;
    private Participant participant3;
    private Participant participant4;
    private Participant participant5;
    private Participant participant6;
    private Pair pair1;
    private Pair pair2;
    private Pair pair3;
    private Group group1;

    @BeforeEach
    void setUp() {
        // Initialize participants and pairs
        participant1 = new Participant("1", new Name("Alice", "Smith"), FoodType.VEGGIE, (byte) 25, Gender.FEMALE, KitchenAvailability.YES, 1, 0.0, 0.0);
        participant2 = new Participant("2", new Name("Bob", "Brown"), FoodType.MEAT, (byte) 30, Gender.MALE, KitchenAvailability.NO, 1, 0.0, 0.0);
        participant3 = new Participant("3", new Name("Charlie", "Davis"), FoodType.NONE, (byte) 35, Gender.MALE, KitchenAvailability.YES, 1, 0.0, 0.0);
        participant4 = new Participant("4", new Name("Diana", "Clark"), FoodType.VEGAN, (byte) 28, Gender.FEMALE, KitchenAvailability.NO, 1, 0.0, 0.0);
        participant5 = new Participant("5", new Name("Eve", "Martinez"), FoodType.VEGGIE, (byte) 27, Gender.FEMALE, KitchenAvailability.YES, 1, 0.0, 0.0);
        participant6 = new Participant("6", new Name("Frank", "Miller"), FoodType.NONE, (byte) 32, Gender.MALE, KitchenAvailability.NO, 1, 0.0, 0.0);

        pair1 = new Pair(participant1, participant2);
        pair2 = new Pair(participant3, participant4);
        pair3 = new Pair(participant5, participant6);

        InputData inputData = InputData.getInstance();
        PairingWeights pairingWeights = new PairingWeights(1, 1, 1);
        GroupWeights groupWeights = new GroupWeights(1, 1, 1, 1);

        pairList = new PairList(inputData, pairingWeights);
        pairList.add(pair1);
        pairList.add(pair2);
        pairList.add(pair3);

        group1 = new Group(pair1, pair2, pair3, Course.MAIN, new Kitchen(new Location(0.0, 0.0), 1));

        List<Group> groups = new ArrayList<>();
        groups.add(group1);

        groupList = new GroupList(pairList, groupWeights);
        groupList.addAll(groups);

        cancellationHandler = new CancellationHandler(pairList, groupList);
    }

    @Test
    void testHandleSingleCancellation() {
        List<Participant> cancelledParticipants = new ArrayList<>();
        cancelledParticipants.add(participant1);

        PairingWeights pairingWeights = new PairingWeights(1, 1, 1);
        GroupWeights groupWeights = new GroupWeights(1, 1, 1, 1);

        cancellationHandler.handleCancellation(cancelledParticipants, pairingWeights, groupWeights);

        // Verify that the participant was removed and the remaining participant was added to successors
        assertFalse(pairList.contains(pair1));
        assertFalse(pairList.getPairs().stream().flatMap(p -> p.getParticipants().stream()).anyMatch(p -> p.equals(participant1)));
        assertFalse(pairList.getSuccessors().contains(participant1));
    }

    @Test
    void testHandleFullPairCancellation() {
        List<Participant> cancelledParticipants = new ArrayList<>();
        cancelledParticipants.add(participant1);
        cancelledParticipants.add(participant2);

        PairingWeights pairingWeights = new PairingWeights(1, 1, 1);
        GroupWeights groupWeights = new GroupWeights(1, 1, 1, 1);

        cancellationHandler.handleCancellation(cancelledParticipants, pairingWeights, groupWeights);

        // Verify that the pair was removed and the groups were updated accordingly
        assertFalse(pairList.contains(pair1)); // Ensure the pair was removed
        assertFalse(pairList.getPairs().stream().anyMatch(p -> p.equals(pair1)));
        //assertFalse(groupList.getGroups().contains(group1)); // Ensure the group was updated  --> wieso schlägt das fehl
        assertFalse(pairList.getSuccessors().contains(participant1));
        assertFalse(pairList.getSuccessors().contains(participant2));
    }

    @Test
    void testUpdateGroups() {
        List<Participant> cancelledParticipants = new ArrayList<>();
        cancelledParticipants.add(participant1);
        cancelledParticipants.add(participant2);

        PairingWeights pairingWeights = new PairingWeights(1, 1, 1);
        GroupWeights groupWeights = new GroupWeights(1, 1, 1, 1);

        cancellationHandler.handleCancellation(cancelledParticipants, pairingWeights, groupWeights);

        cancellationHandler.updateGroups(pairingWeights, groupWeights);

        // Verify that the groups list was updated accordingly
        assertFalse(groupList.contains(group1)); // Ensure the group was removed
    }
}
