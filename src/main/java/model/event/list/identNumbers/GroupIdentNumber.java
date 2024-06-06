package model.event.list.identNumbers;

import model.event.list.ParticipantCollectionList;

public class GroupIdentNumber extends IdentNumber{
    private double averagePathLength;

    //todo

    protected GroupIdentNumber(ParticipantCollectionList participantCollection) {
        super(participantCollection);
        genderDiversity = calcGenderDiversity(participantCollection);
        ageDifference = calcAgeDifference(participantCollection);
        preferenceDeviation = calcPreferenceDeviation(participantCollection);
        averagePathLength = calcAveragePathLength(participantCollection);
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

    protected double calcAveragePathLength(ParticipantCollectionList participantCollection) {
        return 0;
    }
}
