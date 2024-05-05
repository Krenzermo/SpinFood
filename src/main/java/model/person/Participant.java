package model.person;

import model.Kitchen;
import model.Location;

public class Participant implements Comparable<Participant>{

    private final String id;
    private final Name name;
    private final FoodType foodType;
    private final AgeRange ageRange;
    private final Gender gender;
    private final Boolean hasKitchen;
    private final Kitchen kitchen;

    public Participant(String id, Name name, FoodType foodType, byte age, Gender gender, Boolean hasKitchen, int kitchenStory, double kitchenLongitude, double kitchenLatitude) {
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
        this.hasKitchen = false;
        this.kitchen = null;
    }

    @Override
    public String toString() {
        return "ID: " + id + ", Name: " + name.toString() + ", FoodType: " + foodType + ", Age: " + ageRange + ", Gender: " + gender + ", HasKitchen: " + hasKitchen + ", Kitchen: " + kitchen.toString();
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
    public Boolean isHasKitchen() {
        return hasKitchen;
    }

    public Kitchen getKitchen() {
        return kitchen;
    }
}
