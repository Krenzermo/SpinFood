package model.event.collection;

import model.event.Course;
import model.identNumbers.IdentNumber;
import model.kitchen.Kitchen;
import model.person.AgeRange;
import model.person.Participant;

import java.util.List;

// TODO: Implementierung
public class Group implements ParticipantCollection{
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
		// TODO: this
		return List.of();
	}

	/**
	 * @return the {@link Kitchen} assigned to this ParticipantCollection
	 */
	@Override
	public Kitchen getKitchen() {
		// TODO: this
		return null;
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
		return null;
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
}
