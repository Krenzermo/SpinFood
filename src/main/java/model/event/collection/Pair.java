package model.event.collection;

import javafx.beans.property.SimpleStringProperty;
import model.event.Location;
import model.event.list.identNumbers.IdentNumber;
import model.kitchen.Kitchen;
import model.kitchen.KitchenAvailability;
import model.person.FoodType;
import model.person.Gender;
import model.person.Participant;
import model.event.Course;
import model.event.io.InputData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/** This class represents one Pair of {@link Participant} that complies with the defined rules.
 *
 * @author Finn Brecher
 * @author Davide Piacenza
 * @author Daniel Hinkelmann
 */
public class Pair implements ParticipantCollection {

    private int id;
    private final Participant[] participants = new Participant[2];
    private Kitchen kitchen;
    private boolean kitchenOf;
    private Course course; //TODO: delete dummy value for course when implemented correctly
    private Group[] groups = new Group[3];
    private FoodType foodType;
    private int starterNumber;
    private int mainNumber;
    private int dessertNumber;
    public final boolean signedUpTogether;

    private static int COUNTER = 0;
    private static final InputData inputData = InputData.getInstance();

    public Pair(Participant participant1, Participant participant2) {
        this(participant1, participant2, false);
    }

    public Pair(Participant participant1, Participant participant2, boolean signedUpTogether) {
        id = COUNTER++;
        participants[0] = participant1;
        participants[1] = participant2;
        this.signedUpTogether = signedUpTogether;
        this.kitchen = autoAssignKitchen();
        this.foodType = autoAssignFoodType();
    }

    private FoodType autoAssignFoodType() {
        FoodType foodType1 = participants[0].getFoodType();
        FoodType foodType2 = participants[1].getFoodType();
        List<FoodType> list = List.of(foodType1, foodType2);
        if (foodType1 == foodType2) {
            return foodType1;
        }

        if (hasOnlyCarni(list)) {
            return FoodType.MEAT;
        }

        int value = list.stream().mapToInt(FoodType::getValue).max().getAsInt();
        return FoodType.herbiFromValue(value);
    }



    private boolean hasOnlyCarni(List<FoodType> list) {
        return !(list.contains(FoodType.VEGGIE) || list.contains(FoodType.VEGAN));
    }

    @Override
    public IdentNumber getIdentNumber() {
        // TODO: this
        return null;
    }

    @Override
    public List<Participant> getParticipants() {
        // TODO: maybe return a modifiable List instead
        return List.of(participants);
    }

    @Override
    public Kitchen getKitchen() {
        return kitchen;
    }

    @Override
    public double evaluate() {
        //TODO: this
        return 0;
    }

    @Override
    public int getAgeDifference() {
        return participants[0].getAge().getAgeDifference(participants[1].getAge());
    }

    @Override
    public Course getCourse() {
        return course;
    }

    public List<Group> getGroups() {
        return Arrays.asList(groups);
    }

    public void setKitchen(Kitchen kitchen) {
        this.kitchen = kitchen;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public void setGroups(Group[] groups) {
        for (Group group: groups)  {
            // TODO: may need to be changed once Group is implemented.
            if (!Arrays.asList(group.getPairs()).contains(this)) {
                throw new RuntimeException("cannot assign a Group to this Pair if the group does not contain the Pair");
            }
        }

        if (groups.length != 3) {
            throw new RuntimeException("Groups must have exactly 3 pairs!");
        }

        this.groups = groups;
    }

    /**
     * Clears this {@link Pair} of the instances of {@link Group}.
     */
    public void clearGroups() {
        groups = new Group[] {null, null, null};
    }

    /**
     * Automatically assigns a kitchen to this Pair.
     * When it is not clear whose kitchen should be chosen,
     * the on closer to {@link InputData#getEventLocation()} gets chosen.
     *
     * @return the kitchen this Pair was assigned
     */
    private Kitchen autoAssignKitchen() {
        if (KitchenAvailability.NO.equals(participants[0].isHasKitchen()) && KitchenAvailability.NO.equals(participants[1].isHasKitchen())) {
            throw new RuntimeException("No kitchen assigned to either participant!");
        }

        // assign kitchen if signedUpTogether or one does not have a kitchen
        if (signedUpTogether) {
            kitchenOf = false;
            return participants[0].getKitchen();
        }
        if (KitchenAvailability.NO.equals(participants[0].isHasKitchen())) {
            kitchenOf = true;
            return participants[1].getKitchen();
        }
        if (KitchenAvailability.NO.equals(participants[1].isHasKitchen())) {
            kitchenOf = false;
            return participants[0].getKitchen();
        }

        // if exactly one participant maybe has a kitchen
        if (KitchenAvailability.MAYBE.equals(participants[0].isHasKitchen()) ^ KitchenAvailability.MAYBE.equals(participants[1].isHasKitchen())) {
            if (KitchenAvailability.YES.equals(participants[0].isHasKitchen())) {
                kitchenOf = false;
                return participants[0].getKitchen();
            }
            if (KitchenAvailability.YES.equals(participants[1].isHasKitchen())) {
                kitchenOf = true;
                return participants[1].getKitchen();
            }

            if (KitchenAvailability.MAYBE.equals(participants[0].isHasKitchen())) {
                kitchenOf = false;
                return participants[0].getKitchen();
            } else {
                kitchenOf = true;
                return participants[1].getKitchen();
            }
        }

        // here if both have a kitchen or both maybe have a kitchen

        Location eventLocation = inputData.getEventLocation();

        // select the one closest to the eventLocation
        if (participants[0].getKitchen().location().getDistance(eventLocation) <= participants[1].getKitchen().location().getDistance(eventLocation)) {
            return participants[0].getKitchen();
        }

        return participants[1].getKitchen();
    }

    /**
     * @param participant element whose presence in this collection is to be ensured
     * @return {@code true} if the operation was successful, {@code false} otherwise
     * @throws IllegalArgumentException if this ParticipantCollection already contains the Element
     * @throws NullPointerException if the element is null
     */
    @Override
    public boolean add(Participant participant) {
        if (participant == null) {
            throw new NullPointerException("Participant must not be null");
        }
        if (contains(participant)) {
            throw new IllegalArgumentException("This Pair already contains participant " + participant);
        }
        //TODO: this
        return false;
    }

    @Override
    public boolean remove(Object o) {
        if (!(o instanceof Participant)) {
            return false;
        }
        //TODO: this
        return false;
    }

    @Override
    public Participant set(int index, Participant participant) {
        // TODO: maybe this

        return null;
    }

    @Override
    public int hashCode() {
        return Objects.hash(participants[0], participants[1]);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Pair pair = (Pair) obj;
        return (participants[0].equals(pair.participants[0]) && participants[1].equals(pair.participants[1]))
                || (participants[0].equals(pair.participants[1]) && participants[1].equals(pair.participants[0]));
    }

    @Override
    public String toString() {
        return "{Participant1: " + participants[0] + " Participant2: " + participants[1] + kitchen + "}";
    }

    /** Creates an Output String for this pair object in the following format:
     * <p>- Name of Participant 1 </p>
     * <p> - Name of Participant 2 </p>
     * <p> - Signed up together? </p>
     * <p> - Longitude of Kitchen </p>
     * <p> - Latitude of Kitchen </p>
     * <p> - Food Preference </p>
     * <p> - Pair number </p>
     * <p> - Starter Group number </p>
     * <p> - Main Group number </p>
     * <p> - Dessert Group number </p>
     * <p> - Who owns the kitchen? </p>
     * <p> - Cooking course </p>
     *
     * @return A String of a Pair in the output Format
     */
    public String asOutputString() {
        int signedUpTogether = this.signedUpTogether ? 1 : 0;
        String s = course == null ? "NV" : String.valueOf(course.getAsInt());
        return participants[0].getName().asOutputString() + ";" + participants[1].getName().asOutputString() + ";" + signedUpTogether + ";" + kitchen.asOutputString() + ";" + foodType.getOtherName() + ";" + id + ";" + starterNumber + ";" + mainNumber + ";" + dessertNumber + ";" + kitchenOf + ";" + s;
    }

    /** Calculates the deviation of Food preferences of the Participants of this Pair
     *
     * @return The Preference deviation
     */
    public int getPreferenceDeviation() {
        return participants[0].getFoodType().deviation.apply(participants[1].getFoodType());
    }

    /** Calculates the absolute deviation from .5 of the women-to-participants ratio
     *
     * @return The absolute gender deviation
     */
    public double getGenderDeviation() {
        return Math.abs(getParticipants().stream().map(Participant::getGender).filter(g -> g == Gender.FEMALE).count() / 2d - .5);
    }

    public double getDistance() {
        return kitchen.location().getDistance(InputData.getInstance().getEventLocation());
    }

    public boolean hasKitchen() {
        return kitchen != null;
    }

    public FoodType getFoodType() {
        return foodType;
    }
    public double getAverageAgeRange() {
        return (participants[0].getAge().value + participants[1].getAge().value)/ 2.0;
    }

    public void setStarterNumber(int starterNumber) {
        this.starterNumber = starterNumber;
    }

    public int getStarterNumber() {
        return starterNumber;
    }

    public int getMainNumber() {
        return mainNumber;
    }

    public int getDessertNumber() {
        return dessertNumber;
    }

    public void setMainNumber(int mainNumber) {
        this.mainNumber = mainNumber;
    }

    public void setDessertNumber(int dessertNumber) {
        this.dessertNumber = dessertNumber;
    }

    public SimpleStringProperty getIdAsProperty() {
        return new SimpleStringProperty(String.valueOf(id));
    }
}
