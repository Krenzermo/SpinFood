package model.processing;

import model.event.collection.Group;
import model.event.collection.Pair;
import model.event.list.GroupList;
import model.event.list.PairList;
import model.event.list.weight.GroupWeights;
import model.person.Participant;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * This class handles the cancellation of participants and manages the updates required for pairs and groups.
 * It ensures that all groups and pairs are updated correctly after a cancellation event.
 */
public class CancellationHandler {

    private final PairList pairList;
    private GroupList groupList;
    private final List<Participant> participantSuccessors;

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
    }

    /**
     * Handles the cancellation of participants and updates the relevant pairs and groups.
     *
     * @param cancelledParticipants the list of cancelled participants
     * @param groupWeights the weights used for forming groups
     */
    public void handleCancellation(List<Participant> cancelledParticipants, GroupWeights groupWeights) {
        for (Participant cancelledParticipant : cancelledParticipants) {
            Pair affectedPair = findAffectedPair(cancelledParticipant);
            if (affectedPair != null) {
                Participant partner = findPartner(affectedPair, cancelledParticipant);
                if (cancelledParticipants.contains(partner)) {
                    cancelledParticipants.remove(partner);
                    handleFullPairCancellation(affectedPair);
                } else {
                    handlePartialPairCancellation(affectedPair, cancelledParticipant);
                }
            } else {
                handleSingleCancellation(cancelledParticipant);
            }
        }

        if (!Objects.isNull(groupList)) {
            updateGroups(groupWeights);
        }
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
    public void handleFullPairCancellation(Pair affectedPair) {
        pairList.remove(affectedPair);
    }

    /**
     * Handles the cancellation of a participant within a pair and updates the affected pairs list.
     *
     * @param affectedPair the pair that includes the cancelled participant
     * @param cancelledParticipant the participant that is being cancelled
     */
    public void handlePartialPairCancellation(Pair affectedPair, Participant cancelledParticipant) {
        Participant remainingParticipant = findPartner(affectedPair, cancelledParticipant);
        participantSuccessors.add(remainingParticipant);

        pairList.remove(affectedPair);
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
     * Updates the groups by forming new pairs and groups from the successor lists.
     *
     * @param groupWeights the weights used for forming groups
     */
    public void updateGroups(GroupWeights groupWeights) {
        List<Pair> newPairs = new ArrayList<>();
        if (participantSuccessors.size() >= 2) {
            // First form new pairs from participant successors
            List<Participant> tempParticipantSuccessors = new ArrayList<>(participantSuccessors);
            PairList tempPairList = new PairList(pairList.getPairingWeights());
            tempPairList.clear();
            newPairs = tempPairList.buildBestPairs(tempParticipantSuccessors, pairList.getPairingWeights());
        }

        // Remove participants in newPairs from participantSuccessors
        for (Pair pair : newPairs) {
            for (Participant participant : pair.getParticipants()) {
                participantSuccessors.remove(participant);
            }
        }

        // Add all new pairs to the existing pair list
        pairList.addAll(newPairs);

        // Create a new GroupList with all pairs
        GroupList newGroupList = new GroupList(pairList, groupWeights);

        groupList = newGroupList;
    }
}
