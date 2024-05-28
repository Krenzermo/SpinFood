package model.identNumbers;

import model.event.collection.Pair;
import model.event.list.PairList;
import model.event.list.ParticipantCollectionList;
import model.person.Participant;

import java.util.concurrent.atomic.AtomicInteger;

public class PairIdentNumber extends IdentNumber{

    protected PairIdentNumber(PairList participantCollection) {
        super(participantCollection);
        genderDiversity = calcGenderDiversity(participantCollection);
        ageDifference = calcAgeDifference(participantCollection);
        preferenceDeviation = calcPreferenceDeviation(participantCollection);
    }

    @Override
    protected int calcGenderDiversity(ParticipantCollectionList participantCollection) {
        PairList pairList = (PairList) participantCollection;
        AtomicInteger genderDiversity = new AtomicInteger();

        //Calculate the gender Diversity of each pair and increment if the genders divers
        pairList.getPairs().stream().map(Pair::getParticipants).forEach(p -> {
            Participant p1 = p.get(0);
            Participant p2 = p.get(1);

            if (p1.getGender() != p2.getGender()) {
                genderDiversity.getAndIncrement();
            }
        });

        return genderDiversity.get();
    }

    @Override
    protected int calcAgeDifference(ParticipantCollectionList participantCollection) {
        PairList pairList = (PairList) participantCollection;
        return pairList.getPairs().stream().mapToInt(Pair::getAgeDifference).sum();
    }

    @Override
    protected int calcPreferenceDeviation(ParticipantCollectionList participantCollection) {
        //todo
        return 0;
    }
}
