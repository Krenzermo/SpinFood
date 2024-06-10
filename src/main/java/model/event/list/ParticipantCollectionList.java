package model.event.list;

import model.event.collection.ParticipantCollection;
import model.event.list.identNumbers.IdentNumber;
import model.person.Participant;

import java.util.*;

/**
 * This class provides a baseline {@link List} implementation for any {@link List} of {@link ParticipantCollection} objects.
 * Trying to add {@code null} or a {@link ParticipantCollection} containing {@code null} elements
 * will result in a {@link NullPointerException}.
 * Trying to add duplicates will result in an {@link IllegalArgumentException}.
 *
 * @author Finn Brecher
 *
 * @param <E> The type of the elements in this {@link ParticipantCollectionList}, extends {@link ParticipantCollection}
 */
public abstract class ParticipantCollectionList<E extends ParticipantCollection> extends ArrayList<E> {
	/**
	 * A {@link HashSet} containing all unique instances of {@link Participant} in this {@link ParticipantCollectionList}.
	 */
	private final Set<Participant> participants = new HashSet<>();

	// Methods to be implemented:

	/**
	 * Gets the identifying numbers for this {@link ParticipantCollectionList}.
	 *
	 * @return the {@link IdentNumber} (Identifying Numbers) of {@code this}
	 */
	abstract IdentNumber getIdentNumber();

	/**
	 * Evaluates the {@link ParticipantCollectionList}.
	 *
	 * @return the evaluation of {@code this}
	 */
	abstract double evaluate(); // TODO: create methode evaluate() and make it useful


	// Participant Methods:

	/**
	 * Creates a modifiable {@link List} containing all unique instances of {@link Participant} in this {@link ParticipantCollectionList}.
	 *
	 * @return a modifiable {@link List} containing all unique instances of {@link Participant} in {@code this}
	 */
	public List<Participant> getParticipants() {
		return new ArrayList<>(participants);
	}

	/**
	 * Calculates the amount of unique instances of {@link Participant} in this {@link ParticipantCollectionList}.
	 *
	 * @return the amount of unique instances of {@link Participant} in {@code this}
	 */
	public int getParticipantCount() {
		return participants.size();
	}

	/**
	 * Tests if this {@link ParticipantCollectionList} contains the {@link Participant}.
	 *
	 * @param participant {@link Participant} whose presence in {@code this} is to be tested
	 *
	 * @return true if {@code this} contains a {@link ParticipantCollection} containing the {@link Participant}, {@code false} otherwise
	 */
	public boolean containsParticipant(Participant participant) {
		return participants.contains(participant);
	}

	/**
	 * Test if this {@link ParticipantCollectionList} contains all instances of {@link Participant}.
	 *
	 * @param participants elements whose presence in {@code this} is to be tested
	 *
	 * @return {@code true} if all elements are contained in {@code this}, {@code false} otherwise
	 */
	public boolean containsAllParticipants(Collection<Participant> participants) {
		return this.participants.containsAll(participants);
	}

	/**
	 * Test if this {@link ParticipantCollectionList} contains any instances of {@link Participant}.
	 *
	 * @param participants elements whose presence in {@code this} is to be tested
	 *
	 * @return {@code true} if any element is contained in {@code this}, {@code false} otherwise
	 */
	public boolean containsAnyParticipant(Collection<Participant> participants) {
		return participants.stream().anyMatch(this.participants::contains);
	}

	/**
	 * Creates an {@code Array} containing all unique instances of {@link Participant} in this {@link ParticipantCollectionList}.
	 *
	 * @return an {@code Array} containing all unique instances of {@link Participant} in {@code this}
	 */
	public Participant[] getParticipantArray() {
		return participants.toArray(Participant[]::new);
	}

	/**
	 * Creates an {@link Iterator} over all unique instances of {@link Participant} in this {@link ParticipantCollectionList}.
	 *
	 * @return an {@link Iterator} over all unique instances of {@link Participant} in {@code this}
	 */
	public Iterator<Participant> getParticipantIterator() {
		return participants.iterator();
	}

	/**
	 * Gets the element at the specified index from field {@link #participants}.
	 * This method is equivalent to calling {@code getParticipants().get(index)}.
	 *
	 * @param index index of the element to return
	 *
	 * @return the element at the specified position in the {@link List} of {@link Participant} objects
	 *
	 * @throws IndexOutOfBoundsException if the index is out of range ({@code index < 0 || index > size()})
	 */
	public Participant getParticipant(int index) {
		return getParticipants().get(index);
	}


	// Utility and check methods:

	/**
	 * This method is used to get the instances of {@link ParticipantCollection} in this {@link ParticipantCollectionList}
	 * without changing the underlying data structure.
	 *
	 * @return a mutable {@link List} containing all instances of {@link ParticipantCollection} in {@code this}
	 */
	private List<E> getParticipantCollections() {
		return new ArrayList<>(this);
	}

	/**
	 * Clears this {@link ParticipantCollectionList} and adds all elements of the specified {@link List}.
	 *
	 * @param list the {@link List} to be set as this
	 *
	 * @throws IllegalArgumentException if the type check fails
	 */
	public void setList(List<E> list) {
		clear();
		addAll(list);
	}

	/**
	 * Checks if the specified {@link ParticipantCollection} is or contains {@code null}.
	 *
	 * @param collection the {@link ParticipantCollection} to be checked
	 *
	 * @throws NullPointerException if the {@link ParticipantCollection} is or contains {@code null}
	 */
	private void nullCheck(E collection) {
		Objects.requireNonNull(collection);
		if (collection.contains(null)) { // TODO: may need to be changed if Pair or Group throw NullPointerException
			throw new NullPointerException("Collection contains null element");
		}
	}

	/**
	 * Checks if the specified {@link ParticipantCollection} could be added to this {@link ParticipantCollectionList}.
	 *
	 * @param collection the {@link ParticipantCollection} to be checked
	 *
	 * @throws NullPointerException if the {@link ParticipantCollection} is or contains {@code null}
	 * @throws IllegalArgumentException if any {@link Participant} or the {@link ParticipantCollection} is already contained in {@code this}
	 */
	private void check(E collection) {
		nullCheck(collection);
		if (contains(collection)) {
			throw new IllegalArgumentException("This ParticipantCollectionList already contains the collection: " + collection);
		}
		if (containsAnyParticipant(collection)) {
			throw new IllegalArgumentException("This ParticipantCollectionList already contains a participant: " + collection);
		}
	}


	// Inherited Methods:

	/**
	 * Adds the specified {@link ParticipantCollection} to this {@link ParticipantCollectionList}.
	 * The element will not be added if this {@link ParticipantCollectionList}
	 * already contains at least one of the instances of {@link Participant}.
	 *
	 * @param collection element whose presence in {@code this} is to be ensured
	 *
	 * @return {@code true} if the operation was successful, {@code false} otherwise
	 *
	 * @throws NullPointerException if the {@link ParticipantCollection} or a participant is {@code null}
	 * @throws IllegalArgumentException if any {@link Participant} or the {@link ParticipantCollection} is already in {@code this}
	 */
	@Override
	public boolean add(E collection) {
		check(collection);
		participants.addAll(collection);
		return super.add(collection);
	}

	/**
	 * Removes the specified object from this {@link ParticipantCollectionList}, if possible.
	 *
	 * @param object element to be removed from {@code this}, if present
	 *
	 * @return {@code true} if the operation was successful, {@code false} otherwise
	 */
	@Override
	public boolean remove(Object object) {
		participants.remove(object);
		return super.remove(object);
	}

	/**
	 * Removes all the elements from this {@link ParticipantCollectionList}.
	 * {@code this} will be empty after this call returns.
	 */
	@Override
	public void clear() {
		participants.clear();
		super.clear();
	}

	/**
	 * Adds all specified elements to this {@link ParticipantCollectionList}, if possible.
	 *
	 * @param c {@link Collection} containing elements to be added to {@code this}
	 *
	 * @return {@code true} if {@code this} changed as a result of the call, {@code false} otherwise
	 *
	 * @throws NullPointerException if any {@link ParticipantCollection} is or contains {@code null}
	 * @throws IllegalArgumentException if any {@link Participant} or {@link ParticipantCollection} is already in {@code this}
	 */
	@Override
	public boolean addAll(Collection<? extends E> c) {
		for (E e : c) {
			check(e);
		}
		c.forEach(participants::addAll);
		return super.addAll(c);
	}

	/**
	 * Adds all specified elements to this {@link ParticipantCollectionList}, if possible.
	 *
	 * @param index index at which to insert the first element from the specified {@link Collection}
	 * @param c {@link Collection} containing elements to be added to {@code this}
	 *
	 * @return {@code true} if {@code this} changed as a result of the call, {@code false} otherwise
	 *
	 * @throws NullPointerException if any {@link ParticipantCollection} is or contains {@code null}
	 * @throws IllegalArgumentException if any {@link Participant} or {@link ParticipantCollection} is already in {@code this}
	 * @throws IndexOutOfBoundsException if the index is out of range ({@code index < 0 || index > size()})
	 */
	@Override
	public boolean addAll(int index, Collection<? extends E> c) {
		for (E e : c) {
			check(e);
		}
		c.forEach(participants::addAll);
		return super.addAll(index, c);
	}


	/**
	 * Removes all the specified elements from this {@link ParticipantCollectionList}, if possible.
	 *
	 * @param c {@link Collection} containing elements to be removed from {@code this}
	 *
	 * @return {@code true} if {@code this} changed as a result of the call, {@code false} otherwise
	 *
	 * @throws ClassCastException if the class of an element of {@code this} is incompatible with the specified {@link Collection}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean removeAll(Collection<?> c) {
		((Collection<Collection<?>>) c).forEach(participants::removeAll);
		return super.removeAll(c);
	}

	/**
	 * Removes all non-specified elements from this {@link ParticipantCollectionList}.
	 *
	 * @param c {@link Collection} containing elements to be retained in {@code this}
	 *
	 * @return {@code true} if {@code this} changed as a result of the call, {@code false} otherwise
	 *
	 * @throws ClassCastException if the class of an element of {@code this} is incompatible with the specified {@link Collection}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean retainAll(Collection<?> c) {
		((Collection<Collection<?>>) c).forEach(participants::retainAll);
		return super.retainAll(c);
	}

	/**
	 * Adds the specified element to this {@link ParticipantCollectionList} at the specified index.
	 *
	 *
	 * @param index   index of the element to replace
	 * @param element element to be stored at the specified position
	 *
	 * @return the element previously at the specified position
	 *
	 * @throws NullPointerException if element is or contains {@code null}
	 * @throws IllegalArgumentException if {@code this} already contains the {@link ParticipantCollection} or a {@link Participant}
	 * @throws IndexOutOfBoundsException if the index is out of range ({@code index < 0 || index > size()})
	 */
	@Override
	public E set(int index, E element) {
		check(element);
		E oldElement =  super.set(index, element);
		if (oldElement != null) {
			oldElement.forEach(participants::remove); //optimized for speed (by IntelliJ)
		}
		participants.addAll(element);
		return oldElement;
	}

	/**
	 * Inserts the specified element at the specified position in this {@link ParticipantCollectionList} (optional operation).
	 * Shifts the element currently at that position (if any) and any subsequent elements to the right (adds one to their indices).
	 *
	 * @param index   index at which the specified element is to be inserted
	 * @param element element to be inserted
	 *
	 * @throws NullPointerException if element is or contains {@code null}
	 * @throws IllegalArgumentException if {@code this} already contains the {@link ParticipantCollection} or a {@link Participant}
	 * @throws IndexOutOfBoundsException if the index is out of range ({@code index < 0 || index > size()})
	 */
	@Override
	public void add (int index, E element) {
		check(element);
		participants.addAll(element);
		super.add(index, element);
	}

	/**
	 * Removes the element at the specified position in this {@link ParticipantCollectionList}.
	 * Shifts any subsequent elements to the left (subtracts one from their indices).
	 *
	 * @param index the index of the element to be removed
	 *
	 * @return the element previously at the specified position
	 *
	 * @throws IndexOutOfBoundsException if the index is out of range ({@code index < 0 || index > size()})
	 */
	@Override
	public E remove(int index) {
		E oldElement =  super.remove(index);
		if (oldElement != null) {
			participants.addAll(oldElement);
		}
		return oldElement;
	}
}
