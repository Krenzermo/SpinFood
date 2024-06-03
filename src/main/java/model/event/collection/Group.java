package model.event.collection;

import model.event.Course;
import model.event.list.GroupList;
import model.identNumbers.IdentNumber;
import model.kitchen.Kitchen;
import model.person.AgeRange;
import model.person.Participant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

// TODO: Implementierung
public class Group implements ParticipantCollection{

	private final Pair[] pairs;
	private Kitchen kitchen;
	private Course course;


	public Group(Pair pair1, Pair pair2, Pair pair3, Course course, Kitchen kitchen) {
		this.pairs = new Pair[]{pair1, pair2, pair3};
		this.course = course;
		this.kitchen = kitchen;
	}

	/**
	 * @return the {@link IdentNumber} (Identifying Numbers) of this ParticipantCollection
	 */
	@Override
	public IdentNumber getIdentNumber() {
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
		// TODO: this
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
		// TODO: this
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
		return false;
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
		return false;
	}

	public Pair[] getPairs() {
		return pairs;
	}
}
