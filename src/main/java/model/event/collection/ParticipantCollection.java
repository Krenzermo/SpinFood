package model.event.collection;

import model.event.Course;
import model.event.list.ParticipantCollectionList;
import model.event.list.identNumbers.IdentNumber;
import model.kitchen.Kitchen;
import model.person.Participant;

import java.util.*;

/**
 * This interface provides a baseline {@link Collection} for any {@link Collection} of {@link Participant} objects.
 * There exist default implementations for a couple of essential {@link List}/{@link Collection} methods.
 * Since the purpose and usage of the {@link #add} and {@link #remove} methods have not yet been decided, those are not implemented.
 * All related methods are implemented to throw an {@link UnsupportedOperationException} by default.
 * Any {@link ParticipantCollection} implementation may or may not allow/disallow duplicates or {@code null} elements.
 *
 * @author Finn Brecher
 */
public interface ParticipantCollection extends List<Participant> {

	// Methods to be implemented:

	/**
	 * Gets the identifying numbers for this {@link ParticipantCollection}.
	 *
	 * @return the {@link IdentNumber} (Identifying Numbers) of {@code this}
	 */
	IdentNumber getIdentNumber();

	/**
	 * Creates a {@link List} containing all unique instances of {@link Participant} in this {@link ParticipantCollection}.
	 * The {@link List} may or may not be modifiable.
	 *
	 * @return a {@link List} containing all instances of {@link Participant} in {@code this}
	 */
	List<Participant> getParticipants();

	/**
	 * Gets the {@link Kitchen} assigned to this {@link ParticipantCollection}.
	 *
	 * @return the {@link Kitchen} assigned to {@code this}
	 */
	Kitchen getKitchen();

	// TODO: create methode evaluate() and make it useful
	/**
	 * Evaluates the {@link ParticipantCollection}.
	 *
	 * @return the evaluation of {@code this}
	 */
	double evaluate();

	/**
	 * Gets the difference in {@link model.person.AgeRange} in this {@link ParticipantCollection}.
	 *
	 * @return the difference in {@link model.person.AgeRange} in {@code this}
	 */
	int getAgeDifference();

	/**
	 * Gets the {@link Course} assigned to this {@link ParticipantCollection}.
	 *
	 * @return the {@link Course} assigned to {@code this}
	 */
	Course getCourse();


	// Inherited Methods:

	/**
	 * Adds the specified {@link Participant} to this {@link ParticipantCollection}.
	 *
	 * @param participant element whose presence in {@code this} is to be ensured
	 *
	 * @return {@code true} if the operation was successful, {@code false} otherwise
	 *
	 * @throws IllegalArgumentException if {@code this} already contains the element and duplicates are not allowed
	 * @throws NullPointerException if the element is {@code null} and {@code this} does not permit {@code null}
	 */
	@Override
	boolean add(Participant participant); // inherited from List interface

	/**
	 * Removes the specified object from this {@link ParticipantCollection}.
	 *
	 * @param o element to be removed from {@code this}, if present
	 * @return {@code true} if the operation was successful, {@code false} otherwise
	 *
	 * @throws NullPointerException if the element is {@code null} and {@code this} does not permit {@code null} (optional)
	 * @throws ClassCastException if the Object is not of type {@link Participant} (optional)
	 */
	@Override
	boolean remove(Object o); // inherited from List interface

	/**
	 * Returns the amount of {@link Participant} objects in this {@link ParticipantCollection}
	 *
	 * @return the size of {@code this}
	 */
	@Override
	default int size() {
		return getParticipants().size();
	}

	/**
	 * Returns if this {@link ParticipantCollection} does not have any {@link Participant} objects.
	 *
	 * @return {@code true} if {@code this} is empty, {@code false} otherwise
	 */
	@Override
	default boolean isEmpty() {
		return getParticipants().isEmpty();
	}

	/**
	 * Tests if the specified object is contained in this {@link ParticipantCollection}.
	 *
	 * @param o element whose presence in {@code this} is to be tested
	 *
	 * @return {@code true} if the element is contained in {@code this}, {@code false} otherwise
	 *
	 * @throws NullPointerException if the specified element is {@code null} and {@code this} does not permit {@code null} (optional)
	 * @throws ClassCastException if the Object is not of type {@link Participant} (optional)
	 */
	@Override
	default boolean contains(Object o) {
		if (o instanceof Participant) {
			return getParticipants().contains(o);
		}
		return false;
	}

	/**
	 * Returns an {@link Iterator} over all instances of {@link Participant} in this {@link ParticipantCollection}.
	 *
	 * @return an {@link Iterator} over all instances of {@link Participant} in {@code this}
	 */
	@Override
	default Iterator<Participant> iterator() {
		return getParticipants().iterator();
	}

	/**
	 * Returns an {@code Array} containing all instances of {@link Participant} in this {@link ParticipantCollection}.
	 *
	 * @return an {@code Array} containing all instances of {@link Participant} in {@code this}
	 */
	@Override
	default Object[] toArray() {
		return getParticipants().toArray();
	}

	/**
	 * Stores the elements of this {@link ParticipantCollection} in the specified {@code Array}.
	 *
	 * @param a   the {@code Array} into which the elements of {@code this} are to
	 *            be stored, if it is big enough; otherwise, a new {@code Array} of the
	 *            same runtime type is allocated for this purpose.
	 * @param <T> the type of the {@code Array}
	 *
	 * @return an {@code Array} containing the elements of {@code this}
	 *
	 * @throws ArrayStoreException if the runtime type of the specified {@code Array}
	 *          is not a supertype of the runtime type of every element in {@code this}
	 * @throws NullPointerException if the specified {@code Array} is {@code null}
	 */
	@Override
	default <T> T[] toArray(T[] a) {
		return getParticipants().toArray(a);
	}

	/**
	 * Returns {@code true} if this {@link ParticipantCollection} contains all the elements of the specified {@link Collection}.
	 *
	 * @param c {@link Collection} to be checked for containment in {@code this}
	 *
	 * @return {@code true} if {@code this} contains all elements of c, {@code false} otherwise
	 *
	 * @throws NullPointerException if the specified {@link Collection} contains one or more {@code null} elements and
	 *          this does not permit {@code null} elements (optional), or if the specified {@link Collection} is {@code null}
	 * @throws ClassCastException if the types of one or more elements
	 *          in the specified {@link Collection} are incompatible with {@code this} (optional)
	 */
	@Override
	default boolean containsAll(Collection<?> c) {
		return new HashSet<>(getParticipants()).containsAll(c);
	}

	/**
	 * This operation is not implemented by default
	 * and will {@code throw} an {@link UnsupportedOperationException}.
	 * Adds all the specified elements to this {@link ParticipantCollection}.
	 *
	 * @param c {@link Collection} containing elements to be added to {@code this}
	 *
	 * @return {@code true} if {@code this} changed as a result of the call, {@code false} otherwise
	 *
	 * @throws UnsupportedOperationException this operation is not implemented
	 * @throws NullPointerException if the specified element is null and this collection does not permit null
	 * @throws IllegalArgumentException if this list already contains an element and duplicates are not allowed
	 */
	@Override
	default boolean addAll(Collection<? extends Participant> c) {
		throw new UnsupportedOperationException();
	}

	/**
	 * This operation is not implemented by default
	 * and will {@code throw} an {@link UnsupportedOperationException}.
	 * Adds all the specified elements to this {@link ParticipantCollection}.
	 *
	 * @param index index at which to insert the first element from the
	 *              specified {@link Collection}
	 * @param c     {@link Collection} containing elements to be added to {@code this}
	 *
	 * @return {@code true} if {@code this} changed as a result of the call, {@code false} otherwise
	 *
	 * @throws UnsupportedOperationException this operation is not implemented
	 * @throws NullPointerException if the specified {@link Collection} is {@code null} and {@code this} does not permit {@code null}
	 * @throws IllegalArgumentException if {@code this} already contains an element and duplicates are not allowed
	 * @throws IndexOutOfBoundsException if the index is out of range ({@code index < 0 || index > size()})
	 */
	@Override
	default boolean addAll(int index, Collection<? extends Participant> c) {
		throw new UnsupportedOperationException();
	}

	/**
	 * This operation is not implemented by default
	 * and will {@code throw} an {@link UnsupportedOperationException}.
	 * Removes all the specified elements from this {@link ParticipantCollection}.
	 *
	 * @param c {@link Collection} containing elements to be removed from {@code this}
	 *
	 * @return {@code true} if {@code this} changed as a result of the call, {@code false} otherwise
	 *
	 * @throws UnsupportedOperationException this operation is not implemented
	 * @throws NullPointerException if the specified element is {@code null} and {@code this} does not permit {@code null} (optional)
	 */
	@Override
	default boolean removeAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	/**
	 * This operation is not implemented by default
	 * and will {@code throw} an {@link UnsupportedOperationException}.
	 * Removes all the non-specified elements from this {@link ParticipantCollection}.
	 *
	 * @param c {@link Collection} containing elements to be retained in {@code this}
	 *
	 * @return {@code true} if {@code this} changed as a result of the call
	 *
	 * @throws UnsupportedOperationException this operation is not implemented
	 * @throws NullPointerException if any element is {@code null} and {@code this} does not permit {@code null} (optional)
	 */
	@Override
	default boolean retainAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	/**
	 * This operation is not implemented by default
	 * and will {@code throw} an {@link UnsupportedOperationException}.
	 * Removes all the elements from this {@link ParticipantCollection}.
	 *
	 * @throws UnsupportedOperationException this operation is not implemented
	 */
	@Override
	default void clear() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Gets the {@link Participant} associated with the specified index.
	 *
	 * @param index index of the element to return
	 *
	 * @return the element at the specified position in {@code this}
	 *
	 * @throws IndexOutOfBoundsException if the index is out of range ({@code index < 0 || index > size()})
	 */
	@Override
	default Participant get(int index) {
		return getParticipants().get(index);
	}

	/**
	 * This operation is not implemented by default
	 * and will {@code throw} an {@link UnsupportedOperationException}.
	 * Replaces the element at the specified position in this list with the specified element.
	 *
	 * @param index   index of the element to replace
	 * @param element element to be stored at the specified position
	 *
	 * @return the element previously at the specified position
	 *
	 * @throws UnsupportedOperationException this operation is not implemented
	 * @throws IndexOutOfBoundsException if the index is out of range ({@code index < 0 || index > size()})
	 * @throws NullPointerException if the specified element is {@code null} and {@code this} does not permit {@code null}
	 */
	@Override
	default Participant set(int index, Participant element) {
		throw new UnsupportedOperationException();
	}

	/**
	 * This operation is not implemented by default
	 * and will {@code throw} an {@link UnsupportedOperationException}.
	 * Inserts the specified element at the specified position in this list.
	 * Shifts the element currently at that position (if any) and any subsequent elements to the right (adds one to their indices).
	 *
	 * @param index   index at which the specified element is to be inserted
	 * @param element element to be inserted
	 *
	 * @throws UnsupportedOperationException this operation is not implemented
	 * @throws IndexOutOfBoundsException if the index is out of range ({@code index < 0 || index > size()})
	 * @throws NullPointerException if the specified element is {@code null} and {@code this} does not permit {@code null}
	 * @throws IllegalArgumentException if {@code this} already contains the element and duplicates are not allowed
	 */
	@Override
	default void add(int index, Participant element) {
		throw new UnsupportedOperationException();
	}

	/**
	 * This operation is not implemented by default
	 * and will {@code throw} an {@link UnsupportedOperationException}.
	 * Removes the {@link Participant} at the specified index.
	 *
	 * @param index the index of the element to be removed
	 * @return the element previously at the specified position
	 *
	 * @throws UnsupportedOperationException this operation is not implemented
	 * @throws IndexOutOfBoundsException if the index is out of range ({@code index < 0 || index > size()})
	 */
	@Override
	default Participant remove(int index) {
		throw new UnsupportedOperationException();
	}

	/**
	 * This operation is not implemented by default
	 * and will {@code throw} an {@link UnsupportedOperationException}.
	 * Returns the index of the first occurrence of the specified element in this {@link ParticipantCollection},
	 * or {@code -1} if {@code this} does not contain the element.
	 * More formally, returns the lowest index {@code i} such that {@code Objects.equals(o, get(i))}, or {@code -1}
	 * if there is no such index.
	 *
	 * @param o element to search for
	 * @return  the index of the first occurrence of the specified element in {@code this},
	 *          or {@code -1} if {@code this} does not contain the element
	 *
	 * @throws UnsupportedOperationException this operation is not implemented
	 * @throws NullPointerException if the specified element is {@code null} and {@code this} does not permit {@code null} (optional)
	 */
	@Override
	default int indexOf(Object o) {
		throw new UnsupportedOperationException();
	}

	/**
	 * This operation is not implemented by default
	 * and will {@code throw} an {@link UnsupportedOperationException}.
	 * Returns the index of the last occurrence of the specified element in this {@link ParticipantCollection},
	 * or {@code -1} if {@code this} does not contain the element.
	 * More formally, returns the highest index {@code i} such that {@code Objects.equals(o, get(i))}, or {@code -1}
	 * if there is no such index.
	 *
	 * @param o element to search for
	 * @return  the index of the last occurrence of the specified element in
	 *          {@code this}, or {@code -1} if {@code this} does not contain the element
	 *
	 * @throws UnsupportedOperationException this operation is not implemented
	 * @throws NullPointerException if the specified element is {@code null} and {@code this} does not permit {@code null} (optional)
	 */
	@Override
	default int lastIndexOf(Object o) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Returns a {@link ListIterator} over the elements in this {@link ParticipantCollection} (in proper sequence).
	 *
	 * @return a {@link ListIterator} over the elements in {@code this} (in proper sequence)
	 */
	@Override
	default ListIterator<Participant> listIterator() {
		return getParticipants().listIterator();
	}

	/**
	 * Returns a {@link ListIterator} over the elements in this {@link ParticipantCollection} (in proper sequence),
	 * starting at the specified position in {@code this}.
	 * The specified index indicates the first element that would be returned by an initial call to {@link ListIterator#next next}.
	 * An initial call to {@link ListIterator#previous previous}  would return the element with the specified index minus one.
	 *
	 * @param index index of the first element to be returned from the
	 *              {@link ListIterator} (by a call to {@link ListIterator#next next})
	 * @return  a {@link ListIterator} over the elements in {@code this} (in proper sequence),
	 *          starting at the specified position in {@code this}
	 * @throws IndexOutOfBoundsException if the index is out of range ({@code index < 0 || index > size()})
	 */
	@Override
	default ListIterator<Participant> listIterator(int index) {
		return getParticipants().listIterator(index);
	}

	/**
	 * Returns a view of the portion of this {@link ParticipantCollection}
	 * between the specified {@code fromIndex} (inclusive), and {@code toIndex} (exclusive).
	 * (If {@code fromIndex} and {@code toIndex} are equal, the returned {@link List} is empty.)
	 * The returned {@link List} is backed by this {@link ParticipantCollection},
	 * so non-structural changes in the returned {@link List} are reflected in this {@link ParticipantCollection}, and vice-versa.
	 * The returned {@link List} supports all the optional {@link List} operations supported by this {@link List}.
	 *
	 * @param fromIndex low endpoint (inclusive) of the subList
	 * @param toIndex   high endpoint (exclusive) of the subList
	 * @return a view of the specified range within {@code this}
	 * @throws IndexOutOfBoundsException if the index is out of range ({@code index < 0 || index > size()})
	 */
	@Override
	default List<Participant> subList(int fromIndex, int toIndex) {
		return getParticipants().subList(fromIndex, toIndex);
	}
}
