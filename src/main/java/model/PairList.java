package model;

import model.kitchen.KitchenAvailability;
import model.person.Participant;

import java.util.ArrayList;

public class PairList {

    private final ArrayList<Pair> pairList;

    public PairList(ArrayList<Participant> ParticipantList, ArrayList<Pair> pairList) {
        ArrayList<Participant> sortedParticipantList= sortByKitchen(ParticipantList);
        this.pairList = pairUp(sortedParticipantList);

    }

    public ArrayList<Pair> pairUp(ArrayList<Participant> participantList){
        ArrayList<Pair> pairedPairList = new ArrayList<Pair>();
        for(int i=0; i <= participantList.size(); i++){

        }

        return pairedPairList;
    }

    public ArrayList<Participant> sortByKitchen(ArrayList<Participant> participantList){
        ArrayList<Participant> sortedParticipantList = new ArrayList<Participant>();
        for(int i=0; i <= participantList.size(); i++){
            if(participantList.get(i).isHasKitchen().equals("no")) {
                sortedParticipantList.add(participantList.get(i));
            }

        }
        for(int i=0; i <= participantList.size(); i++){
            if(participantList.get(i).isHasKitchen().equals("maybe")) {
                sortedParticipantList.add(participantList.get(i));
            }

        }
        for(int i=0; i <= participantList.size(); i++){
            if(participantList.get(i).isHasKitchen().equals("yes")) {
                sortedParticipantList.add(participantList.get(i));
            }

        }
        return sortedParticipantList;


    }
}
