package model.event.list.weight;

/**
 * This abstract class represents the weights used in various pairings and groupings.
 * It includes common weights for age difference, gender difference, and food preference.
 * Specific classes will extend this to add more specific criteria.
 *
 * @author Davide Piacenza
 */
public abstract class Weights {
    protected double ageDifferenceWeight;
    protected double genderDifferenceWeight;
    protected double foodPreferenceWeight;

    /**
     * Constructs a Weights object with specified weights for age difference,
     * gender difference, and food preference.
     *
     * @param ageDifferenceWeight Weight for age difference.
     * @param genderDifferenceWeight Weight for gender difference.
     * @param foodPreferenceWeight Weight for food preference.
     */
    public Weights(double ageDifferenceWeight, double genderDifferenceWeight, double foodPreferenceWeight) {
        this.ageDifferenceWeight = ageDifferenceWeight;
        this.genderDifferenceWeight = genderDifferenceWeight;
        this.foodPreferenceWeight = foodPreferenceWeight;
    }

    /**
     * Retrieves the weight for age difference.
     *
     * @return The weight for age difference.
     */
    public double getAgeDifferenceWeight() {
        return ageDifferenceWeight;
    }

    /**
     * Sets the weight for age difference.
     *
     * @param ageDifferenceWeight The weight for age difference to set.
     */
    public void setAgeDifferenceWeight(double ageDifferenceWeight) {
        this.ageDifferenceWeight = ageDifferenceWeight;
    }

    /**
     * Retrieves the weight for gender difference.
     *
     * @return The weight for gender difference.
     */
    public double getGenderDifferenceWeight() {
        return genderDifferenceWeight;
    }

    /**
     * Sets the weight for gender difference.
     *
     * @param genderDifferenceWeight The weight for gender difference to set.
     */
    public void setGenderDifferenceWeight(double genderDifferenceWeight) {
        this.genderDifferenceWeight = genderDifferenceWeight;
    }

    /**
     * Retrieves the weight for food preference.
     *
     * @return The weight for food preference.
     */
    public double getFoodPreferenceWeight() {
        return foodPreferenceWeight;
    }

    /**
     * Sets the weight for food preference.
     *
     * @param foodPreferenceWeight The weight for food preference to set.
     */
    public void setFoodPreferenceWeight(double foodPreferenceWeight) {
        this.foodPreferenceWeight = foodPreferenceWeight;
    }
}