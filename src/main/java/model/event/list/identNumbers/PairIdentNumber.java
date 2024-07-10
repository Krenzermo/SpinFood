package model.event.list.identNumbers;

import model.event.collection.Pair;
import model.event.list.PairList;
import model.event.list.ParticipantCollectionList;

/** A class that holds and calculates the defined IdentNumber for a {@link Pair}
 *
 * @author Daniel Hinkelmann
 */
public class PairIdentNumber extends IdentNumber{

    public PairIdentNumber(PairIdentNumber num) {
        super(num.participantCollectionList);
        genderDiversity = num.genderDiversity;
        ageDifference = num.ageDifference;
        preferenceDeviation = num.preferenceDeviation;
    }

    public PairIdentNumber(PairList participantCollection) {
        super(participantCollection);
        genderDiversity = calcGenderDiversity(participantCollection);
        ageDifference = calcAgeDifference(participantCollection);
        preferenceDeviation = calcPreferenceDeviation(participantCollection);
    }

    @Override
    protected double calcGenderDiversity(ParticipantCollectionList participantCollection) {
        PairList pairList = (PairList) participantCollection;
        return pairList.getPairs().stream().mapToDouble(Pair::getGenderDeviation).sum() / numElems;
    }

    @Override
    protected double calcAgeDifference(ParticipantCollectionList participantCollection) {
        PairList pairList = (PairList) participantCollection;
        return pairList.getPairs().stream().mapToInt(Pair::getAgeDifference).sum() / (double)numElems;
    }

    @Override
    protected double calcPreferenceDeviation(ParticipantCollectionList participantCollection) {
        PairList pairList = (PairList) participantCollection;
        return pairList.getPairs().stream().mapToInt(Pair::getPreferenceDeviation).sum() / (double)numElems;
    }
}
