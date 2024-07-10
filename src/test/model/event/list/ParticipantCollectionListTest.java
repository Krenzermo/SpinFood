package model.event.list;

import model.event.collection.Pair;
import model.event.io.InputData;
import model.event.list.weight.PairingWeights;
import model.person.Participant;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the ParticipantCollectionList class.
 *
 * Tests the functionality of adding, removing, and checking participants and pairs.
 */

class ParticipantCollectionListTest {

	private static ParticipantCollectionList<Pair> pCL;
	private static InputData inputData;

	/**
	 * Sets up the test environment before each test.
	 */
	@BeforeEach
	void setUp() {
		inputData = InputData.getInstanceDebug();
		PairingWeights pairingWeights = new PairingWeights(1, 1, 1);
		pCL = new PairList(pairingWeights);
	}

	/**
	 * Tests if all Participants from the inputData are included in a finished pairList.
	 */
	@Test
	void containsParticipant() {
		for (Participant participant : inputData.getParticipantInputData()) {
			Assertions.assertTrue(pCL.containsParticipant(participant));
		}
	}

	/**
	 * Tests if the containsAllParticipants method correctly returns if all Participants from the inputData are included in a finished pairList.
	 */
	@Test
	void containsAllParticipants() {
		Assertions.assertTrue(pCL.containsAllParticipants(inputData.getParticipantInputData()));
	}

	/**
	 * Tests if the containsAnyParticipants method correctly returns if any Participant from the input is contained in a pairList.
	 */
	@Test
	void containsAnyParticipant() {
		Assertions.assertTrue(pCL.containsAnyParticipant(inputData.getParticipantInputData()));
	}

	/**
	 * Tests if adding a per add(Pair) method works.
	 */
	@Test
	void add() {
		Pair pair = pCL.remove(0);
		Assertions.assertFalse(pCL.contains(pair));
		pCL.add(pair);
		Assertions.assertTrue(pCL.contains(pair));
	}

	/**
	 * Tests if removing a per remove(Pair) method works.
	 */
	@Test
	void remove() {
		Pair pair = pCL.remove(0);
		Assertions.assertFalse(pCL.contains(pair));
		pCL.add(pair);
		Assertions.assertTrue(pCL.contains(pair));
		pCL.remove(pair);
		Assertions.assertFalse(pCL.contains(pair));
	}

	/**
	 * Tests if the contains correctly returns if a list contains a pair
	 */
	@Test
	void contains() {
		Assertions.assertTrue(pCL.contains(inputData.getPairInputData().get(0)));
	}

	/**
	 * Tests if the addAll method correctly adds all pairs from a list of pairs
	 */
	@Test
	void addAll() {
		List<Pair> pairs = new ArrayList<>(pCL.subList(0, 5));
		pCL.removeAll(pairs);
		Assertions.assertFalse(pCL.containsAll(pairs));
		pCL.addAll(pairs);
		Assertions.assertTrue(pCL.containsAll(pairs));
	}

	/**
	 * Tests if the removeAll method correctly removes all pairs from a list of pairs from another list
	 */
	@Test
	void removeAll() {
		List<Pair> pairs = new ArrayList<>(pCL.subList(0, 5));
		pCL.removeAll(pairs);
		Assertions.assertFalse(pCL.containsAll(pairs));
	}

	/**
	 * Tests if retainAll method correctly removes all non specified elements and retains the specified elements
	 */
	@Test
	void retainAll() {
		List<Pair> pairs = new ArrayList<>(pCL.subList(0, 5));
		List<Pair> pCL2 = new ArrayList<>(pCL);
		pCL2.removeAll(pairs);
		pCL.retainAll(pairs);
		Assertions.assertTrue(pCL.containsAll(pairs));
		for (Pair pair : pCL2) {
			Assertions.assertFalse(pCL.contains(pair));
		}
	}
	/**
	 * Tests if the containsAll method correctly checks if all Pairs are contained
	 */
	@Test
	void containsAll() {
		Assertions.assertTrue(pCL.containsAll(inputData.getPairInputData()));
	}
}
