package model.identNumbers;

import model.event.list.GroupList;
import model.event.list.PairList;
import model.event.list.ParticipantCollectionList;
import model.person.Participant;

import java.util.List;

/**
 * @author Finn Brecher
 * @author Daniel Hinkelmann
 */
public abstract class IdentNumber {
	//TODO: this
    private int numElems;
    private int numSuccessors;
    private int genderDiversity;
    private int ageDifference;
    private int preferenceDeviation;

    protected IdentNumber(ParticipantCollectionList participantCollection) {
    }

    /** Calculates the number of Participants in this Collection
     *
     * @param participantCollection The List
     * @return Number of Participants
     */
    private int calcNumElems(ParticipantCollectionList participantCollection) {
        List<Participant> list;
        if (participantCollection instanceof PairList pairList) {
            list = pairList.getPairs();
        } else {
            GroupList groupList = (GroupList) participantCollection;
            list = groupList.getGroups();
        }

        return list.size();
    }

    /** Calculates the number of Successors for the ParticipantCollectionList
     *
     * @param participantCollection The List
     * @return The number of Successors
     */
    private int calcNumSuccessors(ParticipantCollectionList participantCollection) {
        List<Participant> list;
        if (participantCollection instanceof PairList pairList) {
            list = pairList.getSuccessors();
        } else {
            GroupList groupList = (GroupList) participantCollection;
            list = groupList.getSuccessors();
        }

        return list.size();
    }

    /** Calculates the gender diversity in this ParticipantCollectionList
     *
     * @param participantCollection The List
     * @return The gender diversity
     */
    protected abstract int calcGenderDiversity(ParticipantCollectionList participantCollection);

    /** Calculates the age difference for a ParticipantCollectionList
     *
     * @param participantCollection The List
     * @return The age difference
     */
    protected abstract int calcAgeDifference(ParticipantCollectionList participantCollection);

    /** Calculates the deviation in food preferences of this ParticipantCollectionList
     *
     * @param participantCollection The List
     * @return The deviation in food preferences
     */
    protected abstract int calcPreferenceDeviation(ParticipantCollectionList participantCollection);


}
