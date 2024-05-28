package model.identNumbers;

import model.event.list.ParticipantCollectionList;

public class GroupIdentNumber extends IdentNumber{

    //todo

    protected GroupIdentNumber(ParticipantCollectionList participantCollection) {
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
