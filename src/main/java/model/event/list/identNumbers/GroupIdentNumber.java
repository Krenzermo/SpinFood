package model.event.list.identNumbers;

import model.event.collection.Group;
import model.event.collection.Pair;
import model.event.list.GroupList;
import model.event.list.ParticipantCollectionList;
import model.event.InputData;

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
                    double[] pathLengths = new double[pairs.length];

                    for (int i = 0; i < pairs.length; i++) {
                        Pair pair = pairs[i];

                        // Berechne den Weg von Vorspeise über Hauptspeise bis zum Dessert und zurück zur Eventlocation
                        double distanceToStarter = pair.getKitchen().location().getDistance(group.getKitchen().location());
                        double distanceStarterToMain = group.getPairs()[0].getKitchen().location().getDistance(group.getPairs()[1].getKitchen().location());
                        double distanceMainToDessert = group.getPairs()[1].getKitchen().location().getDistance(group.getPairs()[2].getKitchen().location());
                        double distanceDessertToEvent = group.getPairs()[2].getKitchen().location().getDistance(InputData.getInstance().getEventLocation());

                        // Summiere die Distanzen, um die Gesamtlänge des Pfades zu berechnen
                        pathLengths[i] = distanceToStarter + distanceStarterToMain + distanceMainToDessert + distanceDessertToEvent;
                    }

                    return Arrays.stream(pathLengths);
                })
                .sum();
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
