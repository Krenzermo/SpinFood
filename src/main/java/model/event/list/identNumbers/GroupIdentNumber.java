package model.event.list.identNumbers;

import model.event.io.InputData;
import model.event.Location;
import model.event.collection.Group;
import model.event.collection.Pair;
import model.event.list.GroupList;
import model.event.list.ParticipantCollectionList;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**A class that holds and calculates the IdentNumbers for a {@link Group}
 *
 * @author Ole Krenzer
 * @author Davide Piacenza
 *
 */
public class GroupIdentNumber extends IdentNumber {
    private double averagePathLength;
    private double totalPathLength;
    private double pathLengthStdDev;
    private final GroupList groupList;

    public GroupIdentNumber(GroupList participantCollection) {
        super(participantCollection);
        this.numSuccessors = participantCollection.getSuccessorPairs().size();
        this.groupList = participantCollection;
        genderDiversity = calcGenderDiversity(participantCollection);
        ageDifference = calcAgeDifference(participantCollection);
        preferenceDeviation = calcPreferenceDeviation(participantCollection);
        totalPathLength = calcTotalPathLength(participantCollection);
        averagePathLength = totalPathLength / participantCollection.getPairList().getPairs().size();
        pathLengthStdDev = calcPathLengthStdDev(participantCollection);
    }

    @Override
    protected double calcGenderDiversity(ParticipantCollectionList participantCollection) {
        GroupList groupList = (GroupList) participantCollection;
        return groupList.getGroups().stream()
                .mapToDouble(this::calculateGroupGenderDeviation)
                .average().orElse(0.0);
    }

    @Override
    protected double calcAgeDifference(ParticipantCollectionList participantCollection) {
        GroupList groupList = (GroupList) participantCollection;
        return groupList.getGroups().stream()
                .mapToDouble(this::calculateGroupAgeDifference)
                .average().orElse(0.0);
    }

    @Override
    protected double calcPreferenceDeviation(ParticipantCollectionList participantCollection) {
        GroupList groupList = (GroupList) participantCollection;
        return groupList.getGroups().stream()
                .mapToDouble(this::calculateGroupPreferenceDeviation)
                .average().orElse(0.0);
    }

    private double calcTotalPathLength(ParticipantCollectionList participantCollection) {
        List<Pair> pairs = getAllPairs(participantCollection);
        List<Pair> pairsNoDuplicate = new ArrayList<>();
        for (Pair pair : pairs ){
            if (!pairsNoDuplicate.contains(pair)){
                pairsNoDuplicate.add(pair);
            }

        }
        return pairsNoDuplicate.stream()
                .mapToDouble(this::calculateTotalDistanceForPair)
                .sum();
    }
    private double calculateTotalDistanceForPair(Pair pair) {
        double totalDistance = 0.0;

        Group starterGroup = getGroupById(pair.getStarterNumber());
        Group mainGroup = getGroupById(pair.getMainNumber());
        Group dessertGroup = getGroupById(pair.getDessertNumber());
        Location partyLocation = InputData.getInstance().getEventLocation();

        if (starterGroup != null && mainGroup != null) {
            totalDistance += starterGroup.getKitchen().location().getDistance(mainGroup.getKitchen().location());
        }

        if (mainGroup != null && dessertGroup != null) {
            totalDistance += mainGroup.getKitchen().location().getDistance(dessertGroup.getKitchen().location());
        }

        if (dessertGroup != null) {
            totalDistance += dessertGroup.getKitchen().location().getDistance(partyLocation);
        }

        return totalDistance;
    }

    private double calcPathLengthStdDev(ParticipantCollectionList participantCollection) {
        List<Double> pathLengths = getAllPairs(participantCollection).stream()
                .map(this::calculateTotalDistanceForPair)
                .collect(Collectors.toList());

        double mean = pathLengths.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        double variance = pathLengths.stream().mapToDouble(length -> Math.pow(length - mean, 2)).average().orElse(0.0);
        return Math.sqrt(variance);
    }

    private Group getGroupById(int groupId) {
        return groupList.getGroups().stream()
                .filter(group -> group.getId() == groupId)
                .findFirst()
                .orElse(null);
    }

    private List<Pair> getAllPairs(ParticipantCollectionList participantCollection) {
        GroupList groupList = (GroupList) participantCollection;
        return groupList.getGroups().stream()
                .flatMap(group -> List.of(group.getPairs()).stream())
                .collect(Collectors.toList());
    }

    private int getTotalPairs(ParticipantCollectionList participantCollection) {
        return getAllPairs(participantCollection).size();
    }

    private double calculateGroupGenderDeviation(Group group) {
        Pair[] pairs = group.getPairs();
        double pair1Deviation = pairs[0].getGenderDeviation();
        double pair2Deviation = pairs[1].getGenderDeviation();
        double pair3Deviation = pairs[2].getGenderDeviation();
        double groupDeviation = Math.abs((pair1Deviation + pair2Deviation + pair3Deviation) / 3);
        return groupDeviation;
    }

    private double calculateGroupAgeDifference(Group group) {
        Pair[] pairs = group.getPairs();
        double pair1AverageAge = pairs[0].getAverageAgeRange();
        double pair2AverageAge = pairs[1].getAverageAgeRange();
        double pair3AverageAge = pairs[2].getAverageAgeRange();
        double groupAgeAverage = Math.abs((pair1AverageAge + pair2AverageAge + pair3AverageAge) / 3);
        double groupAgeDifference = (Math.abs(pair1AverageAge-groupAgeAverage) + Math.abs(pair2AverageAge-groupAgeAverage) +Math.abs(pair3AverageAge-groupAgeAverage))/ 3;
        return groupAgeDifference;
    }

    private double calculateGroupPreferenceDeviation(Group group) {
        Pair[] pairs = group.getPairs();
        double pair1Deviation = pairs[0].getFoodType().deviation.apply(pairs[1].getFoodType());
        double pair2Deviation = pairs[1].getFoodType().deviation.apply(pairs[2].getFoodType());
        double pair3Deviation = pairs[0].getFoodType().deviation.apply(pairs[2].getFoodType());
        double groupDeviation = Math.abs((pair1Deviation + pair2Deviation + pair3Deviation) / 3);
        return groupDeviation;
    }

    public double getAveragePathLength() {
        return averagePathLength;
    }

    public double getTotalPathLength() {
        return totalPathLength;
    }

    public double getPathLengthStdDev() {
        return pathLengthStdDev;
    }


    @Override
    public String toString() {
        return "Anzahl der Gruppen: " + numElems +
                ", Anzahl der Nachrückenden Pärchen: " + numSuccessors +
                ", Geschlechterdiversität: " + genderDiversity +
                ", Altersunterschied: " + ageDifference +
                ", Vorliebenabweichung: " + preferenceDeviation +
                ", \nDurchschnittliche Pfadlänge: " + averagePathLength +
                ", Gesamte Pfadlänge: " + totalPathLength +
                ", Standardabweichung der Pfadlängen: " + pathLengthStdDev;
    }
}
