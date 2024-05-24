package model.person;

import model.event.collection.Group;
import model.event.collection.ParticipantCollection;
import model.event.collection.Pair;
import model.kitchen.Kitchen;
import model.event.Location;
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
    private final Kitchen kitchen; // kitchen may be null
    private Pair pair;
    private Group[] groups = new Group[3];

    public Participant(String id, Name name, FoodType foodType, byte age, Gender gender, KitchenAvailability hasKitchen, int kitchenStory, double kitchenLongitude, double kitchenLatitude) {
        this.id = id;
        this.name = name;
        this.foodType = foodType;
        this.ageRange = AgeRange.getAgeRange(age);
        this.gender = gender;
        this.hasKitchen = hasKitchen;
        this.kitchen = new Kitchen(new Location(kitchenLongitude, kitchenLatitude), kitchenStory);
        this.pair = null;
        this.groups = null;
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
        //Don't call kitchen.toString() explicitly as kitchen may be null and this would yield a NullPointerException
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
     * @return  {@link KitchenAvailability#YES} if there is a kitchen,
     *          {@link KitchenAvailability#NO} if there is no kitchen
     *          and {@link KitchenAvailability#MAYBE} if there may be a kitchen.
     */
    public KitchenAvailability isHasKitchen() {
        return hasKitchen;
    }

    public Kitchen getKitchen() {
        return kitchen;
    }

    public Pair getPair() {
        return pair;
    }

    public Group[] getGroups() {
        return groups;
    }

    public void setPair(Pair pair) {
        if (!pair.contains(this))
            throw new RuntimeException("The pair does not contain this participant. " + "Pair: " + pair + ", Participant: " + this);

        this.pair = pair;
    }

    public void setGroups(Group[] groups) {
        if (pair == null) {
            throw new RuntimeException("cannot assign Groups to this Participant if no Pair is assigned.");
        }

        for (Group group: groups)  {
            // TODO: may need to be changed once Group is implemented.
            if (!group.contains(pair)) {
                throw new RuntimeException("cannot assign a Group to this Participant if the Group does not contain the Participants Pair.");
            }
        }

        if (groups.length != 3) {
            throw new RuntimeException("Groups must have exactly 3 pairs!");
        }
        this.groups = groups;
    }
}
