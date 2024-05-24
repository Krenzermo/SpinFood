package model.event.collection;

import model.event.Course;
import model.identNumbers.IdentNumber;
import model.kitchen.Kitchen;
import model.person.Participant;

import java.util.*;

/**
 * @author Finn Brecher
 */
public interface ParticipantCollection extends List<Participant> {
	/**
	 * @return the {@link IdentNumber} (Identifying Numbers) of this ParticipantCollection
	 */
	IdentNumber getIdentNumber();

	/**
	 * @return a list containing all instances of {@link Participant} in this ParticipantCollection
	 */
	List<Participant> getParticipants();

	/**
	 * @return the {@link Kitchen} assigned to this ParticipantCollection
	 */
	Kitchen getKitchen();

	// TODO: create methode evaluate() and make it useful
	/**
	 * @return the evaluation of this ParticipantCollection
	 */
	double evaluate();

	/**
	 * @return the difference in {@link model.person.AgeRange} in this ParticipantCollection
	 */
	int getAgeDifference();

	/**
	 * @return the {@link Course} assigned to this ParticipantCollection
	 */
	Course getCourse();

	/**
	 * @param participant element whose presence in this collection is to be ensured
	 * @return {@code true} if the operation was successful, {@code false} otherwise
	 */
	@Override
	boolean add(Participant participant); // inherited from List interface

	/**
	 * @param o element to be removed from this list, if present
	 * @return {@code true} if the operation was successful, {@code false} otherwise
	 */
	@Override
	boolean remove(Object o); // inherited from List interface

	/**
	 * @return the size of this ParticipantCollection
	 */
	@Override
	default int size() {
		return getParticipants().size();
	}

	/**
	 * @return {@code true} if this ParticipantCollection is empty, {@code false} otherwise
	 */
	@Override
	default boolean isEmpty() {
		return getParticipants().isEmpty();
	}

	/**
	 * @param o element whose presence in this list is to be tested
	 * @return {@code true} if the element is contained in this ParticipantCollection
	 */
	@Override
	default boolean contains(Object o) {
		if (o instanceof Participant) {
			return getParticipants().contains(o);
		}
		return false;
	}

	/**
	 * @return an Iterator over all Participants in this ParticipantCollection
	 */
	@Override
	default Iterator<Participant> iterator() {
		return getParticipants().iterator();
	}

	/**
	 * @return an array containing all Participants int this ParticipantCollection
	 */
	@Override
	default Object[] toArray() {
		return getParticipants().toArray();
	}

	/**
	 * @param a   the array into which the elements of this list are to
	 *            be stored, if it is big enough; otherwise, a new array of the
	 *            same runtime type is allocated for this purpose.
	 * @param <T> the type of the array
	 * @return an array containing the elements of this list
	 */
	@Override
	default <T> T[] toArray(T[] a) {
		return getParticipants().toArray(a);
	}

	/**
	 * @param c collection to be checked for containment in this list
	 * @return {@code true} if this ParticipantCollection contains all elements of c
	 */
	@Override
	default boolean containsAll(Collection<?> c) {
		return new HashSet<>(getParticipants()).containsAll(c);
	}

	/**
	 * This operation is not implemented by default.
	 *
	 * @param c collection containing elements to be added to this collection
	 * @return {@code true} if this list changed as a result of the call
	 *
	 * @throws UnsupportedOperationException this operation is not implemented
	 */
	@Override
	default boolean addAll(Collection<? extends Participant> c) {
		throw new UnsupportedOperationException();
	}

	/**
	 * This operation is not implemented by default.
	 *
	 * @param index index at which to insert the first element from the
	 *              specified collection
	 * @param c     collection containing elements to be added to this list
	 * @return {@code true} if this list changed as a result of the call
	 *
	 * @throws UnsupportedOperationException this operation is not implemented
	 */
	@Override
	default boolean addAll(int index, Collection<? extends Participant> c) {
		throw new UnsupportedOperationException();
	}

	/**
	 * This operation is not implemented by default.
	 *
	 * @param c collection containing elements to be removed from this list
	 * @return {@code true} if this list changed as a result of the call
	 *
	 * @throws UnsupportedOperationException this operation is not implemented
	 */
	@Override
	default boolean removeAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	/**
	 * This operation is not implemented by default.
	 *
	 * @param c collection containing elements to be retained in this list
	 * @return {@code true} if this list changed as a result of the call
	 *
	 * @throws UnsupportedOperationException this operation is not implemented
	 */
	@Override
	default boolean retainAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	/**
	 * This operation is not implemented by default.
	 *
	 * @throws UnsupportedOperationException this operation is not implemented
	 */
	@Override
	default void clear() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param index index of the element to return
	 * @return the element at the specified position in this list
	 */
	@Override
	default Participant get(int index) {
		return getParticipants().get(index);
	}

	/**
	 * This operation is not implemented by default.
	 *
	 * @param index   index of the element to replace
	 * @param element element to be stored at the specified position
	 * @return the element previously at the specified position
	 *
	 * @throws UnsupportedOperationException this operation is not implemented
	 */
	@Override
	default Participant set(int index, Participant element) {
		throw new UnsupportedOperationException();
	}

	/**
	 * This operation is not implemented by default.
	 *
	 * @param index   index at which the specified element is to be inserted
	 * @param element element to be inserted
	 *
	 * @throws UnsupportedOperationException this operation is not implemented
	 */
	@Override
	default void add(int index, Participant element) {
		throw new UnsupportedOperationException();
	}

	/**
	 * This operation is not implemented by default.
	 *
	 * @param index the index of the element to be removed
	 * @return the element previously at the specified position
	 *
	 * @throws UnsupportedOperationException this operation is not implemented
	 */
	@Override
	default Participant remove(int index) {
		throw new UnsupportedOperationException();
	}

	/**
	 * This operation is not implemented by default.
	 *
	 * @param o element to search for
	 * @return  the index of the first occurrence of the specified element in this list,
	 *          or -1 if this list does not contain the element
	 *
	 * @throws UnsupportedOperationException this operation is not implemented
	 */
	@Override
	default int indexOf(Object o) {
		throw new UnsupportedOperationException();
	}

	/**
	 * This operation is not implemented by default.
	 *
	 * @param o element to search for
	 * @return  the index of the last occurrence of the specified element in
	 *          this list, or -1 if this list does not contain the element
	 *
	 * @throws UnsupportedOperationException this operation is not implemented
	 */
	@Override
	default int lastIndexOf(Object o) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @return a list iterator over the elements in this ParticipantCollection (in proper sequence)
	 */
	@Override
	default ListIterator<Participant> listIterator() {
		return getParticipants().listIterator();
	}

	/**
	 * @param index index of the first element to be returned from the
	 *              list iterator (by a call to {@link ListIterator#next next})
	 * @return  a list iterator over the elements in this list (in proper equence),
	 *          starting at the specified position in the ParticipantCollection
	 */
	@Override
	default ListIterator<Participant> listIterator(int index) {
		return getParticipants().listIterator(index);
	}

	/**
	 * @param fromIndex low endpoint (inclusive) of the subList
	 * @param toIndex   high endpoint (exclusive) of the subList
	 * @return a view of the specified range within this ParticipantCollection
	 */
	@Override
	default List<Participant> subList(int fromIndex, int toIndex) {
		return getParticipants().subList(fromIndex, toIndex);
	}
}
