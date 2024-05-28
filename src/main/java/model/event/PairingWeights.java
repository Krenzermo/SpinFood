package model.event;

/**
 * This class represents the weights used in pairing participants based on various criteria.
 * It includes weights for age difference, gender difference, and food preference.
 *
 * @author Krenzerm
 */
public class PairingWeights {
    private double pairAgeDifferenceWeight;
    private double pairGenderDifferenceWeight;
    private double pairFoodPreferenceWeight;

    /**
     * Constructs a new PairingWeights object with the specified weights.
     *
     * @param pairAgeDifferenceWeight the weight for age difference
     * @param pairGenderDifferenceWeight the weight for gender difference
     * @param pairFoodPreferenceWeight the weight for food preference
     */
    public PairingWeights(double pairAgeDifferenceWeight, double pairGenderDifferenceWeight, double pairFoodPreferenceWeight) {
        this.pairAgeDifferenceWeight = pairAgeDifferenceWeight;
        this.pairGenderDifferenceWeight = pairGenderDifferenceWeight;
        this.pairFoodPreferenceWeight = pairFoodPreferenceWeight;
    }

    /**
     * Gets the weight for age difference.
     *
     * @return the weight for age difference
     */
    public double getPairAgeDifferenceWeight() {
        return pairAgeDifferenceWeight;
    }

    /**
     * Sets the weight for age difference.
     *
     * @param pairAgeDifferenceWeight the new weight for age difference
     */
    public void setPairAgeDifferenceWeight(double pairAgeDifferenceWeight) {
        this.pairAgeDifferenceWeight = pairAgeDifferenceWeight;
    }

    /**
     * Gets the weight for gender difference.
     *
     * @return the weight for gender difference
     */
    public double getPairGenderDifferenceWeight() {
        return pairGenderDifferenceWeight;
    }

    /**
     * Sets the weight for gender difference.
     *
     * @param pairGenderDifferenceWeight the new weight for gender difference
     */
    public void setPairGenderDifferenceWeight(double pairGenderDifferenceWeight) {
        this.pairGenderDifferenceWeight = pairGenderDifferenceWeight;
    }

    /**
     * Gets the weight for food preference.
     *
     * @return the weight for food preference
     */
    public double getPairFoodPreferenceWeight() {
        return pairFoodPreferenceWeight;
    }

    /**
     * Sets the weight for food preference.
     *
     * @param pairFoodPreferenceWeight the new weight for food preference
     */
    public void setPairFoodPreferenceWeight(double pairFoodPreferenceWeight) {
        this.pairFoodPreferenceWeight = pairFoodPreferenceWeight;
    }
}