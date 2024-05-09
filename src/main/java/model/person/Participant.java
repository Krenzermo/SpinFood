package model.person;

import model.kitchen.Kitchen;
import model.Location;
import model.kitchen.KitchenAvailability;

public class Participant implements Comparable<Participant>{

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
    public String toString() {
        //Don't call kitchen.toString explicitly
        return "ID: " + id + ", Name: " + name.toString() + ", FoodType: " + foodType + ", Age: " + ageRange + ", Gender: " + gender + ", HasKitchen: " + hasKitchen.toString() + ", Kitchen: " + kitchen;
    }

    @Override
    public int compareTo(Participant o) {
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
