package model;

import model.kitchen.Kitchen;
import model.person.Participant;

import java.util.*;

/**
 * @author Finn Brecher
 */
public interface IParticipantCollection extends List<Participant> {
	IIdentNumber getIdentNumber();
	List<Participant> getParticipants();
	Kitchen getKitchen();
	double evaluate();
	int getAgeDifference();
	Course getCourse();

	default int size() {
		return getParticipants().size();
	}

	default boolean isEmpty() {
		return getParticipants().isEmpty();
	}

	default boolean contains(Object o) {
		if (o instanceof Participant) {
			return getParticipants().contains(o);
		}
		return false;
	}

	default Iterator<Participant> iterator() {
		return getParticipants().iterator();
	}

	default Object[] toArray() {
		return getParticipants().toArray();
	}

	default <T> T[] toArray(T[] a) {
		return getParticipants().toArray(a);
	}

	default boolean containsAll(Collection<?> c) {
		return new HashSet<>(getParticipants()).containsAll(c);
	}

	default boolean addAll(Collection<? extends Participant> c) {
		throw new UnsupportedOperationException();
	}

	default boolean addAll(int index, Collection<? extends Participant> c) {
		throw new UnsupportedOperationException();
	}

	default boolean removeAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	default boolean retainAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	default void clear() {
		throw new UnsupportedOperationException();
	}

	default Participant get(int index) {
		return getParticipants().get(index);
	}

	default Participant set(int index, Participant element) {
		throw new UnsupportedOperationException();
	}

	default void add(int index, Participant element) {
		throw new UnsupportedOperationException();
	}

	default Participant remove(int index) {
		throw new UnsupportedOperationException();
	}

	default int indexOf(Object o) {
		throw new UnsupportedOperationException();
	}

	default int lastIndexOf(Object o) {
		throw new UnsupportedOperationException();
	}

	default ListIterator<Participant> listIterator() {
		return getParticipants().listIterator();
	}

	default ListIterator<Participant> listIterator(int index) {
		return getParticipants().listIterator(index);
	}

	default List<Participant> subList(int fromIndex, int toIndex) {
		return getParticipants().subList(fromIndex, toIndex);
	}
}
