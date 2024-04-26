package model;

public class Participant {

    private String id;
    private String name;
    private FoodType foodType;
    private AgeRange ageRange;
    private Gender gender;
    private boolean hasKitchen;
    private Kitchen kitchen;

    public Participant(String id, String name, FoodType foodType, byte age, Gender gender, boolean hasKitchen, int kitchenStory, double kitchenLongitude, double kitchenLatitude) {
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
        return "ID: " + id + ", Name: " + name + ", FoodType: " + foodType + ", Age: " + ageRange + ", Gender: " + gender + ", HasKitchen: " + hasKitchen + ", Kitchen: " + kitchen.toString();
    }

    public AgeRange getAge() {
        return ageRange;
    }

}
