package model.event.io;

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
        for (Participant cancelledParticipant : cancelledParticipants) {
            Pair affectedPair = findAffectedPair(cancelledParticipant);
            if (affectedPair != null) {
                Participant partner = findPartner(affectedPair, cancelledParticipant);
                if (cancelledParticipants.contains(partner)) {
                    cancelledParticipants.remove(partner);
                    handleFullPairCancellation(affectedPair);
                } else {
                    handlePartialPairCancellation(affectedPair, cancelledParticipant, pairingWeights, groupWeights);
                }
            } else {
                handleSingleCancellation(cancelledParticipant, pairingWeights, groupWeights);
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

    private void handleFullPairCancellation(Pair affectedPair) {
        pairList.remove(affectedPair);

        for (Group group : affectedPair.getGroups()) {
            if (group != null) {
                groupList.remove(group);
            }
        }
    }

    private void handlePartialPairCancellation(Pair affectedPair, Participant cancelledParticipant, PairingWeights pairingWeights, GroupWeights groupWeights) {
        Participant remainingParticipant = findPartner(affectedPair, cancelledParticipant);

        pairList.remove(affectedPair);

        for (Group group : affectedPair.getGroups()) {
            if (group != null) {
                groupList.remove(group);
            }
        }

        if (remainingParticipant != null) {
            Participant successor = findSuccessorForParticipant(remainingParticipant, pairingWeights);
            if (successor != null) {
                Pair newPair = new Pair(remainingParticipant, successor);
                pairList.add(newPair);
                updateGroups(pairingWeights, groupWeights);
            } else {
                participantSuccessors.add(remainingParticipant);
            }
        }
    }

    private void handleSingleCancellation(Participant cancelledParticipant, PairingWeights pairingWeights, GroupWeights groupWeights) {
        participantSuccessors.remove(cancelledParticipant);

        Pair affectedPair = findAffectedPair(cancelledParticipant);
        if (affectedPair != null) {
            handlePartialPairCancellation(affectedPair, cancelledParticipant, pairingWeights, groupWeights);
        } else {
            rerunPairingAlgorithm(pairingWeights);
        }
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

    private Pair findSuccessorPairForSingle(Participant participant, PairingWeights pairingWeights) {
        for (Pair pair : pairSuccessors) {
            if (pair.getParticipants().contains(participant)) {
                pairSuccessors.remove(pair);
                return pair;
            }
        }
        return null;
    }

    private void rerunPairingAlgorithm(PairingWeights pairingWeights) {
        PairList newPairList = new PairList(InputData.getInstance(), pairingWeights);
        pairList.clear();
        pairList.addAll(newPairList);
    }

    public void updateGroups(PairingWeights pairingWeights, GroupWeights groupWeights) {
        GroupList newGroupList = new GroupList(pairList, groupWeights);
        groupList.clear();
        groupList.addAll(newGroupList.getGroups());

    }
}
