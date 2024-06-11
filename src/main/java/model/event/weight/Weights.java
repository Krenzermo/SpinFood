package model.event.weight;

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

    public Weights(double ageDifferenceWeight, double genderDifferenceWeight, double foodPreferenceWeight) {
        this.ageDifferenceWeight = ageDifferenceWeight;
        this.genderDifferenceWeight = genderDifferenceWeight;
        this.foodPreferenceWeight = foodPreferenceWeight;
    }

    public double getAgeDifferenceWeight() {
        return ageDifferenceWeight;
    }

    public void setAgeDifferenceWeight(double ageDifferenceWeight) {
        this.ageDifferenceWeight = ageDifferenceWeight;
    }

    public double getGenderDifferenceWeight() {
        return genderDifferenceWeight;
    }

    public void setGenderDifferenceWeight(double genderDifferenceWeight) {
        this.genderDifferenceWeight = genderDifferenceWeight;
    }

    public double getFoodPreferenceWeight() {
        return foodPreferenceWeight;
    }

    public void setFoodPreferenceWeight(double foodPreferenceWeight) {
        this.foodPreferenceWeight = foodPreferenceWeight;
    }
}
