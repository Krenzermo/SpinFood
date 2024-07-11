package model.event.list.weight;

/**
 * This class represents the weights used in grouping participants based on various criteria specific to groups.
 * It includes weights for age difference, gender difference, food preference, and distance.
 *
 * It extends the abstract class Weights.
 *
 * @author Davide Piacenza
 */
public class GroupWeights extends Weights {
    private double distanceWeight;

    public GroupWeights(double groupAgeDifferenceWeight, double groupGenderDifferenceWeight, double groupFoodPreferenceWeight, double distanceWeight) {
        super(groupAgeDifferenceWeight, groupGenderDifferenceWeight, groupFoodPreferenceWeight);
        this.distanceWeight = distanceWeight;
    }

    /**
     * Copy constructor
     * @param weights The weights to copy
     */
    public GroupWeights(GroupWeights weights) {
        super(weights.ageDifferenceWeight, weights.genderDifferenceWeight, weights.foodPreferenceWeight);
        distanceWeight = weights.distanceWeight;
    }

    /**
     * Gets the weight for distance.
     *
     * @return the weight for distance
     */
    public double getDistanceWeight() {
        return distanceWeight;
    }

    /**
     * Sets the weight for distance.
     *
     * @param distanceWeight the new weight for distance
     */
    public void setDistanceWeight(double distanceWeight) {
        this.distanceWeight = distanceWeight;
    }


}
