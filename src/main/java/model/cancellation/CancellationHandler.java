package model.cancellation;

import model.event.collection.Group;
import model.event.collection.Pair;
import model.event.list.GroupList;
import model.event.list.PairList;
import model.event.list.weight.GroupWeights;
import model.event.list.weight.PairingWeights;
import model.person.Participant;

import java.util.ArrayList;
import java.util.List;

public class CancellationHandler {

    private final PairList pairList;
    private final GroupList groupList;
    private final List<Participant> participantSuccessors;
    private final List<Pair> pairSuccessors;

    public CancellationHandler(PairList pairList, GroupList groupList) {
        this.pairList = pairList;
        this.groupList = groupList;
        this.participantSuccessors = pairList.getSuccessors();
        this.pairSuccessors = groupList.getSuccessorPairs();
    }

    public void handleCancellation(List<Participant> cancelledParticipants, PairingWeights pairingWeights, GroupWeights groupWeights) {
        List<Pair> affectedPairs = new ArrayList<>();

        for (Participant cancelledParticipant : cancelledParticipants) {
            Pair affectedPair = findAffectedPair(cancelledParticipant);
            if (affectedPair != null) {
                Participant partner = findPartner(affectedPair, cancelledParticipant);
                if (cancelledParticipants.contains(partner)) {
                    cancelledParticipants.remove(partner);
                    handleFullPairCancellation(affectedPair, affectedPairs);
                } else {
                    handlePartialPairCancellation(affectedPair, cancelledParticipant, affectedPairs, pairingWeights, groupWeights);
                }
            } else {
                handleSingleCancellation(cancelledParticipant);
            }
        }

        updateGroups(pairingWeights, groupWeights);
    }

    private Pair findAffectedPair(Participant participant) {
        for (Pair pair : pairList) {
            if (pair.getParticipants().contains(participant)) {
                return pair;
            }
        }
        return null;
    }

    private Participant findPartner(Pair pair, Participant participant) {
        return pair.getParticipants().stream()
                .filter(p -> !p.equals(participant))
                .findFirst()
                .orElse(null);
    }

    private void handleFullPairCancellation(Pair affectedPair, List<Pair> affectedPairs) {
        pairList.remove(affectedPair);

        for (Group group : affectedPair.getGroups()) {
            if (group != null) {
                for (Pair pair : group.getPairs()) {
                    if (!pair.equals(affectedPair) && !affectedPairs.contains(pair)) {
                        affectedPairs.add(pair);
                    }
                }
                groupList.remove(group);
            }
        }
    }

    private void handlePartialPairCancellation(Pair affectedPair, Participant cancelledParticipant, List<Pair> affectedPairs, PairingWeights pairingWeights, GroupWeights groupWeights) {
        Participant remainingParticipant = findPartner(affectedPair, cancelledParticipant);

        pairList.remove(affectedPair);

        for (Group group : affectedPair.getGroups()) {
            if (group != null) {
                for (Pair pair : group.getPairs()) {
                    if (!pair.equals(affectedPair) && !affectedPairs.contains(pair)) {
                        affectedPairs.add(pair);
                    }
                }
                groupList.remove(group);
            }
        }

        if (remainingParticipant != null) {
            Participant successor = findSuccessorForParticipant(remainingParticipant, pairingWeights);
            if (successor != null) {
                Pair newPair = new Pair(remainingParticipant, successor);
                pairList.add(newPair);
                affectedPairs.add(newPair);
            } else {
                participantSuccessors.add(remainingParticipant);
            }
        }
    }

    private void handleSingleCancellation(Participant cancelledParticipant) {
        participantSuccessors.remove(cancelledParticipant);
    }

    private Participant findSuccessorForParticipant(Participant participant, PairingWeights pairingWeights) {
        for (Participant successor : participantSuccessors) {
            double score = PairList.calculatePairScore(participant, successor, pairingWeights);
            if (score != Double.NEGATIVE_INFINITY) {
                participantSuccessors.remove(successor);
                return successor;
            }
        }
        return null;
    }

    public void updateGroups(PairingWeights pairingWeights, GroupWeights groupWeights) {
        // Zuerst neue Paare aus Teilnehmer-Nachfolgern bilden
        List<Participant> tempParticipantSuccessors = new ArrayList<>(participantSuccessors);
        participantSuccessors.clear();

        if (tempParticipantSuccessors.size() >= 2) {
            List<Pair> newPairs = PairList.buildBestPairs(tempParticipantSuccessors, pairingWeights);
            pairSuccessors.addAll(newPairs);
        }

        // Dann Gruppen aus Paar-Nachfolgern erstellen
        if (pairSuccessors.size() >= 9) {
            List<Pair> newPairs = new ArrayList<>(pairSuccessors);
            pairSuccessors.clear();

            List<Group> newGroups = new GroupList(newPairs, groupWeights).getGroups();
            groupList.addAll(newGroups);
        }
    }
}
