package model.event.list;

import model.event.InputData;
import model.event.PairingWeights;
import model.event.collection.Pair;
import model.event.collection.ParticipantCollection;
import model.identNumbers.IdentNumber;
import model.identNumbers.PairIdentNumber;
import model.kitchen.KitchenAvailability;
import model.person.FoodType;
import model.person.Participant;

import java.util.ArrayList;
import java.util.List;


//TODO identnumber methoden anders verbauen? gewichtung identnumber anpassen(insbesondere meat-none paarung die gewünscht ist)? gewichtung paarsortierung anpassen?
//TODO in pair mit autoassign kitchen den küchenbesitzer festhalten? falls nötig maybe küchenwahl verbessern,   habe identnummer nicht mehr protected gemacht und doubles, ka
public class PairList implements ParticipantCollectionList {
    private IdentNumber identNumber;
    private final ArrayList<Pair> pairs;
    public ArrayList<Participant> successors = new ArrayList<>();

    public PairList(InputData inputData, PairingWeights pairingWeights) {

        ArrayList<Participant> sortedParticipantList= sortParticipants(inputData.getParticipantInputData());

        this.pairs = (buildBestPairs(sortedParticipantList, pairingWeights));
        pairs.addAll(inputData.getPairInputData());
        this.identNumber = deriveIdentNumber(pairs);

    }

    public ArrayList<Pair> buildBestPairs(ArrayList<Participant> participantList, PairingWeights pairingWeights){
        ArrayList<Pair> bestPairList = new ArrayList<>();

        while (participantList.size() >= 2) {
            Participant participant1 = participantList.remove(0);
            int bestPartnerPosition = -1;
            double bestPartnerScore = -100;

            for(int i=0; i < participantList.size(); i++) {
                double score = 0;
                Participant testedParticipant = participantList.get(i);
                score += compareKitchen(participant1, testedParticipant);
                score += compareGender(participant1, testedParticipant, pairingWeights);
                score += compareFoodPreference(participant1, testedParticipant, pairingWeights);
                score += compareAge(participant1, testedParticipant, pairingWeights);

                if (score > bestPartnerScore) {
                    bestPartnerScore = score;
                    bestPartnerPosition = i;
                }
            }

            if (bestPartnerPosition == -1){
                successors.add(participant1);
                continue;
            }

            Participant participant2 = participantList.remove(bestPartnerPosition);
            bestPairList.add(new Pair(participant1, participant2, false));   //TODO wessen Küche als Feld in Pair?


        }
        successors.addAll(participantList);
        return bestPairList;
    }
// requires List ordered by kitchen
    public double compareKitchen(Participant participant1, Participant testedParticipant){
        if (participant1.isHasKitchen() == KitchenAvailability.YES){
            if (testedParticipant.isHasKitchen() == KitchenAvailability.YES && participant1.getKitchen().equals(testedParticipant.getKitchen())) {
                return -1000;
            } else {
                return 0;
            }

        } else if(participant1.isHasKitchen() == KitchenAvailability.NO) {
            if (testedParticipant.isHasKitchen() == KitchenAvailability.YES){
                return 0;
            } else if (testedParticipant.isHasKitchen() == KitchenAvailability.MAYBE) {
                return -50;
            } else {
                return -1000;
            }
        } else {
            if (testedParticipant.isHasKitchen() == KitchenAvailability.YES) {
                return 0;
            } else {
                return -50;


            }
        }
    }   //TODO was passiert wenn wir maybe einbinden wollen? einfach successors mit maybe als yes neu durchlaufen lassen?
    public double compareGender(Participant participant1, Participant testedParticipant, PairingWeights pairingWeights){
        if(participant1.getGender().equals(testedParticipant.getGender())) {
            return 0;
        } else {
            return 0.5 * pairingWeights.getPairGenderDifferenceWeight();
        }
    }

    public double compareFoodPreference(Participant participant1, Participant testedParticipant, PairingWeights pairingWeights){
        if(participant1.getFoodType() == FoodType.MEAT){
            if (testedParticipant.getFoodType() == FoodType.MEAT) {
                return pairingWeights.getPairFoodPreferenceWeight();
            } else if (testedParticipant.getFoodType() == FoodType.ANY){
                return 0.5 * pairingWeights.getPairFoodPreferenceWeight();
            } else{
                return -1000;
            }
        } else if (participant1.getFoodType() == FoodType.VEGGIE){
            if (testedParticipant.getFoodType() == FoodType.VEGGIE) {
                return pairingWeights.getPairFoodPreferenceWeight();
            } else if (testedParticipant.getFoodType() == FoodType.VEGAN){
                return 0.5 * pairingWeights.getPairFoodPreferenceWeight();
            } else if (testedParticipant.getFoodType() == FoodType.ANY){
                return 0.25 * pairingWeights.getPairFoodPreferenceWeight();
            } else{
                return -1000;
            }
        } else if (participant1.getFoodType() == FoodType.VEGAN){
            if (testedParticipant.getFoodType() == FoodType.VEGAN) {
                return pairingWeights.getPairFoodPreferenceWeight();
            } else if (testedParticipant.getFoodType() == FoodType.VEGGIE){
                return 0.5 * pairingWeights.getPairFoodPreferenceWeight();
            } else if (testedParticipant.getFoodType() == FoodType.ANY){
                return 0.25 * pairingWeights.getPairFoodPreferenceWeight();
            } else{
                return -1000;
            }
        } else {
            if (testedParticipant.getFoodType() == FoodType.ANY) {
                return 0.5 * pairingWeights.getPairFoodPreferenceWeight();
            } else if (testedParticipant.getFoodType() == FoodType.MEAT){
                return 0.5 * pairingWeights.getPairFoodPreferenceWeight();
            } else{
                return 0.25 * pairingWeights.getPairFoodPreferenceWeight();
            }
        }

    }
    public double compareAge(Participant participant1, Participant testedParticipant, PairingWeights pairingWeights){
        return pairingWeights.getPairAgeDifferenceWeight() * (1- 0.1 *(participant1.getAge().getAgeDifference(testedParticipant.getAge())));

    }

    public ArrayList<Participant> sortParticipants(ArrayList<Participant> participantList) {
        ArrayList<Participant> sortedNoKitchenList = new ArrayList<>();
        ArrayList<Participant> sortedMaybeKitchenList = new ArrayList<>();
        ArrayList<Participant> sortedYesKitchenList = new ArrayList<>();
        ArrayList<Participant> sortedParticipantList = new ArrayList<>();

        for (Participant participant : participantList) {
            if (participant.isHasKitchen() == KitchenAvailability.NO) {
                sortedNoKitchenList.add(participant);
            }
        }

        for (Participant participant : participantList) {
            if (participant.isHasKitchen() == KitchenAvailability.MAYBE) {
                sortedMaybeKitchenList.add(participant);
            }
        }

        for (Participant participant : participantList) {
            if (participant.isHasKitchen() == KitchenAvailability.YES) {
                sortedYesKitchenList.add(participant);
            }
        }

        sortedParticipantList.addAll(sortByFoodType(sortedNoKitchenList));
        sortedParticipantList.addAll(sortByFoodType(sortedMaybeKitchenList));
        sortedParticipantList.addAll(sortByFoodType(sortedYesKitchenList));
        return sortedParticipantList;
    }

    public ArrayList<Participant> sortByFoodType(ArrayList<Participant> participantList) {
        ArrayList<Participant> sortedParticipantList = new ArrayList<>();

        for (Participant participant : participantList) {
            if (participant.getFoodType() != FoodType.ANY) {
                sortedParticipantList.add(participant);
            }
        }

        for (Participant participant : participantList) {
            if (participant.getFoodType() == FoodType.ANY) {
                sortedParticipantList.add(participant);
            }
        }

        return sortedParticipantList;
    }

    public ArrayList<Participant> getSuccessors() {
        return successors;
    }

    public ArrayList<Pair> getPairs() {
        return pairs;
    }

    public IdentNumber deriveIdentNumber (ArrayList<Pair> pairs) {
        return new PairIdentNumber(this);
    }

    /**
     * @return the {@link IdentNumber} (Identifying Numbers) of this ParticipantCollectionList
     */
    @Override
    public IdentNumber getIdentNumber() {
        return identNumber;
    }

    /**
     * @return the evaluation of this ParticipantCollection
     */
    @Override
    public double evaluate() {
        // TODO: this
        return 0;
    }

    /**
     * This method is used to change the underlying data structure.
     * It is used by the default implementations of {@link #add}, {@link #remove}, {@link #addUnsafe},
     * {@link #addAll} and {@link #removeAll}methods.
     *
     * @return the data structure that stores the instances of {@link ParticipantCollection}
     */
    @Override
    public List<ParticipantCollection> getDataStructure() {
        return null; //TODO alles wieder zu participant collection?
    }

    /**
     * Checks if the collection has the same type as this ParticipantCollectionList (subclass)
     *
     * @param collection the element to be checked
     * @throws IllegalArgumentException if the type check fails
     */
    @Override
    public void checkType(ParticipantCollection collection) {
        if (!collection.getClass().equals(Pair.class)) {
            throw new IllegalArgumentException("Collection is not a Pair");
        }
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("PairList {\n");

        sb.append("Pairs:\n");
        for (Pair pair : pairs) {
            sb.append(pair.toString()).append("\n");
        }

        sb.append("\nSuccessors:\n");
        for (Participant successor : successors) {
            sb.append(successor.toString()).append("\n");
        }

        sb.append("}");
        return sb.toString();
    }
}