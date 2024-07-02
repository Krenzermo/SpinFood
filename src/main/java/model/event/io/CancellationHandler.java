package model.event.io;

import model.event.collection.Group;
import model.event.collection.Pair;
import model.event.list.GroupList;
import model.event.list.PairList;
import model.event.list.weight.GroupWeights;
import model.event.list.weight.PairingWeights;
import model.person.Participant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class handles the cancellation of participants and manages the updates required for pairs and groups.
 * It ensures that all groups and pairs are updated correctly after a cancellation event.
 */
public class CancellationHandler {

    private final PairList pairList;
    private final GroupList groupList;
    private final List<Participant> participantSuccessors;
    private final List<Pair> pairSuccessors;
    private final List<Pair> affectedPairs;

    /**
     * Constructs a CancellationHandler with the specified PairList and GroupList.
     *
     * @param pairList the list of pairs
     * @param groupList the list of groups
     */
    public CancellationHandler(PairList pairList, GroupList groupList) {
        this.pairList = pairList;
        this.groupList = groupList;
        this.participantSuccessors = pairList.getSuccessors();
        this.pairSuccessors = groupList.getSuccessorPairs();
        this.affectedPairs = new ArrayList<>();
    }

    /**
     * Handles the cancellation of participants and updates the relevant pairs and groups.
     *
     * @param cancelledParticipants the list of cancelled participants
     * @param pairingWeights the weights used for pairing participants
     * @param groupWeights the weights used for forming groups
     */
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
                handleSingleCancellation(cancelledParticipant);
            }
        }

        removeGroupsWithSuccessors();
        updateGroups(pairingWeights, groupWeights);
    }

    /**
     * Finds the pair that includes the specified participant.
     *
     * @param participant the participant to find
     * @return the pair that includes the participant, or null if not found
     */
    private Pair findAffectedPair(Participant participant) {
        for (Pair pair : pairList) {
            if (pair.getParticipants().contains(participant)) {
                return pair;
            }
        }
        return null;
    }

    /**
     * Finds the partner of the specified participant in a pair.
     *
     * @param pair the pair to search
     * @param participant the participant whose partner is to be found
     * @return the partner of the participant, or null if not found
     */
    private Participant findPartner(Pair pair, Participant participant) {
        return pair.getParticipants().stream()
                .filter(p -> !p.equals(participant))
                .findFirst()
                .orElse(null);
    }

    /**
     * Handles the cancellation of an entire pair and updates the affected pairs list.
     *
     * @param affectedPair the pair that is being cancelled
     */
    private void handleFullPairCancellation(Pair affectedPair) {
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

    /**
     * Handles the cancellation of a participant within a pair and updates the affected pairs list.
     *
     * @param affectedPair the pair that includes the cancelled participant
     * @param cancelledParticipant the participant that is being cancelled
     * @param pairingWeights the weights used for pairing participants
     * @param groupWeights the weights used for forming groups
     */
    private void handlePartialPairCancellation(Pair affectedPair, Participant cancelledParticipant, PairingWeights pairingWeights, GroupWeights groupWeights) {
        Participant remainingParticipant = findPartner(affectedPair, cancelledParticipant);
        participantSuccessors.add(remainingParticipant);

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

    /**
     * Handles the cancellation of a single participant.
     *
     * @param cancelledParticipant the participant that is being cancelled
     */
    private void handleSingleCancellation(Participant cancelledParticipant) {
        participantSuccessors.remove(cancelledParticipant);
    }

    /**
     * Finds a successor for the specified participant based on the pairing weights.
     *
     * @param participant the participant to find a successor for
     * @param pairingWeights the weights used for pairing participants
     * @return the successor participant, or null if not found
     */
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

    /**
     * Removes all groups that include participants from the successor list or pairs from the affected pairs list.
     */
    private void removeGroupsWithSuccessors() {
        List<Group> groupsToRemove = new ArrayList<>();

        // Remove groups with participant successors
        for (Group group : groupList) {
            for (Participant successor : participantSuccessors) {
                if (group.getParticipants().contains(successor)) {
                    groupsToRemove.add(group);
                    break;
                }
            }
        }

        // Remove groups with affected pairs
        for (Group group : groupList) {
            for (Pair pair : affectedPairs) {
                if (Arrays.asList(group.getPairs()).contains(pair)) {
                    groupsToRemove.add(group);
                    break;
                }
            }
        }

        groupList.removeAll(groupsToRemove);
    }

    /**
     * Updates the groups by forming new pairs and groups from the successor lists.
     *
     * @param pairingWeights the weights used for pairing participants
     * @param groupWeights the weights used for forming groups
     */
    public void updateGroups(PairingWeights pairingWeights, GroupWeights groupWeights) {
        // First form new pairs from participant successors
        List<Participant> tempParticipantSuccessors = new ArrayList<>(participantSuccessors);
        participantSuccessors.clear();

        if (tempParticipantSuccessors.size() >= 2) {
            List<Pair> newPairs = PairList.buildBestPairs(tempParticipantSuccessors, pairingWeights);
            pairSuccessors.addAll(newPairs);
        }

        // Add all affected pairs to pairSuccessors
        pairSuccessors.addAll(affectedPairs);

        // Then form new groups from pair successors
        if (pairSuccessors.size() >= 9) {
            List<Pair> newPairs = new ArrayList<>(pairSuccessors);
            pairSuccessors.clear();

            List<Group> newGroups = GroupList.buildBestGroups(newPairs, groupWeights);
            groupList.addAll(newGroups);
        }
    }
}
