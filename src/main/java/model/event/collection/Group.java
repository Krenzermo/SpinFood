package model.event.collection;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import model.event.Course;
import model.event.list.GroupList;
import model.event.list.identNumbers.IdentNumber;
import model.kitchen.Kitchen;
import model.person.AgeRange;
import model.person.Participant;

import javax.naming.OperationNotSupportedException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/** This class holds a Group (3 {@link Pair}) in compliance with the defined rules
 *
 * @author Finn Brecher
 * @author Daniel Hinkelmann
 * @author Ole Krenzer
 * @author Davide Piacenza
 */
public class Group implements ParticipantCollection{

	private final Pair[] pairs;
	private Kitchen kitchen;
	private Course course;
	private int id;
	public static int COUNTER = 0;
	private int cookIndex;


	/**
	 * Constructs a Group with the specified pairs, course, and kitchen.
	 * Sets the pair IDs and determines the cook index based on the kitchen owner.
	 *
	 * @param pair1 the first pair in the group
	 * @param pair2 the second pair in the group
	 * @param pair3 the third pair in the group
	 * @param course the course assigned to the group
	 * @param kitchen the kitchen assigned to the group
	 */
	public Group(Pair pair1, Pair pair2, Pair pair3, Course course, Kitchen kitchen) {
		this(pair1, pair2, pair3, course, kitchen, COUNTER++);
	}

	public Group(Pair pair1, Pair pair2, Pair pair3, Course course, Kitchen kitchen, int id) {
		id = COUNTER++;
		this.pairs = new Pair[]{pair1, pair2, pair3};
		this.course = course;
		this.kitchen = kitchen;
		setPairIds();
		cookIndex = getKitchenOwner(kitchen);
		if (cookIndex < 0) {
			throw new IllegalStateException("Couldn't find any owner of kitchen: " + kitchen);
		}
	}

	/**
	 * Copy constructor for class {@link Group}.
	 * Copies all fields but does not copy the {@link Group} class information.
	 * This constructor returns a deep copy (also copies the {@link Pair} instances).
	 *
	 * @param group the specified {@link Group}
	 */
	public Group(Group group) {
		id = group.id;
		course = group.course;
		kitchen = group.kitchen;
		cookIndex = group.cookIndex;
		Pair[] temp = group.getPairs();
		pairs = new Pair[]{new Pair(temp[0]), new Pair(temp[1]), new Pair(temp[2])};
	}

	private void setPairIds(){

		if (this.course == Course.STARTER){
			pairs[0].setStarterNumber(id);
			pairs[1].setStarterNumber(id);
			pairs[2].setStarterNumber(id);
			pairs[0].setCourse(course);

		}else if (this.course == Course.MAIN){
			pairs[0].setMainNumber(id);
			pairs[1].setMainNumber(id);
			pairs[2].setMainNumber(id);
			pairs[1].setCourse(course);

		}else {
			pairs[0].setDessertNumber(id);
			pairs[1].setDessertNumber(id);
			pairs[2].setDessertNumber(id);
			pairs[2].setCourse(course);
		}
	}


	private int getKitchenOwner(Kitchen kitchen){
		for (int i = 0; i < pairs.length; i++){
			if (pairs[i].getKitchen().equals(kitchen)){
				return i;
			}
		}
		return -1;
	}

	/**
	 * @return the {@link IdentNumber} (Identifying Numbers) of this ParticipantCollection
	 */
	@Override
	public IdentNumber<Group> getIdentNumber() {
		// TODO: this
		return null;
	}

	/**
	 * @return a modifiable List containing all instances of {@link Participant} in this ParticipantCollection
	 */
	@Override
	public List<Participant> getParticipants() {
        List<Pair> participants = new ArrayList<>(Arrays.stream(pairs).toList());
		return participants.stream().flatMap(p -> p.getParticipants().stream()).collect(Collectors.toList());
	}

	/**
	 * @return the {@link Kitchen} assigned to this ParticipantCollection
	 */
	@Override
	public Kitchen getKitchen() {
		return kitchen;
	}

	/**
	 * @return the evaluation of this ParticipantCollection
	 */
	@Override
	public double evaluate() {
		// TODO: this
		return 0;
	}

	/**
	 * @return the difference in {@link AgeRange} in this ParticipantCollection
	 */
	@Override
	public int getAgeDifference() {
		// TODO: this
		return 0;
	}

	/**
	 * @return the {@link Course} assigned to this ParticipantCollection
	 */
	@Override
	public Course getCourse() {
		return course;
	}

	/**
	 * @param participant element whose presence in this collection is to be ensured
	 * @return {@code true} if the operation was successful, {@code false} otherwise
	 * @throws IllegalArgumentException if this list already contains the element and duplicates are not allowed
	 * @throws NullPointerException     if the element is null and this list does not permit null
	 */
	@Override
	public boolean add(Participant participant) {
		// TODO: this
		throw new UnsupportedOperationException();
	}

	/**
	 * @param o element to be removed from this list, if present
	 * @return {@code true} if the operation was successful, {@code false} otherwise
	 * @throws NullPointerException if the element is null and this list does not permit null
	 * @throws ClassCastException   if the Object is not of type {@link Participant}
	 */
	@Override
	public boolean remove(Object o) {
		// TODO: this
		throw new UnsupportedOperationException();
	}

	/**
	 * @return a string representation of the Group in a specific output format
	 */
	public String asOutputString() {
		return id + ";" + course.getAsInt() + ";" + pairs[0].asOutputString() + ";" + pairs[1].asOutputString() + ";" + pairs[2].asOutputString();
	}

	/**
	 * @return the pairs in this Group
	 */
	public Pair[] getPairs() {
		return pairs;
	}

	/**
	 * @return the ID of this Group
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return the ID of this Group as an observable value
	 */
	public ObservableValue<Integer> getIdAsObservable() {
		return new SimpleIntegerProperty(id).asObject();
	}

	/**
	 * @return the ID of the cook pair in this Group as an observable value
	 */
	public ObservableValue<Integer> getCookPairIdAsObservable() {
		return new SimpleIntegerProperty(pairs[cookIndex].getId()).asObject();
	}

	@Override
	public int hashCode() {
		return Objects.hash(pairs[0], pairs[1], pairs[2]);
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof Group group)) {
			return false;
		}

		List<Pair> list = Arrays.stream(group.getPairs()).sequential().toList();

		if (list.size() != 3) {
			throw new RuntimeException("fuck this"); // TODO: delete
		}

		for (Pair pair : list) {
			if (!pair.equals(pairs[0]) && !pair.equals(pairs[1]) && !pair.equals(pairs[2])) {
				return false;
			}
		}
		return true;
	}

	@Override
	public String toString() {
		return "{Pair1: " + pairs[0] + ", Pair2: " + pairs[1] + ", Pair3: " + pairs[2] + "}";
	}
}
