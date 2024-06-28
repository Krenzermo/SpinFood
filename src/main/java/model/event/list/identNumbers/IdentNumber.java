package model.event.list.identNumbers;

import javafx.beans.Observable;
import javafx.util.Callback;
import model.event.collection.Group;
import model.event.collection.Pair;
import model.event.collection.ParticipantCollection;
import model.event.list.GroupList;
import model.event.list.PairList;
import model.event.list.ParticipantCollectionList;
import model.person.Participant;

import java.util.List;

/**
 * The abstract class for the different identNumbers of {@link Pair} and {@link Group}
 *
 * @author Finn Brecher
 * @author Daniel Hinkelmann
 */
public abstract class IdentNumber {
	//TODO: this
    protected int numElems;
    protected int numSuccessors;
    protected double genderDiversity;
    protected double ageDifference;
    protected double preferenceDeviation;

    protected IdentNumber(ParticipantCollectionList participantCollection) {
        numElems = calcNumElems(participantCollection);
        numSuccessors = calcNumSuccessors(participantCollection);
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
    protected abstract double calcGenderDiversity(ParticipantCollectionList participantCollection);

    /** Calculates the average age difference for a ParticipantCollectionList
     *
     * @param participantCollection The List
     * @return The age difference
     */
    protected abstract double calcAgeDifference(ParticipantCollectionList participantCollection);

    /** Calculates the deviation in food preferences of this ParticipantCollectionList
     *
     * @param participantCollection The List
     * @return The deviation in food preferences
     */
    protected abstract double calcPreferenceDeviation(ParticipantCollectionList participantCollection);

    @Override
    public String toString() {
        return "Anzahl: " + numElems + " Nachrücker: " + numSuccessors + " Geschlechterdiversität: " + genderDiversity + " Altersunterschied: " + ageDifference + " Vorliebenabweichung: " + preferenceDeviation;
    }

    public List<String> asList() {
        return List.of(
                "Anzahl Paare:\t\t" + numElems,
                "Anzahl Nachrücker:\t" + numSuccessors,
                "Altersdifferenz:\t\t" + Math.round(ageDifference * 1_000_000) / 1_000_000d,
                "Geschlechterdiversität:\t" + Math.round(genderDiversity * 1_000_000) / 1_000_000d,
                "Vorliebenabweichung:\t" + Math.round(preferenceDeviation * 1_000_000) / 1_000_000d
        );
    }
}
