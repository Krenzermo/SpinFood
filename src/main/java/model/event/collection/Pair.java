package model.event.collection;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableValue;
import model.event.Location;
import model.event.list.identNumbers.IdentNumber;
import model.kitchen.Kitchen;
import model.kitchen.KitchenAvailability;
import model.person.FoodType;
import model.person.Gender;
import model.person.Participant;
import model.event.Course;
import model.event.io.InputData;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/** This class represents one Pair of {@link Participant} that complies with the defined rules.
 *
 * @autor Finn Brecher
 * @autor Davide Piacenza
 * @autor Daniel Hinkelmann
 */
public class Pair implements ParticipantCollection {

    private final int id;
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

    //private static int COUNTER = 0;
    private static final InputData inputData = InputData.getInstance();

    /**
     * Constructs a Pair object with two participants and assigns an ID.
     *
     * @param participant1 The first participant of the pair.
     * @param participant2 The second participant of the pair.
     * @param idCounter    The ID counter for uniquely identifying this pair.
     */

    public Pair(Participant participant1, Participant participant2, int idCounter) {
        this(participant1, participant2, false, idCounter);
    }

    /**
     * Constructs a Pair object with two participants, signed up together status, and assigns an ID.
     *
     * @param participant1     The first participant of the pair.
     * @param participant2     The second participant of the pair.
     * @param signedUpTogether Indicates if the participants signed up together.
     * @param idCounter        The ID counter for uniquely identifying this pair.
     */
    public Pair(Participant participant1, Participant participant2, boolean signedUpTogether, int idCounter) {
        id = idCounter;
        participants[0] = participant1;
        participants[1] = participant2;
        this.signedUpTogether = signedUpTogether;
        this.kitchen = autoAssignKitchen();
        this.foodType = autoAssignFoodType();
        participant1.setPair(this);
        participant2.setPair(this);
    }

    /**
     * Creates a shallow Copy of the specified {@link Pair}.
     * That copy does not contain the {@link Group} information of the original {@link Pair}
     *
     * @param pair the specified {@link Pair}
     */
    public Pair(Pair pair) {
        this.participants[0] = new Participant(pair.participants[0]);
        this.participants[1] = new Participant(pair.participants[1]);
        participants[0].setPair(this);
        participants[1].setPair(this);
        this.kitchen = pair.kitchen;
        this.foodType = pair.foodType;
        this.signedUpTogether = pair.signedUpTogether;
        this.id = pair.id;
        this.course = pair.course;
        this.starterNumber = pair.starterNumber;
        this.mainNumber = pair.mainNumber;
        this.dessertNumber = pair.dessertNumber;
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
        return participants[0].getAgeRange().getAgeDifference(participants[1].getAgeRange());
    }

    @Override
    public Course getCourse() {
        return course;
    }

    public int getId() {
        return id;
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

    /**
     * sets the groups for the pair
     *
     * @param groups
     */
    public void setGroups(Group[] groups) {
        for (Group group : groups) {
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
        groups = new Group[]{null, null, null};
        this.setCourse(null);
        this.setStarterNumber(0);
        this.setMainNumber(0);
        this.setDessertNumber(0);
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
     * Replaces a participant in the pair with a new participant.
     *
     * @param oldParticipant the participant to be replaced
     * @param newParticipant the new participant to be added to the pair
     * @throws IllegalArgumentException if the old participant is not in the pair
     * @throws IllegalArgumentException if the new participant is already in the pair
     * @throws NullPointerException     if the new participant is null
     */
    public void replaceParticipant(Participant oldParticipant, Participant newParticipant) {
        if (newParticipant == null) {
            throw new NullPointerException("New participant must not be null");
        }
        if (!contains(oldParticipant)) {
            throw new IllegalArgumentException("Old participant is not part of this pair");
        }
        if (contains(newParticipant)) {
            throw new IllegalArgumentException("New participant is already part of this pair");
        }

        if (participants[0].equals(oldParticipant)) {
            participants[0] = newParticipant;
        } else {
            participants[1] = newParticipant;
        }

        // EXTRA kein update der anderen Parameter --> hat Tutor bestätigt
        // Methode wird eh nur in der händischen Eingabe der GUI verwendet
    }

    private boolean contains(Participant participant) {
        return participants[0].equals(participant) || participants[1].equals(participant);
    }

    /**
     * Adds a participant to the pair.
     *
     * @param participant element whose presence in this collection is to be ensured
     * @return {@code true} if the operation was successful, {@code false} otherwise
     * @throws IllegalArgumentException if this ParticipantCollection already contains the Element
     * @throws NullPointerException     if the element is null
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
        //return "{Participant1: " + participants[0] + " Participant2: " + participants[1] + kitchen + "}";
        return starterNumber + " " + mainNumber + " " + dessertNumber;
    }

    /**
     * Creates an Output String for this pair object in the following format:
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

    /**
     * Calculates the deviation of Food preferences of the Participants of this Pair
     *
     * @return The Preference deviation
     */
    public int getPreferenceDeviation() {
        return participants[0].getFoodType().deviation.apply(participants[1].getFoodType());
    }

    /**
     * Calculates the absolute deviation from .5 of the women-to-participants ratio
     *
     * @return The absolute gender deviation
     */
    public double getGenderDeviation() {
        return Math.abs(getParticipants().stream().map(Participant::getGender).filter(g -> g == Gender.FEMALE).count() / 2d - .5);
    }

    /**
     * @return the distance from the pairs kitchen to the eventlocation
     */
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
        return (participants[0].getAgeRange().value + participants[1].getAgeRange().value)/ 2.0;
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

    public ObservableValue<Integer> getIdAsObservable() {
        return new SimpleIntegerProperty(id).asObject();
    }

    public ObservableValue<Boolean> getSignedUpTogetherAsObservable() {
        return new SimpleBooleanProperty(signedUpTogether);
    }

    /**
     * Gets the partner of the specified {@link Participant} in this {@link Pair} and returns {@code null} otherwise
     *
     * @param participant the specified {@link Participant}
     * @return the other {@link Participant} in {@code this} or {@code null}
     */
    public Participant getOtherParticipant(Participant participant) {
        if (!contains(participant)) {
            return null;
        }
        return participants[0].equals(participant) ? participants[1] : participants[0];
    }

    public boolean isGroupsEmpty() {
        return Objects.isNull(groups[0]) && Objects.isNull(groups[1]) && Objects.isNull(groups[2]);
    }
}