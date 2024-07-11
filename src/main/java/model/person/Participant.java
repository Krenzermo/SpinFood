package model.person;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import model.event.collection.Group;
import model.event.collection.ParticipantCollection;
import model.event.collection.Pair;
import model.kitchen.Kitchen;
import model.event.Location;
import model.kitchen.KitchenAvailability;

import java.util.Objects;

/**
 * A simple class that holds information about a Participant.
 *
 * @author Daniel Hinkelmann
 * @author Davide Piacenza
 * @author Finn Brecher
 * @author Ole Krenzer
 */
public class Participant {

    private final String id;
    private final Name name;
    private final FoodType foodType;
    private final byte age;
    private final AgeRange ageRange;
    private final Gender gender;
    private KitchenAvailability hasKitchen;
    private Kitchen kitchen; // kitchen may be null
    private Pair pair;
    private Group[] groups = new Group[3];

    /**
     * Constructs a Participant with specified attributes.
     *
     * @param id               Participant ID.
     * @param name             Participant's name.
     * @param foodType         Participant's food type preference.
     * @param age              Participant's age.
     * @param gender           Participant's gender.
     * @param hasKitchen       Kitchen availability status.
     * @param kitchenStory     Kitchen story number.
     * @param kitchenLongitude Longitude of kitchen location.
     * @param kitchenLatitude  Latitude of kitchen location.
     */
    public Participant(String id, Name name, FoodType foodType, byte age, Gender gender, KitchenAvailability hasKitchen, int kitchenStory, double kitchenLongitude, double kitchenLatitude) {
        this.id = id;
        this.name = name;
        this.foodType = foodType;
        this.age = age;
        this.ageRange = AgeRange.getAgeRange(age);
        this.gender = gender;
        this.hasKitchen = hasKitchen;
        this.kitchen = new Kitchen(new Location(kitchenLongitude, kitchenLatitude), kitchenStory);
        this.pair = null;
    }

    /**
     * Constructs a Participant with specified attributes and default kitchen status (no kitchen).
     *
     * @param id       Participant ID.
     * @param name     Participant's name.
     * @param foodType Participant's food type preference.
     * @param age      Participant's age.
     * @param gender   Participant's gender.
     */
    public Participant(String id, Name name, FoodType foodType, byte age, Gender gender) {
        this.id = id;
        this.name = name;
        this.foodType = foodType;
        this.age = age;
        this.ageRange = AgeRange.getAgeRange(age);
        this.gender = gender;
        this.hasKitchen = KitchenAvailability.NO;
        this.kitchen = null;
        this.pair = null;
    }

    /**
     * Copy-Constructor for class {@link Participant}.
     * This constructor copies all fields but not the {@link Pair} or {@link Group} class information.
     *
     * @param participant the specified {@link Participant}
     */
    public Participant(Participant participant) {
	    this.id = participant.id;
	    this.name = participant.name;
	    this.foodType = participant.foodType;
	    this.ageRange = participant.ageRange;
	    this.gender = participant.gender;
	    this.hasKitchen = participant.hasKitchen;
	    this.kitchen = participant.kitchen;
	    this.age = participant.getAge();
    }
	/*
	public Participant(Participant participant) {
        this(participant.getId(), participant.getName(), participant.getFoodType(), participant.getAge(), participant.getGender());
    }
	*/

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
        if (Objects.isNull(kitchen)) {
            if (other.kitchen != null) {
                return false;
            }
            return this.id.equals(other.id) && this.name.equals(other.name) && this.foodType.equals(other.foodType) && this.ageRange.equals(other.ageRange)
                    && this.gender.equals(other.gender) && this.hasKitchen.equals(other.hasKitchen);
        }
        return this.id.equals(other.id) && this.name.equals(other.name) && this.foodType.equals(other.foodType) && this.ageRange.equals(other.ageRange)
                && this.gender.equals(other.gender) && this.hasKitchen.equals(other.hasKitchen) && this.kitchen.equals(other.kitchen);
    }


    /**
     * Returns a string representation of this Participant.
     *
     * @return A string representation of this Participant.
     */
    @Override
    public String toString() {
        //Don't call kitchen.toString() explicitly as kitchen may be null and this would yield a NullPointerException
        return "ID: " + id + ", Name: " + name.toString() + ", " + foodType + ", " + ageRange + ", " + gender + ", " + hasKitchen + ", " + kitchen;
    }

    /**
     * Compares this Participant's assigned kitchen with another Participant's assigned kitchen.
     *
     * @param o The other Participant to compare.
     * @return A double value representing the comparison result.
     */
    public double compareTo(Participant o) {
        return this.kitchen.compareTo(o.kitchen);
    }

    /**
     * Returns the age of the participant.
     *
     * @return The age of the participant.
     */
    public byte getAge() {
        return age;
    }

    /**
     * Returns the age range category of the participant.
     *
     * @return The age range category of the participant.
     */
    public AgeRange getAgeRange() {
        return ageRange;
    }

    /**
     * Returns the ID of the participant.
     *
     * @return The ID of the participant.
     */
    public String getId() {
        return id;
    }

    /**
     * Returns an observable value of the participant's ID.
     *
     * @return Observable value representing the participant's ID.
     */
    public ObservableValue<String> getIdAsObservable() {
        return new SimpleStringProperty(id);
    }

    /**
     * Returns the name of the participant.
     *
     * @return The name of the participant.
     */
    public Name getName() {
        return name;
    }

    /**
     * Returns the food type preference of the participant.
     *
     * @return The food type preference of the participant.
     */
    public FoodType getFoodType() {
        return foodType;
    }

    /**
     * Returns the gender of the participant.
     *
     * @return The gender of the participant.
     */
    public Gender getGender() {
        return gender;
    }

    /**
     * Returns the availability of a kitchen for the participant.
     *
     * @return {@link KitchenAvailability#YES} if there is a kitchen,
     * {@link KitchenAvailability#NO} if there is no kitchen,
     * {@link KitchenAvailability#MAYBE} if there may be a kitchen.
     */
    public KitchenAvailability isHasKitchen() {
        return hasKitchen;
    }

    /**
     * Returns the kitchen assigned to the participant.
     *
     * @return The kitchen assigned to the participant, or null if no kitchen is assigned.
     */
    public Kitchen getKitchen() {
        return kitchen;
    }

    /**
     * Returns the pair this participant belongs to.
     *
     * @return The pair this participant belongs to, or null if not assigned to any pair.
     */
    public Pair getPair() {
        return pair;
    }

    /**
     * Returns the groups this participant belongs to.
     *
     * @return The array of groups this participant belongs to.
     */
    public Group[] getGroups() {
        return groups;
    }

    /**
     * Sets the pair this participant belongs to.
     *
     * @param pair The pair to set for this participant.
     * @throws RuntimeException If the pair does not contain this participant.
     */
    public void setPair(Pair pair) {
        if (!pair.contains(this))
            throw new RuntimeException("The pair does not contain this participant. " + "Pair: " + pair + ", Participant: " + this);

        this.pair = pair;
    }

    /**
     * Sets the groups this participant belongs to.
     *
     * @param groups The array of groups to set for this participant.
     * @throws RuntimeException If no pair is assigned to this participant.
     * @throws RuntimeException If any group does not contain the participant's pair.
     * @throws RuntimeException If the number of groups is not exactly 3.
     */
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

    /**
     * Sets the kitchen availability status to NO and clears the assigned kitchen.
     */
    public void setNoKitchen() {
        this.hasKitchen = KitchenAvailability.NO;
        this.kitchen = null;
    }

    /**
     * Clears the assigned pair for this participant.
     */
    public void clearPair() {
        this.pair = null;
    }
}
