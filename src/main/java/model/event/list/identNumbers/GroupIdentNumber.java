package model.event.list.identNumbers;

import model.event.collection.Group;
import model.event.collection.Pair;
import model.event.list.GroupList;
import model.event.list.ParticipantCollectionList;

import java.util.Arrays;

public class GroupIdentNumber extends IdentNumber {
    private double averagePathLength;

    public GroupIdentNumber(GroupList participantCollection) {
        super(participantCollection);
        genderDiversity = calcGenderDiversity(participantCollection);
        ageDifference = calcAgeDifference(participantCollection);
        preferenceDeviation = calcPreferenceDeviation(participantCollection);
        averagePathLength = calcAveragePathLength(participantCollection);
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

    @Override
    public String toString() {
        return "Anzahl: " + numElems +
                " Nachrücker: " + numSuccessors +
                " Geschlechterdiversität: " + genderDiversity +
                " Altersunterschied: " + ageDifference +
                " Vorliebenabweichung: " + preferenceDeviation +
                " Durchschnittliche Pfadlänge: " + averagePathLength;
    }
}
