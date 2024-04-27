package model;

public class Participant {

    private final String id;
    private final Name name;
    private final FoodType foodType;
    private final AgeRange ageRange;
    private final Gender gender;
    private final boolean hasKitchen;
    private final Kitchen kitchen;

    public Participant(String id, Name name, FoodType foodType, byte age, Gender gender, boolean hasKitchen, int kitchenStory, double kitchenLongitude, double kitchenLatitude) {
        this.id = id;
        this.name = name;
        this.foodType = foodType;
        this.ageRange = AgeRange.calcRange(age);
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
