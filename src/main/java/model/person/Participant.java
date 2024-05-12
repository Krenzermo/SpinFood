package model.person;

import model.Pair;
import model.kitchen.Kitchen;
import model.Location;
import model.kitchen.KitchenAvailability;

import java.util.Objects;

/**
 * @author Daniel Hinkelmann
 * @author Davide Piacenza
 * @author Finn Brecher
 * @author Ole Krenzer
 */
public class Participant {

    private final String id;
    private final Name name;
    private final FoodType foodType;
    private final AgeRange ageRange;
    private final Gender gender;
    private final KitchenAvailability hasKitchen;
    private final Kitchen kitchen;

    public Participant(String id, Name name, FoodType foodType, byte age, Gender gender, KitchenAvailability hasKitchen, int kitchenStory, double kitchenLongitude, double kitchenLatitude) {
        this.id = id;
        this.name = name;
        this.foodType = foodType;
        this.ageRange = AgeRange.getAgeRange(age);
        this.gender = gender;
        this.hasKitchen = hasKitchen;
        this.kitchen = new Kitchen(new Location(kitchenLongitude, kitchenLatitude), kitchenStory);

    }

    public Participant(String id, Name name, FoodType foodType, byte age, Gender gender) {
        this.id = id;
        this.name = name;
        this.foodType = foodType;
        this.ageRange = AgeRange.getAgeRange(age);
        this.gender = gender;
        this.hasKitchen = KitchenAvailability.NO;
        this.kitchen = null;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, foodType, ageRange, gender, hasKitchen, kitchen);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Participant other = (Participant) obj;
        return this.id.equals(other.id) && this.name.equals(other.name) && this.foodType.equals(other.foodType) && this.ageRange.equals(other.ageRange)
                && this.gender.equals(other.gender) && this.hasKitchen.equals(other.hasKitchen) && this.kitchen.equals(other.kitchen);
    }

    @Override
    public String toString() {
        //Don't call kitchen.toString explicitly
        return "@Participant{ID: " + id + ", Name: " + name.toString() + ", " + foodType + ", " + ageRange + ", " + gender + ", " + hasKitchen + ", " + kitchen + "}";
    }


    public double compareTo(Participant o) {
        return this.kitchen.compareTo(o.kitchen);
    }

    public AgeRange getAge() {
        return ageRange;
    }

    public String getId() {
        return id;
    }

    public Name getName() {
        return name;
    }

    public FoodType getFoodType() {
        return foodType;
    }

    public AgeRange getAgeRange() {
        return ageRange;
    }

    public Gender getGender() {
        return gender;
    }

    /**
     * @return true if hasKitchen, false if not hasKitchen, null if maybe hasKitchen
     */
    public KitchenAvailability isHasKitchen() {
        return hasKitchen;
    }

    public Kitchen getKitchen() {
        return kitchen;
    }
}
