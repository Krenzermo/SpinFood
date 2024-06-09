package model.event.list.identNumbers;

import model.event.collection.Group;
import model.event.collection.Pair;
import model.event.list.GroupList;
import model.event.list.ParticipantCollectionList;
import model.event.InputData;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GroupIdentNumber extends IdentNumber {
    protected double totalPathLength;
    protected double averagePathLength;
    protected double pathLengthStdDev;

    public GroupIdentNumber(GroupList participantCollection) {
        super(participantCollection);
        genderDiversity = calcGenderDiversity(participantCollection);
        ageDifference = calcAgeDifference(participantCollection);
        preferenceDeviation = calcPreferenceDeviation(participantCollection);
        calculatePathLengths(participantCollection);
    }

    @Override
    protected double calcGenderDiversity(ParticipantCollectionList participantCollection) {
        GroupList groupList = (GroupList) participantCollection;
        return groupList.getGroups().stream()
                .flatMapToDouble(group -> Arrays.stream(group.getPairs()).mapToDouble(Pair::getGenderDeviation))
                .average().orElse(0.0);
    }

    @Override
    protected double calcAgeDifference(ParticipantCollectionList participantCollection) {
        GroupList groupList = (GroupList) participantCollection;
        return groupList.getGroups().stream()
                .flatMapToDouble(group -> Arrays.stream(group.getPairs()).mapToDouble(Pair::getAgeDifference))
                .average().orElse(0.0);
    }

    @Override
    protected double calcPreferenceDeviation(ParticipantCollectionList participantCollection) {
        GroupList groupList = (GroupList) participantCollection;
        return groupList.getGroups().stream()
                .flatMapToDouble(group -> Arrays.stream(group.getPairs()).mapToDouble(Pair::getPreferenceDeviation))
                .average().orElse(0.0);
    }

    private void calculatePathLengths(ParticipantCollectionList participantCollection) {
        GroupList groupList = (GroupList) participantCollection;
        List<Double> pathLengths = groupList.getGroups().stream()
                .flatMap(group -> {
                    Pair[] pairs = group.getPairs();
                    return Arrays.stream(pairs)
                            .map(pair -> {
                                double pathLength = 0;
                                for (int i = 0; i < pairs.length - 1; i++) {
                                    pathLength += pairs[i].getKitchen().location().getDistance(pairs[i + 1].getKitchen().location());
                                }
                                pathLength += pairs[pairs.length - 1].getKitchen().location().getDistance(InputData.getInstance().getEventLocation());
                                return pathLength;
                            });
                })
                .collect(Collectors.toList());

        totalPathLength = pathLengths.stream().mapToDouble(Double::doubleValue).sum();
        averagePathLength = pathLengths.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        pathLengthStdDev = Math.sqrt(pathLengths.stream().mapToDouble(pl -> Math.pow(pl - averagePathLength, 2)).average().orElse(0.0));
    }

    @Override
    public String toString() {
        return "Anzahl: " + numElems +
                " Nachrücker: " + numSuccessors +
                " Geschlechterdiversität: " + genderDiversity +
                " Altersunterschied: " + ageDifference +
                " Vorliebenabweichung: " + preferenceDeviation +
                " Gesamte Weglänge: " + totalPathLength +
                " Durchschnittliche Weglänge: " + averagePathLength +
                " Standardabweichung der Weglänge: " + pathLengthStdDev;
    }
}