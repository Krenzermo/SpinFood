package model.identNumbers;

import model.event.collection.Pair;
import model.event.collection.ParticipantCollection;
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
    protected int genderDiversity;
    protected int ageDifference;
    protected int preferenceDeviation;

    protected IdentNumber(ParticipantCollectionList participantCollection) {
        numElems = calcNumElems(participantCollection);
        numElems = calcNumSuccessors(participantCollection);
    }

    /** Calculates the number of ParticipantCollections in this List
     *
     * @param participantCollection The List
     * @return Number of Participants
     */
    private int calcNumElems(ParticipantCollectionList participantCollection) {
        List<? extends ParticipantCollection> list;
        list = participantCollection instanceof PairList pairList ? pairList.getPairs() :
                                                                    ((GroupList)participantCollection).getGroups();
        return list.size();
    }

    /** Calculates the number of Successors for the ParticipantCollectionList
     *
     * @param participantCollection The List
     * @return The number of Successors
     */
    private int calcNumSuccessors(ParticipantCollectionList participantCollection) {
        List<Participant> list;
        list = participantCollection instanceof PairList pairList ? pairList.getSuccessors() :
                                                                    ((GroupList)participantCollection).getSuccessors();
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
