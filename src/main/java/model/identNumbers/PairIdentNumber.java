package model.identNumbers;

import model.event.collection.Pair;
import model.event.list.PairList;
import model.event.list.ParticipantCollectionList;
import model.person.Participant;

import java.util.concurrent.atomic.AtomicInteger;

public class PairIdentNumber extends IdentNumber{

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
