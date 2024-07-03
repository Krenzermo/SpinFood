package model.event.list;

import model.event.io.InputData;
import model.event.list.weight.PairingWeights;
import model.event.collection.Pair;
import model.event.list.identNumbers.IdentNumber;
import model.event.list.identNumbers.PairIdentNumber;
import model.kitchen.KitchenAvailability;
import model.person.FoodType;
import model.person.Participant;

import java.util.ArrayList;
import java.util.List;

/**
 * The PairList class represents a collection of pairs of participants.
 * It provides functionality to pair participants based on various criteria
 * and maintains a list of unpaired participants (successors).
 *
 * @author Ole Krenzer
 * @author Daniel Hinkelmann
 * @author Finn Brecher
 * @author Davide Piacenza
 */
public class PairList extends ParticipantCollectionList<Pair> {
    private static final InputData inputData = InputData.getInstance();
    private final IdentNumber identNumber;
    private static final List<Participant> successors = new ArrayList<>();

    /**
     * Constructs a PairList object by sorting participants and building the best pairs.
     *
     * @param pairingWeights the weights used for pairing criteria
     */
    public PairList(PairingWeights pairingWeights) {
        this(buildBestPairs(sortParticipants(inputData.getParticipantInputData()), pairingWeights));
        //this.printFoodNumbers();
    }

    public PairList(List<Pair> pairList) {
        successors.clear();
        setList(pairList);
        addAll(inputData.getPairInputData());
        this.identNumber = deriveIdentNumber();
    }

    /**
     * Builds the best pairs of participants based on pairing criteria and weights.
     *
     * @param participantList the list of participants to be paired
     * @param pairingWeights  the weights used for pairing criteria
     * @return a list of the best pairs of participants
     */
    public static List<Pair> buildBestPairs(List<Participant> participantList, PairingWeights pairingWeights) {
        List<Pair> bestPairList = new ArrayList<>();

        while (participantList.size() >= 2) {
            Participant participant1 = participantList.remove(0);
            int bestPartnerPosition = -1;
            double bestPartnerScore = Double.NEGATIVE_INFINITY;

            for (int i = 0; i < participantList.size(); i++) {
                Participant testedParticipant = participantList.get(i);
                double score = calculatePairScore(participant1, testedParticipant, pairingWeights);

                if (score > bestPartnerScore) {
                    bestPartnerScore = score;
                    bestPartnerPosition = i;
                }
            }

            if (bestPartnerPosition == -1) {
                successors.add(participant1);
                continue;
            }

            Participant participant2 = participantList.remove(bestPartnerPosition);
            bestPairList.add(new Pair(participant1, participant2, false)); // TODO: Determine kitchen ownership if necessary
        }

        successors.addAll(participantList);
        return bestPairList;
    }

    /**
     * Calculates the score for pairing two participants based on pairing criteria and weights.
     *
     * @param participant1    the first participant
     * @param testedParticipant the second participant being tested as a potential pair
     * @param pairingWeights  the weights used for pairing criteria
     * @return the score for pairing the two participants
     */
    public static double calculatePairScore(Participant participant1, Participant testedParticipant, PairingWeights pairingWeights) {
        double score = 0;
        double kitchenScore = compareKitchen(participant1, testedParticipant);
        if (kitchenScore == Double.NEGATIVE_INFINITY) {
            return Double.NEGATIVE_INFINITY;
        }
        score += kitchenScore;
        score += compareGender(participant1, testedParticipant, pairingWeights);
        score += compareFoodPreference(participant1, testedParticipant, pairingWeights);
        score += compareAge(participant1, testedParticipant, pairingWeights);
        return score;
    }

    /**
     * Compares the kitchen availability of two participants.
     *
     * @param participant1    the first participant
     * @param testedParticipant the second participant being tested
     * @return the score based on the kitchen availability comparison
     */

    //TODO fix gleiche küche unmöglich
    private static double compareKitchen(Participant participant1, Participant testedParticipant) {
	    return switch (participant1.isHasKitchen()) {
		    case YES -> (testedParticipant.isHasKitchen() == KitchenAvailability.YES &&
				    participant1.getKitchen().equals(testedParticipant.getKitchen())) ? Double.NEGATIVE_INFINITY : 0;
		    case NO -> {
			    if (testedParticipant.isHasKitchen() == KitchenAvailability.YES) yield 0;
			    yield (testedParticipant.isHasKitchen() == KitchenAvailability.MAYBE) ? -50 : Double.NEGATIVE_INFINITY;
		    }
		    case MAYBE -> (testedParticipant.isHasKitchen() == KitchenAvailability.YES) ? 0 : -50;
		    default -> 0;
	    };
    }

    /**
     * Compares the gender of two participants based on pairing weights.
     *
     * @param participant1    the first participant
     * @param testedParticipant the second participant being tested
     * @param pairingWeights  the weights used for pairing criteria
     * @return the score based on the gender comparison
     */
    private static double compareGender(Participant participant1, Participant testedParticipant, PairingWeights pairingWeights) {
        return participant1.getGender().equals(testedParticipant.getGender()) ? 0 : 0.5 * pairingWeights.getGenderDifferenceWeight();
    }

    /**
     * Compares the food preference of two participants based on pairing weights.
     *
     * @param participant1    the first participant
     * @param testedParticipant the second participant being tested
     * @param pairingWeights  the weights used for pairing criteria
     * @return the score based on the food preference comparison
     */
    private static double compareFoodPreference(Participant participant1, Participant testedParticipant, PairingWeights pairingWeights) {
        double weight = 0.5 * pairingWeights.getFoodPreferenceWeight();
        switch (participant1.getFoodType()) {
            case MEAT:
                if (testedParticipant.getFoodType() == FoodType.MEAT) return weight;
                return (testedParticipant.getFoodType() == FoodType.NONE) ? weight : - 1000 * weight;
            case VEGGIE:
                if (testedParticipant.getFoodType() == FoodType.VEGGIE) return weight;
                if (testedParticipant.getFoodType() == FoodType.VEGAN) return 0.5 * weight;
                return (testedParticipant.getFoodType() == FoodType.NONE) ? 0.33 * weight : -1000 * weight;
            case VEGAN:
                if (testedParticipant.getFoodType() == FoodType.VEGAN) return weight;
                if (testedParticipant.getFoodType() == FoodType.VEGGIE) return 0.5 * weight;
                return (testedParticipant.getFoodType() == FoodType.NONE) ? 0.25 * weight : -1000 * weight;
            case NONE:
                return (testedParticipant.getFoodType() == FoodType.NONE || testedParticipant.getFoodType() == FoodType.MEAT) ? weight : 0.25 * weight;
            default:
                return 0;
        }
    }

    /**
     * Compares the age of two participants based on pairing weights.
     *
     * @param participant1    the first participant
     * @param testedParticipant the second participant being tested
     * @param pairingWeights  the weights used for pairing criteria
     * @return the score based on the age comparison
     */
    private static double compareAge(Participant participant1, Participant testedParticipant, PairingWeights pairingWeights) {
        double ageDifference = participant1.getAge().getAgeDifference(testedParticipant.getAge());
        return pairingWeights.getAgeDifferenceWeight() * (1 - 0.1 * ageDifference);
    }

    /**
     * Sorts participants based on their kitchen availability and food type.
     *
     * @param participantList the list of participants to be sorted
     * @return the sorted list of participants
     */
    private static List<Participant> sortParticipants(List<Participant> participantList) {
        List<Participant> sortedNoKitchenList = new ArrayList<>();
        List<Participant> sortedMaybeKitchenList = new ArrayList<>();
        List<Participant> sortedYesKitchenList = new ArrayList<>();

        for (Participant participant : participantList) {
            switch (participant.isHasKitchen()) {
                case NO:
                    sortedNoKitchenList.add(participant);
                    break;
                case MAYBE:
                    sortedMaybeKitchenList.add(participant);
                    break;
                case YES:
                    sortedYesKitchenList.add(participant);
                    break;
            }
        }

        List<Participant> sortedParticipantList = new ArrayList<>();
        sortedParticipantList.addAll(sortByFoodType(sortedNoKitchenList));
        sortedParticipantList.addAll(sortByFoodType(sortedMaybeKitchenList));
        sortedParticipantList.addAll(sortByFoodType(sortedYesKitchenList));

        return sortedParticipantList;
    }

    /**
     * Sorts participants based on their food type.
     *
     * @param participantList the list of participants to be sorted
     * @return the sorted list of participants
     */
    private static List<Participant> sortByFoodType(List<Participant> participantList) {
        List<Participant> sortedParticipantList = new ArrayList<>();

        for (Participant participant : participantList) {
            if (participant.getFoodType() == FoodType.VEGAN) {
                sortedParticipantList.add(participant);
            }
        }
        for (Participant participant : participantList) {
            if (participant.getFoodType() == FoodType.VEGGIE) {
                sortedParticipantList.add(participant);
            }
        }
        for (Participant participant : participantList) {
            if (participant.getFoodType() == FoodType.MEAT) {
                sortedParticipantList.add(participant);
            }
        }

        for (Participant participant : participantList) {
            if (participant.getFoodType() == FoodType.NONE) {
                sortedParticipantList.add(participant);
            }
        }

        return sortedParticipantList;
    }

    /**
     * Gets the list of unpaired participants.
     *
     * @return the list of unpaired participants
     */
    public List<Participant> getSuccessors() {
        return successors;
    }

    /**
     * Gets the list of pairs of participants.
     *
     * @return the list of pairs of participants
     */
    public List<Pair> getPairs() {
        return this;
    }

    /**
     * Derives the identifying number for the list of pairs.
     *
     * @return the identifying number for the list of pairs
     */
    private IdentNumber deriveIdentNumber() {
        return new PairIdentNumber(this);
    }

    /**
     * Gets the identifying number for this PairList.
     *
     * @return the identifying number for this PairList
     */
    @Override
    public IdentNumber getIdentNumber() {
        return identNumber;
    }

    /**
     * Evaluates the PairList.
     *
     * @return the evaluation score
     */
    @Override
    public double evaluate() {
        // TODO: Implement evaluation logic
        return 0;
    }

    /**
     * Returns a string representation of the PairList.
     *
     * @return a string representation of the PairList
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("PairList {\nPairs:\n");
        for (Pair pair : this) {
            sb.append(pair.toString()).append("\n");
        }
        sb.append("\nSuccessors:\n");
        for (Participant successor : successors) {
            sb.append(successor.toString()).append("\n");
        }
        sb.append("}");
        return sb.toString();
    }

    /**
     * @return the composition by foodtype of the PairList for debug purposes
     */
    public void printFoodNumbers(){
        int meatParticipants = 0;
        int noneParticipants = 0;
        int veggieParticipants = 0;
        int veganParticipants = 0;
        for (Pair pair : this){
            if (pair.getFoodType() == FoodType.MEAT){
                meatParticipants++;
            }
            if (pair.getFoodType() == FoodType.NONE){
                noneParticipants++;
            }
            if (pair.getFoodType() == FoodType.VEGGIE){
                veggieParticipants++;
            }
            if (pair.getFoodType() == FoodType.VEGAN){
                veganParticipants++;
            }
        }
        System.out.println("Meat: " + meatParticipants);
        System.out.println("None: " + noneParticipants);
        System.out.println("Veggie: " + veggieParticipants);
        System.out.println("Vegan: " + veganParticipants);
        System.out.println("Meat: " + meatParticipants);
        System.out.println("AllMeat: " + (meatParticipants + noneParticipants) + "divided by 9: " + ((meatParticipants + noneParticipants) / 9.0));
        System.out.println("AllVeggie: " + (veganParticipants + veggieParticipants));

    }
}
