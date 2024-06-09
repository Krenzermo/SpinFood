package model.event.list.identNumbers;

import model.event.collection.Group;
import model.event.collection.Pair;
import model.event.list.GroupList;
import model.event.list.ParticipantCollectionList;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GroupIdentNumber extends IdentNumber {
    private double averagePathLength;
    private double totalPathLength;
    private double pathLengthStdDev;

    public GroupIdentNumber(GroupList participantCollection) {
        super(participantCollection);
        genderDiversity = calcGenderDiversity(participantCollection);
        ageDifference = calcAgeDifference(participantCollection);
        preferenceDeviation = calcPreferenceDeviation(participantCollection);
        averagePathLength = calcAveragePathLength(participantCollection);
        pathLengthStdDev = calcPathLengthStdDev(participantCollection);
        totalPathLength = calcTotalPathLength(participantCollection);
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

    protected double calcAveragePathLength(ParticipantCollectionList participantCollection) {
        GroupList groupList = (GroupList) participantCollection;
        return groupList.getGroups().stream()
                .flatMapToDouble(group -> {
                    Pair[] pairs = group.getPairs();
                    double starterToMain = pairs[0].getKitchen().location().getDistance(pairs[1].getKitchen().location());
                    double mainToDessert = pairs[1].getKitchen().location().getDistance(pairs[2].getKitchen().location());
                    return Arrays.stream(new double[]{starterToMain, mainToDessert});
                })
                .average().orElse(0.0);
    }

    private double calcTotalPathLength(ParticipantCollectionList participantCollection) {
        GroupList groupList = (GroupList) participantCollection;
        return groupList.getGroups().stream()
                .flatMapToDouble(group -> {
                    Pair[] pairs = group.getPairs();
                    double starterToMain = pairs[0].getKitchen().location().getDistance(pairs[1].getKitchen().location());
                    double mainToDessert = pairs[1].getKitchen().location().getDistance(pairs[2].getKitchen().location());
                    return Arrays.stream(new double[]{starterToMain, mainToDessert});
                })
                .sum();
    }

    private double calcPathLengthStdDev(ParticipantCollectionList participantCollection) {
        GroupList groupList = (GroupList) participantCollection;
        List<Double> pathLengths = groupList.getGroups().stream()
                .flatMap(group -> {
                    Pair[] pairs = group.getPairs();
                    double starterToMain = pairs[0].getKitchen().location().getDistance(pairs[1].getKitchen().location());
                    double mainToDessert = pairs[1].getKitchen().location().getDistance(pairs[2].getKitchen().location());
                    return Arrays.asList(starterToMain, mainToDessert).stream();
                })
                .collect(Collectors.toList());

        double mean = pathLengths.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        double variance = pathLengths.stream().mapToDouble(length -> Math.pow(length - mean, 2)).average().orElse(0.0);
        return Math.sqrt(variance);
    }

    private double calculateGroupGenderDeviation(Group group) {
        Pair[] pairs = group.getPairs();
        double pair1Deviation = pairs[0].getGenderDeviation();
        double pair2Deviation = pairs[1].getGenderDeviation();
        double pair3Deviation = pairs[2].getGenderDeviation();
        double groupDeviation = Math.abs((pair1Deviation + pair2Deviation + pair3Deviation) / 3 - 0.5);
        // - 0,5, damit wir sehen können, um welches Verhältnis die Gruppe von einander abweicht. Beispiel: Pair 1: 0,5 Pair 2: 0,5 und Pair 3: 0,5 ergibt 0.5 - 0.5 = 0 (perfektes Verhältnis)

        return groupDeviation;
    }

    private double calculateGroupAgeDifference(Group group) {
        Pair[] pairs = group.getPairs();
        double pair1AverageAge = pairs[0].getAverageAgeRange();
        double pair2AverageAge = pairs[1].getAverageAgeRange();
        double pair3AverageAge = pairs[2].getAverageAgeRange();
        double groupAgeDifference = Math.abs((pair1AverageAge + pair2AverageAge + pair3AverageAge) / 3);
        return groupAgeDifference;
    }

    private double calculateGroupPreferenceDeviation(Group group) {
        Pair[] pairs = group.getPairs();
        double pair1Deviation = pairs[0].getPreferenceDeviation();
        double pair2Deviation = pairs[1].getPreferenceDeviation();
        double pair3Deviation = pairs[2].getPreferenceDeviation();
        double groupDeviation = Math.abs((pair1Deviation + pair2Deviation + pair3Deviation) / 3);
        return groupDeviation;
    }

    @Override
    public String toString() {
        return "Anzahl der Gruppen: " + numElems +
                ", Anzahl der Nachrückenden Pärchen: " + numSuccessors +
                ", Geschlechterdiversität: " + genderDiversity +
                ", Altersunterschied: " + ageDifference +
                ", Vorliebenabweichung: " + preferenceDeviation +
                ", Durchschnittliche Pfadlänge: " + averagePathLength +
                ", Gesamte Pfadlänge: " + totalPathLength +
                ", Standardabweichung der Pfadlängen: " + pathLengthStdDev;
    }
}
