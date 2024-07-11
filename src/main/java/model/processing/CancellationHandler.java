package model.processing;

import controller.FXMLControllers.MainController;
import model.event.collection.Group;
import model.event.collection.Pair;
import model.event.list.GroupList;
import model.event.list.PairList;
import model.event.list.weight.GroupWeights;
import model.person.Participant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * This class handles the cancellation of participants and manages the updates required for pairs and groups.
 * It ensures that all groups and pairs are updated correctly after a cancellation event.
 * @author Davide Piacenza
 */
public class CancellationHandler {

    private final PairList pairList;
    private final GroupList groupList;
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
     */
    public void handleCancellation(List<Participant> cancelledParticipants) {
        for (Participant cancelledParticipant : cancelledParticipants) {
            Pair affectedPair = cancelledParticipant.getPair();
            if (affectedPair != null) {
                Participant partner = affectedPair.getOtherParticipant(cancelledParticipant);
                if (cancelledParticipants.contains(partner)) {
                    if (pairList.contains(affectedPair)) {
                        handleFullPairCancellation(affectedPair);
                    }
                } else {
                    handlePartialPairCancellation(affectedPair, cancelledParticipant);
                }
            } else {
                handleSingleCancellation(cancelledParticipant);
            }
            if (!Objects.isNull(groupList) && !Objects.isNull(affectedPair)) {
                updateGroups(affectedPair);
            }
        }
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
        Participant remainingParticipant = affectedPair.getOtherParticipant(cancelledParticipant);
        remainingParticipant.clearPair();
        participantSuccessors.add(remainingParticipant);
        pairList.remove(affectedPair);
    }

    /**
     * Handles the cancellation of a single participant.
     *
     * @param cancelledParticipant the participant that is being canceled
     */
    public void handleSingleCancellation(Participant cancelledParticipant) {
        participantSuccessors.remove(cancelledParticipant);
    }

    /**
     * method to pair Successor Participant to new pairs
     */
    public void pairSuccessorParticipants() {
        List<Pair> newPairs = new ArrayList<>();

        // First form new pairs from participant successors
        if (participantSuccessors.size() >= 2) {
            List<Participant> tempParticipantSuccessors = new ArrayList<>(participantSuccessors);
            PairList tempPairList = new PairList(pairList.getPairingWeights());
            tempPairList.clear();
            newPairs = tempPairList.buildBestPairs(tempParticipantSuccessors, pairList.getPairingWeights());
        }

        // Remove participants in newPairs from participantSuccessors
        newPairs.forEach(pair -> participantSuccessors.removeAll(pair.getParticipants()));

        // Add all new pairs to the existing pair list
        pairList.addAll(newPairs);
    }

    /**
     * Updates the groups by forming new pairs and groups from the successor lists.
     */
    public void updateGroups(Pair affectedPair) {
        pairSuccessorParticipants();

    }
}
