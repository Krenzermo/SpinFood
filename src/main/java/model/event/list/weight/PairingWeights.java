package model.event.list.weight;

/**
 * This class represents the weights used in pairing participants based on various criteria specific to pairs.
 * It includes weights for age difference, gender difference, and food preference.
 *
 * It extends the abstract class Weights.
 *
 * @author Krenzerm
 */
public class PairingWeights extends Weights {

    public PairingWeights(double pairAgeDifferenceWeight, double pairGenderDifferenceWeight, double pairFoodPreferenceWeight) {
        super(pairAgeDifferenceWeight, pairGenderDifferenceWeight, pairFoodPreferenceWeight);
    }

}
