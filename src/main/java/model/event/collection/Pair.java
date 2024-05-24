package model.event.collection;

import model.event.Location;
import model.identNumbers.IdentNumber;
import model.kitchen.Kitchen;
import model.kitchen.KitchenAvailability;
import model.person.Participant;
import model.event.Course;
import model.event.InputData;

import java.util.List;
import java.util.Objects;

/**
 * @author Finn Brecher
 * @author Davide Piacenza
 * @author Daniel Hinkelmann
 */
public class Pair implements ParticipantCollection {

    private final Participant[] participants = new Participant[2];
    private Kitchen kitchen;
    private Course course;
    //TODO: change type to Group
    private ParticipantCollection[] groups = new ParticipantCollection[3];
    public final boolean signedUpTogether;

    private static final InputData inputData = InputData.getInstance();

    public Pair(Participant participant1, Participant participant2) {
        this(participant1, participant2, false);
    }

    public Pair(Participant participant1, Participant participant2, boolean signedUpTogether) {
        participants[0] = participant1;
        participants[1] = participant2;
        this.signedUpTogether = signedUpTogether;
        this.kitchen = autoAssignKitchen();
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

    // TODO: change type to Group
    public List<ParticipantCollection> getGroups() {
        return List.of(groups);
    }

    public void setKitchen(Kitchen kitchen) {
        this.kitchen = kitchen;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    // TODO: change type to Group
    public void setGroups(ParticipantCollection[] groups) {
        for (ParticipantCollection group: groups)  {
            // TODO: may need to be changed once group is implemented.
            if (!group.contains(this)) {
                throw new RuntimeException("cannot assign a Group to this Pair if the group does not contain the Pair");
            }
        }

        if (groups.length != 3) {
            throw new RuntimeException("Groups must have exactly 3 pairs!");
        }

        this.groups = groups;

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
            return participants[0].getKitchen();
        }
        if (KitchenAvailability.NO.equals(participants[0].isHasKitchen())) {
            return participants[1].getKitchen();
        }
        if (KitchenAvailability.NO.equals(participants[1].isHasKitchen())) {
            return participants[0].getKitchen();
        }

        // if exactly one participant maybe has a kitchen
        if (KitchenAvailability.MAYBE.equals(participants[0].isHasKitchen()) ^ KitchenAvailability.MAYBE.equals(participants[1].isHasKitchen())) {
            if (KitchenAvailability.YES.equals(participants[0].isHasKitchen())) {
                return participants[0].getKitchen();
            }
            if (KitchenAvailability.YES.equals(participants[1].isHasKitchen())) {
                return participants[1].getKitchen();
            }

            return KitchenAvailability.MAYBE.equals(participants[0].isHasKitchen()) ? participants[0].getKitchen() : participants[1].getKitchen();
        }

        // here if both have a kitchen or both maybe have a kitchen

        Location eventLocation = inputData.getEventLocation();

        // select the one closest to the eventLocation
        if (participants[0].getKitchen().location().getDistance(eventLocation) <= participants[1].getKitchen().location().getDistance(eventLocation)) {
            return participants[0].getKitchen();
        }

        return participants[1].getKitchen();
    }

    @Override
    public boolean add(Participant participant) {
        if (contains(participant)) {
            return false;
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
        return "{Participant 1: " + participants[0].toString() + ", Participant 2: " + participants[1].toString() + "}";
    }
}
