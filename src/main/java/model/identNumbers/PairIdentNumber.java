package model.identNumbers;

import model.event.list.ParticipantCollectionList;

public class PairIdentNumber extends IdentNumber{

    //todo

    protected PairIdentNumber(ParticipantCollectionList participantCollection) {
        super(participantCollection);
    }

    @Override
    protected int calcGenderDiversity(ParticipantCollectionList participantCollection) {
        return 0;
    }

    @Override
    protected int calcAgeDifference(ParticipantCollectionList participantCollection) {
        return 0;
    }

    @Override
    protected int calcPreferenceDeviation(ParticipantCollectionList participantCollection) {
        return 0;
    }
}
