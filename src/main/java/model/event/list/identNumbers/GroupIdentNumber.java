package model.event.list.identNumbers;

import model.event.list.ParticipantCollectionList;

public class GroupIdentNumber extends IdentNumber{

    //todo

    protected GroupIdentNumber(ParticipantCollectionList participantCollection) {
        super(participantCollection);
    }

    @Override
    protected double calcGenderDiversity(ParticipantCollectionList participantCollection) {
        return 0;
    }

    @Override
    protected double calcAgeDifference(ParticipantCollectionList participantCollection) {
        return 0;
    }

    @Override
    protected double calcPreferenceDeviation(ParticipantCollectionList participantCollection) {
        return 0;
    }
}
