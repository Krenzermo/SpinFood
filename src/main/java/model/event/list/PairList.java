package model.event.list;

import model.event.InputData;
import model.event.PairingWeights;
import model.event.collection.Pair;
import model.event.collection.ParticipantCollection;
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
 */
public class PairList implements ParticipantCollectionList {
    private IdentNumber identNumber;
    private final ArrayList<Pair> pairs;
    private final ArrayList<Participant> successors = new ArrayList<>();

    /**
     * Constructs a PairList object by sorting participants and building the best pairs.
     *
     * @param inputData      the input data containing participant and pair information
     * @param pairingWeights the weights used for pairing criteria
     */
    public PairList(InputData inputData, PairingWeights pairingWeights) {
        ArrayList<Participant> sortedParticipantList = sortParticipants(inputData.getParticipantInputData());
        this.pairs = buildBestPairs(sortedParticipantList, pairingWeights);
        this.pairs.addAll(inputData.getPairInputData());
        this.identNumber = deriveIdentNumber();
    }

    /**
     * Builds the best pairs of participants based on pairing criteria and weights.
     *
     * @param participantList the list of participants to be paired
     * @param pairingWeights  the weights used for pairing criteria
     * @return a list of the best pairs of participants
     */
    private ArrayList<Pair> buildBestPairs(ArrayList<Participant> participantList, PairingWeights pairingWeights) {
        ArrayList<Pair> bestPairList = new ArrayList<>();

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
    private double calculatePairScore(Participant participant1, Participant testedParticipant, PairingWeights pairingWeights) {
        double score = 0;
        score += compareKitchen(participant1, testedParticipant);
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
    private double compareKitchen(Participant participant1, Participant testedParticipant) {
        switch (participant1.isHasKitchen()) {
            case YES:
                return (testedParticipant.isHasKitchen() == KitchenAvailability.YES &&
                        participant1.getKitchen().equals(testedParticipant.getKitchen())) ? -1000 : 0;
            case NO:
                if (testedParticipant.isHasKitchen() == KitchenAvailability.YES) return 0;
                return (testedParticipant.isHasKitchen() == KitchenAvailability.MAYBE) ? -50 : -1000;
            case MAYBE:
                return (testedParticipant.isHasKitchen() == KitchenAvailability.YES) ? 0 : -50;
            default:
                return 0;
        }
    }

    /**
     * Compares the gender of two participants based on pairing weights.
     *
     * @param participant1    the first participant
     * @param testedParticipant the second participant being tested
     * @param pairingWeights  the weights used for pairing criteria
     * @return the score based on the gender comparison
     */
    private double compareGender(Participant participant1, Participant testedParticipant, PairingWeights pairingWeights) {
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
    private double compareFoodPreference(Participant participant1, Participant testedParticipant, PairingWeights pairingWeights) {
        double weight = pairingWeights.getFoodPreferenceWeight();
        switch (participant1.getFoodType()) {
            case MEAT:
                if (testedParticipant.getFoodType() == FoodType.MEAT) return weight;
                return (testedParticipant.getFoodType() == FoodType.NONE) ? 0.5 * weight : -1000;
            case VEGGIE:
                if (testedParticipant.getFoodType() == FoodType.VEGGIE) return weight;
                if (testedParticipant.getFoodType() == FoodType.VEGAN) return 0.5 * weight;
                return (testedParticipant.getFoodType() == FoodType.NONE) ? 0.25 * weight : -1000;
            case VEGAN:
                if (testedParticipant.getFoodType() == FoodType.VEGAN) return weight;
                if (testedParticipant.getFoodType() == FoodType.VEGGIE) return 0.5 * weight;
                return (testedParticipant.getFoodType() == FoodType.NONE) ? 0.25 * weight : -1000;
            case NONE:
                return (testedParticipant.getFoodType() == FoodType.NONE || testedParticipant.getFoodType() == FoodType.MEAT) ? 0.5 * weight : 0.25 * weight;
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
    private double compareAge(Participant participant1, Participant testedParticipant, PairingWeights pairingWeights) {
        double ageDifference = participant1.getAge().getAgeDifference(testedParticipant.getAge());
        return pairingWeights.getAgeDifferenceWeight() * (1 - 0.1 * ageDifference);
    }

    /**
     * Sorts participants based on their kitchen availability and food type.
     *
     * @param participantList the list of participants to be sorted
     * @return the sorted list of participants
     */
    private ArrayList<Participant> sortParticipants(ArrayList<Participant> participantList) {
        ArrayList<Participant> sortedNoKitchenList = new ArrayList<>();
        ArrayList<Participant> sortedMaybeKitchenList = new ArrayList<>();
        ArrayList<Participant> sortedYesKitchenList = new ArrayList<>();

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

        ArrayList<Participant> sortedParticipantList = new ArrayList<>();
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
    private ArrayList<Participant> sortByFoodType(ArrayList<Participant> participantList) {
        ArrayList<Participant> sortedParticipantList = new ArrayList<>();

        for (Participant participant : participantList) {
            if (participant.getFoodType() != FoodType.NONE) {
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
    public ArrayList<Participant> getSuccessors() {
        return successors;
    }

    /**
     * Gets the list of pairs of participants.
     *
     * @return the list of pairs of participants
     */
    public ArrayList<Pair> getPairs() {
        return pairs;
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
     * Gets the identifying number for this ParticipantCollectionList.
     *
     * @return the identifying number for this ParticipantCollectionList
     */
    @Override
    public IdentNumber getIdentNumber() {
        return identNumber;
    }

    /**
     * Evaluates the ParticipantCollection.
     *
     * @return the evaluation score
     */
    @Override
    public double evaluate() {
        // TODO: Implement evaluation logic
        return 0;
    }

    /**
     * Gets the data structure that stores the instances of ParticipantCollection.
     *
     * @return the data structure that stores the instances of ParticipantCollection
     */
    @Override
    public List<ParticipantCollection> getDataStructure() {
        // TODO: Implement the data structure for ParticipantCollection
        return null;
    }

    /**
     * Checks if the collection has the same type as this ParticipantCollectionList.
     *
     * @param collection the element to be checked
     * @throws IllegalArgumentException if the type check fails
     */
    @Override
    public void checkType(ParticipantCollection collection) {
        if (!(collection instanceof Pair)) {
            throw new IllegalArgumentException("Collection is not a Pair");
        }
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
