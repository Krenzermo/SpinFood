package model.person;

import model.Kitchen;

public class Participant {

    public final String id;
    public final Name name;
    public final FoodType foodType;
    public final AgeRange ageRange;
    public final Gender gender;
    public final boolean hasKitchen;
    public final Kitchen kitchen;

    public Participant(String id, Name name, FoodType foodType, byte age, Gender gender, boolean hasKitchen, int kitchenStory, double kitchenLongitude, double kitchenLatitude) {
        this.id = id;
        this.name = name;
        this.foodType = foodType;
        this.ageRange = AgeRange.getAgeRange(age);
        this.gender = gender;
        this.hasKitchen = hasKitchen;
        this.kitchen = new Kitchen(kitchenLongitude, kitchenLatitude, kitchenStory);

    }

    @Override
    public String toString() {
        return "ID: " + id + ", Name: " + name.toString() + ", FoodType: " + foodType + ", Age: " + ageRange + ", Gender: " + gender + ", HasKitchen: " + hasKitchen + ", Kitchen: " + kitchen.toString();
    }

    public AgeRange getAge() {
        return ageRange;
    }

}
