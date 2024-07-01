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
import java.util.Arrays;
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
    private Participant participant7;
    private Participant participant8;
    private Participant participant9;
    private Participant participant10;
    private Participant participant11;
    private Participant participant12;
    private Participant participant13;
    private Participant participant14;
    private Participant participant15;
    private Participant participant16;
    private Participant participant17;
    private Participant participant18;
    private Pair pair1;
    private Pair pair2;
    private Pair pair3;
    private Pair pair4;
    private Pair pair5;
    private Pair pair6;
    private Pair pair7;
    private Pair pair8;
    private Pair pair9;

    @BeforeEach
    void setUp() {
        // Initialize participants and pairs
        participant1 = new Participant("1", new Name("Alice", "Smith"), FoodType.VEGGIE, (byte) 25, Gender.FEMALE, KitchenAvailability.YES, 1, 1.002, 0.0);
        participant2 = new Participant("2", new Name("Bob", "Brown"), FoodType.VEGGIE, (byte) 30, Gender.MALE, KitchenAvailability.NO, 1, 0.0, 0.0);
        participant3 = new Participant("3", new Name("Charlie", "Davis"), FoodType.VEGGIE, (byte) 35, Gender.MALE, KitchenAvailability.YES, 1, 1.003, 0.0);
        participant4 = new Participant("4", new Name("Diana", "Clark"), FoodType.VEGGIE, (byte) 28, Gender.FEMALE, KitchenAvailability.NO, 1, 0.0, 0.0);
        participant5 = new Participant("5", new Name("Eve", "Martinez"), FoodType.VEGGIE, (byte) 27, Gender.FEMALE, KitchenAvailability.YES, 1, 1.004, 0.0);
        participant6 = new Participant("6", new Name("Frank", "Miller"), FoodType.VEGGIE, (byte) 32, Gender.MALE, KitchenAvailability.NO, 1, 0.0, 0.0);
        participant7 = new Participant("7", new Name("Grace", "Wilson"), FoodType.VEGGIE, (byte) 29, Gender.FEMALE, KitchenAvailability.YES, 1, 1.005, 0.0);
        participant8 = new Participant("8", new Name("Hank", "Lee"), FoodType.VEGGIE, (byte) 31, Gender.MALE, KitchenAvailability.NO, 1, 0.0, 0.0);
        participant9 = new Participant("9", new Name("Ivy", "Walker"), FoodType.VEGGIE, (byte) 24, Gender.FEMALE, KitchenAvailability.YES, 1, 1.006, 0.0);
        participant10 = new Participant("10", new Name("Jack", "Hall"), FoodType.VEGGIE, (byte) 33, Gender.MALE, KitchenAvailability.NO, 1, 0.0, 0.0);
        participant11 = new Participant("11", new Name("Kathy", "Young"), FoodType.VEGGIE, (byte) 26, Gender.FEMALE, KitchenAvailability.YES, 1, 1.007, 0.0);
        participant12 = new Participant("12", new Name("Leo", "King"), FoodType.VEGGIE, (byte) 34, Gender.MALE, KitchenAvailability.NO, 1, 3.0, 0.0);
        participant13 = new Participant("13", new Name("Mona", "Scott"), FoodType.VEGAN, (byte) 25, Gender.FEMALE, KitchenAvailability.YES, 1, 1.008, 0.0);
        participant14 = new Participant("14", new Name("Nate", "Adams"), FoodType.VEGGIE, (byte) 30, Gender.MALE, KitchenAvailability.NO, 1, 0.0, 0.0);
        participant15 = new Participant("15", new Name("Olivia", "Baker"), FoodType.VEGGIE, (byte) 28, Gender.FEMALE, KitchenAvailability.YES, 1, 1.009, 0.0);
        participant16 = new Participant("16", new Name("Paul", "Campbell"), FoodType.VEGGIE, (byte) 27, Gender.MALE, KitchenAvailability.NO, 1, 0.0, 0.0);
        participant17 = new Participant("17", new Name("Quinn", "Mitchell"), FoodType.VEGGIE, (byte) 32, Gender.FEMALE, KitchenAvailability.YES, 1, 1.001, 0.0);
        participant18 = new Participant("18", new Name("Ryan", "Perez"), FoodType.VEGAN, (byte) 29, Gender.MALE, KitchenAvailability.NO, 1, 0.0, 0.0);

        pair1 = new Pair(participant1, participant2);
        pair2 = new Pair(participant3, participant4);
        pair3 = new Pair(participant5, participant6);
        pair4 = new Pair(participant7, participant8);
        pair5 = new Pair(participant9, participant10);
        pair6= new Pair(participant11, participant12);
        pair7 = new Pair(participant13, participant14);
        pair8 = new Pair(participant15, participant16);
        pair9 = new Pair(participant17, participant18);

        InputData inputData = InputData.getInstance();
        PairingWeights pairingWeights = new PairingWeights(1, 1, 1);
        GroupWeights groupWeights = new GroupWeights(1, 1, 1, 1);

        pairList = new PairList(inputData, pairingWeights);
        pairList.add(pair1);
        pairList.add(pair2);
        pairList.add(pair3);
        pairList.add(pair4);
        pairList.add(pair5);
        pairList.add(pair6);
        pairList.add(pair7);
        pairList.add(pair8);
        pairList.add(pair9);

        //group1 = new Group(pair1, pair2, pair3, Course.MAIN, new Kitchen(new Location(0.0, 0.0), 1));

        //List<Group> groups = new ArrayList<>();
        //groups.add(group1);

        groupList = new GroupList(pairList, groupWeights);
        //groupList.addAll(groups);

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
        assertTrue(pairList.contains(pair1));
        assertTrue(groupList.stream()
                .flatMap(group -> Arrays.stream(group.getPairs()))
                .anyMatch(p -> p.equals(pair1)));

        cancellationHandler.handleCancellation(cancelledParticipants, pairingWeights, groupWeights);

        // Verify that the pair was removed and the groups were updated accordingly
        assertFalse(pairList.contains(pair1)); // Ensure the pair was removed
        assertFalse(pairList.getPairs().stream().anyMatch(p -> p.equals(pair1)));
        assertFalse(groupList.stream()
                .flatMap(group -> Arrays.stream(group.getPairs()))
                .anyMatch(p -> p.equals(pair1)));
        assertFalse(groupList.stream()
                .flatMap(group -> Arrays.stream(group.getPairs()))
                .anyMatch(p -> p.equals(pair8)));// TODO Ensure the group was updated  --> wieso schlägt das fehl
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

        assertTrue(groupList.stream()
                .flatMap(group -> Arrays.stream(group.getPairs()))
                .anyMatch(p -> p.equals(pair1)));
        assertTrue(groupList.stream()
                .flatMap(group -> Arrays.stream(group.getPairs()))
                .anyMatch(p -> p.equals(pair8)));

        cancellationHandler.handleCancellation(cancelledParticipants, pairingWeights, groupWeights);

        cancellationHandler.updateGroups(pairingWeights, groupWeights);

        // Verify that the groups list was updated accordingly
        assertFalse(groupList.stream()
                .flatMap(group -> Arrays.stream(group.getPairs()))
                .anyMatch(p -> p.equals(pair1)));
        assertFalse(groupList.stream()
                .flatMap(group -> Arrays.stream(group.getPairs()))
                .anyMatch(p -> p.equals(pair2)));// Ensure the group was removed TODO
    }
}
