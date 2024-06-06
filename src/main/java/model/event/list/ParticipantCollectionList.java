package model.event.list;

import model.event.collection.ParticipantCollection;
import model.event.list.identNumbers.IdentNumber;
import model.person.Participant;

import java.util.*;

public interface ParticipantCollectionList extends List<ParticipantCollection> {
	/**
	 * @return the {@link IdentNumber} (Identifying Numbers) of this ParticipantCollectionList
	 */
	IdentNumber getIdentNumber();

	/**
	 * @return the evaluation of this ParticipantCollection
	 */
	double evaluate(); // TODO: create methode evaluate() and make it useful

	/**
	 * This method is used to change the underlying data structure.
	 * It is used by the default implementations of {@link #add}, {@link #remove}, {@link #addUnsafe},
	 * {@link #addAll} and {@link #removeAll}methods.
	 *
	 * @return the data structure that stores the instances of {@link ParticipantCollection}
	 */
	List<ParticipantCollection> getDataStructure();

	/**
	 * Checks if the collection has the same type as this ParticipantCollectionList (subclass)
	 *
	 * @param collection the element to be checked
	 * @throws IllegalArgumentException if the type check fails
	 */
	void checkType(ParticipantCollection collection);

	/**
	 * This method is used to get the instances of {@link ParticipantCollection} in this ParticipantCollectionList
	 * without changing the underlying data structure.
	 *
	 * @return a mutable List containing all instances of {@link ParticipantCollection} in this ParticipantCollectionList
	 */
	default List<ParticipantCollection> getParticipantCollections() {
		return new ArrayList<>(getDataStructure());
	}

	/**
	 * clears this ParticipantCollectionList and adds all elements of list
	 *
	 * @param list the list to be set as this
	 * @throws IllegalArgumentException if the type check fails
	 */
	default void setList(List<? extends ParticipantCollection> list) {
		for (ParticipantCollection participantCollection : list) {
			checkType(participantCollection);
		}
		clear();
		addAll(list);
	}


	// Participant Methods:

	/**
	 * @return a List containing all unique instances of {@link Participant} in this ParticipantCollectionList
	 */
	default List<Participant> getParticipants() {
		HashSet<Participant> participants = new HashSet<>();
		for (ParticipantCollection participantCollection : getParticipantCollections()) {
			participants.addAll(participantCollection.getParticipants());
		}
		return participants.stream().toList(); // TODO: maybe return a modifiable List instead
	}

	/**
	 * @return the amount of unique instances of {@link Participant} in this ParticipantCollectionList
	 */
	default int getParticipantCount() {
		return getParticipants().size();
	}

	/**
	 * @param participant {@link Participant} whose presence in this list is to be tested
	 * @return true if this ParticipantCollectionList contains a {@link ParticipantCollection} containing the participant
	 */
	default boolean containsParticipant(Participant participant) {
		for (ParticipantCollection participantCollection : getParticipantCollections()) {
			if (participantCollection.getParticipants().contains(participant)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @param participants elements whose presence in this list is to be tested
	 * @return {@code true} if all elements are contained in this ParticipantCollectionList, {@link false} otherwise
	 */
	default boolean containsAllParticipants(Collection<Participant> participants) {
		return new HashSet<>(getParticipants()).containsAll(participants);
	}

	/**
	 * @param participants elements whose presence in this list is to be tested
	 * @return {@code true} if any element is contained in this ParticipantCollectionList, {@link false} otherwise
	 */
	default boolean containsAnyParticipant(Collection<Participant> participants) {
		for (Participant participant: participants) {
			if (containsParticipant(participant)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @return an array containing all unique instances of {@link Participant} in this ParticipantCollectionList
	 */
	default Participant[] getParticipantArray() {
		return getParticipants().toArray(Participant[]::new);
	}

	/**
	 * @return an Iterator over all unique instances of {@link Participant} in this ParticipantCollectionList
	 */
	default Iterator<Participant> getParticipantIterator() {
		return getParticipants().iterator();
	}

	/**
	 * @return a list iterator over all unique instances of {@link Participant} in this ParticipantCollectionList
	 */
	default ListIterator<Participant> getParticipantListIterator() {
		return getParticipants().listIterator();
	}

	/**
	 * @param index index of the element to return
	 * @return the element at the specified position in this list
	 * @throws IndexOutOfBoundsException if the index is out of range ({@code index < 0 || index > size()})
	 */
	default Participant getParticipant(int index) {
		return getParticipants().get(index);
	}


	// Utility Methods:

	/**
	 * @return {@code true} if all elements in this List are of the same type, {@code false} otherwise.
	 */
	default boolean isHomogenous() {
		if (isEmpty()) {
			return true;
		}

		Class<? extends ParticipantCollection> firstElement = getParticipantCollections().get(0).getClass();
		for (ParticipantCollection participantCollection : getParticipantCollections()) {
			if (!firstElement.isInstance(participantCollection)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Checks if this List is homogenous and returns the {@link Class} if it is.
	 * Returns {@code ParticipantCollection.class} if this List is empty or contains multiple different implementations.
	 *
	 * @return the {@link Class} of the elements in this ParticipantCollectionList
	 */
	default Class<? extends ParticipantCollection> getListElementsClass() {
		if (!isHomogenous() || isEmpty()) {
			return ParticipantCollection.class;
		}
		return getParticipantCollections().get(0).getClass();
	}

	/**
	 * @param collection the collection to be checked
	 * @throws NullPointerException if the collection is or contains null
	 */
	private void checkCollectionIsNotNull(ParticipantCollection collection) {
		// TODO: may need to be changed if ParticipantCollection.contains() throws a NullPointerException
		if (collection.contains(null)) {
			throw new NullPointerException("Participants must not be null!");
		}
	}

	/**
	 * Checks if the specified collection could be added to this ParticipantCollectionList
	 *
	 * @param collection the collection to be checked
	 * @throws NullPointerException if the collection is or contains null
	 * @throws IllegalArgumentException if any {@link Participant} is already in this ParticipantCollectionList
	 */
	private void checkCollection(ParticipantCollection collection) {
		checkCollectionIsNotNull(collection);
		checkType(collection);
		if (getParticipantCollections().contains(collection)) {
			throw new IllegalArgumentException("ParticipantCollectionList already contains this collection!");
		}
		if (containsAnyParticipant(collection)) {
			throw new IllegalArgumentException("ParticipantCollectionList already contains a participant ");
		}
	}

	// Unsafe add methods:

	/**
	 * Adds the specified Collection without checking for duplicates
	 *
	 * @param collection element whose presence in this collection is to be ensured
	 * @return {@code true} if the operation was successful, {@code false} otherwise
	 * @throws NullPointerException if the collection or a participant is null
	 */
	default boolean addUnsafe(ParticipantCollection collection) {
		checkCollectionIsNotNull(collection);
		return getDataStructure().add(collection);
	}


	// Inherited Methods:

	/**
	 * Adds the specified {@link ParticipantCollection} to this List.
	 * The element will not be added if this ParticipantCollectionList
	 * already contains one of the instances of {@link Participant}.
	 * Use {@link #addUnsafe} to allow duplicates
	 *
	 * @param collection element whose presence in this collection is to be ensured
	 * @return {@code true} if the operation was successful, {@code false} otherwise
	 * @throws NullPointerException if the collection or a participant is null
	 * @throws IllegalArgumentException if any {@link Participant} is already in this ParticipantCollectionList
	 */
	@Override
	default boolean add(ParticipantCollection collection) {
		checkCollection(collection);
		return getDataStructure().add(collection);
	}

	/**
	 * @param object element to be removed from this list, if present
	 * @return {@code true} if the operation was successful, {@code false} otherwise
	 * @throws NullPointerException if the collection or a participant is null
	 * @throws ClassCastException if the Object is not of type {@link ParticipantCollection}
	 */
	@Override
	default boolean remove(Object object) {
		checkCollectionIsNotNull((ParticipantCollection) object);
		return getDataStructure().remove(object);
	}

	/**
	 * @return the size of this ParticipantCollectionList
	 */
	@Override
	default int size() {
		return getParticipantCollections().size();
	}

	/**
	 * @return {@code true} if this ParticipantCollectionList is empty, {@code false} otherwise
	 */
	@Override
	default boolean isEmpty() {
		return getParticipantCollections().isEmpty();
	}

	/**
	 * Checks if this ParticipantCollectionList contains the specified Object.
	 * Only true if the Object is a {@link ParticipantCollection}.
	 * Use {@link #containsParticipant} to check for Participants
	 *
	 * @param o element whose presence in this list is to be tested
	 * @return {@code true} if the element is contained in this ParticipantCollectionList, {@code false} otherwise
	 * @throws NullPointerException if the collection or a participant is null
	 * @throws ClassCastException if the Object is not of type {@link ParticipantCollection}
	 */
	@Override
	default boolean contains(Object o) {
		checkCollectionIsNotNull((ParticipantCollection) o);
		return getParticipantCollections().contains(o);
	}

	/**
	 * @return an Iterator over all instances of {@link Participant} in this ParticipantCollectionList
	 */
	@Override
	default Iterator<ParticipantCollection> iterator() {
		return getParticipantCollections().listIterator();
	}

	/**
	 * @return an array containing all instances of {@link ParticipantCollection} in this ParticipantCollectionList
	 */
	@Override
	default Object[] toArray() {
		return getParticipantCollections().toArray();
	}

	/**
	 * @param a   the array into which the elements of this list are to
	 *            be stored, if it is big enough; otherwise, a new array of the
	 *            same runtime type is allocated for this purpose.
	 * @param <T> the type of the array
	 * @return an array containing the elements of this list
	 * @throws ArrayStoreException if the runtime type of the specified array
	 *          is not a supertype of the runtime type of every element in this list
	 * @throws NullPointerException if the specified array is null
	 */
	@Override
	default <T> T[] toArray(T[] a) {
		return getParticipantCollections().toArray(a);
	}

	/**
	 * Checks if this ParticipantCollectionList contains all specified elements.
	 * Only true if the Object is a {@link ParticipantCollection}.
	 * Use {@link #containsAllParticipants} to check for Participants
	 *
	 * @param c collection to be checked for containment in this list
	 * @return {@code true} if this ParticipantCollectionList contains all elements of c, {@code false} otherwise
	 * @throws ClassCastException if the types of one or more elements
	 *          in the specified collection are incompatible with this list
	 */
	@Override
	default boolean containsAll(Collection<?> c) {
		return new HashSet<>(getParticipantCollections()).containsAll(c);
	}

	/**
	 * @param c collection containing elements to be added to this collection
	 * @return {@code true} if this list changed as a result of the call, {@code false} otherwise
	 * @throws NullPointerException if the collection is or contains null
	 * @throws IllegalArgumentException if a Collection or a Participant is already in this ParticipantCollectionList
	 */
	@Override
	default boolean addAll(Collection<? extends ParticipantCollection> c) {
		return addAll(size(), c);
	}

	/**
	 * @param index index at which to insert the first element from the
	 *              specified collection
	 * @param c     collection containing elements to be added to this list
	 * @return {@code true} if this list changed as a result of the call, {@code false} otherwise
	 * @throws NullPointerException if any collection is or contains null
	 * @throws IllegalArgumentException if any {@link ParticipantCollection} or any {@link Participant}
	 *          is already in this ParticipantCollectionList
	 * @throws IndexOutOfBoundsException if the index is out of range ({@code index < 0 || index > size()})
	 */
	@Override
	default boolean addAll(int index, Collection<? extends ParticipantCollection> c) {
		for (ParticipantCollection participantCollection : c) {
			checkCollection(participantCollection);
		}
		return getDataStructure().addAll(index, c);
	}

	/**
	 * @param c collection containing elements to be removed from this list
	 * @return {@code true} if this list changed as a result of the call, {@code false} otherwise
	 * @throws NullPointerException if the collection is or contains null
	 */
	@Override
	default boolean removeAll(Collection<?> c) {
		if (c.contains(null)) {
			throw new NullPointerException("ParticipantCollection must not be null!");
		}
		return getDataStructure().removeAll(c);
	}

	/**
	 * @param c collection containing elements to be retained in this list
	 * @return {@code true} if this list changed as a result of the call, {@code false} otherwise
	 * @throws NullPointerException if the collection is or contains null
	 */
	@Override
	default boolean retainAll(Collection<?> c) {
		if (c.contains(null)) {
			throw new NullPointerException("ParticipantCollection must not be null!");
		}
		return getDataStructure().retainAll(c);
	}

	/**
	 * Removes all the elements from this List.
	 * The list will be empty after this call returns.
	 */
	@Override
	default void clear() {
		getDataStructure().clear();
	}

	/**
	 * @param index index of the element to return
	 * @return the element at the specified position in this list
	 * @throws IndexOutOfBoundsException if the index is out of range ({@code index < 0 || index > size()})
	 */
	@Override
	default ParticipantCollection get(int index) {
		return getParticipantCollections().get(index);
	}

	/**
	 * @param index   index of the element to replace
	 * @param element element to be stored at the specified position
	 * @return the element previously at the specified position
	 * @throws NullPointerException if element is or contains null
	 * @throws IllegalArgumentException if this list already contains the {@link ParticipantCollection} or a {@link Participant}
	 * @throws IndexOutOfBoundsException if the index is out of range ({@code index < 0 || index > size()})
	 */
	@Override
	default ParticipantCollection set(int index, ParticipantCollection element) {
		checkCollection(element);
		return getDataStructure().set(index, element);
	}

	/**
	 * @param index   index at which the specified element is to be inserted
	 * @param element element to be inserted
	 * @throws NullPointerException if element is or contains null
	 * @throws IllegalArgumentException if this list already contains the {@link ParticipantCollection} or a {@link Participant}
	 * @throws IndexOutOfBoundsException if the index is out of range ({@code index < 0 || index > size()})
	 */
	@Override
	default void add (int index, ParticipantCollection element) {
		checkCollection(element);
		getDataStructure().add(index, element);
	}

	/**
	 * @param index the index of the element to be removed
	 * @return the element previously at the specified position
	 * @throws IndexOutOfBoundsException if the index is out of range ({@code index < 0 || index > size()})
	 */
	@Override
	default ParticipantCollection remove(int index) {
		return getDataStructure().remove(index);
	}

	/**
	 * @param o element to search for
	 * @return the index of the first occurrence of the specified element in this list,
	 *          or -1 if this list does not contain the element
	 * @throws NullPointerException if the specified element is null
	 * @throws ClassCastException if the type of the specified element is incompatible with this list
	 */
	@Override
	default int indexOf(Object o) {
		checkCollectionIsNotNull((ParticipantCollection) o);
		try {
			checkType((ParticipantCollection) o);
		} catch (IllegalArgumentException e) {
			throw new ClassCastException(e.getMessage());
		}
		return getParticipantCollections().indexOf(o);
	}

	/**
	 * @param o element to search for
	 * @return the index of the last occurrence of the specified element in
	 *          this list, or -1 if this list does not contain the element
	 * @throws NullPointerException if the specified element is null
	 * @throws ClassCastException if the type of the specified element is incompatible with this list
	 */
	@Override
	default int lastIndexOf(Object o) {
		checkCollectionIsNotNull((ParticipantCollection) o);
		try {
			checkType((ParticipantCollection) o);
		} catch (IllegalArgumentException e) {
			throw new ClassCastException(e.getMessage());
		}
		return getParticipantCollections().lastIndexOf(o);
	}

	/**
	 * @return a list iterator over the elements in this ParticipantCollectionList (in proper sequence)
	 */
	@Override
	default ListIterator<ParticipantCollection> listIterator() {
		return getParticipantCollections().listIterator();
	}

	/**
	 * @param index index of the first element to be returned from the
	 *              list iterator (by a call to {@link ListIterator#next next})
	 * @return a list iterator over the elements in this list (in proper sequence),
	 * 	 *          starting at the specified position in the ParticipantCollectionList
	 * @throws IndexOutOfBoundsException if the index is out of range ({@code index < 0 || index > size()})
	 */
	@Override
	default ListIterator<ParticipantCollection> listIterator(int index) {
		return getParticipantCollections().listIterator(index);
	}

	/**
	 * @param fromIndex low endpoint (inclusive) of the subList
	 * @param toIndex   high endpoint (exclusive) of the subList
	 * @return a view of the specified range within this ParticipantCollection
	 * @throws IndexOutOfBoundsException if the index is out of range ({@code index < 0 || index > size()})
	 */
	@Override
	default List<ParticipantCollection> subList(int fromIndex, int toIndex) {
		return getParticipantCollections().subList(fromIndex, toIndex);
	}
}
