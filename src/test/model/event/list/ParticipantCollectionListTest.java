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

class ParticipantCollectionListTest {

	private static ParticipantCollectionList<Pair> pCL;
	private static InputData inputData;

	@BeforeEach
	void setUp() {
		inputData = InputData.getInstanceDebug();
		PairingWeights pairingWeights = new PairingWeights(1, 1, 1);
		pCL = new PairList(pairingWeights);
	}

	@Test
	void containsParticipant() {
		for (Participant participant : inputData.getParticipantInputData()) {
			Assertions.assertTrue(pCL.containsParticipant(participant));
		}
	}

	@Test
	void containsAllParticipants() {
		Assertions.assertTrue(pCL.containsAllParticipants(inputData.getParticipantInputData()));
	}

	@Test
	void containsAnyParticipant() {
		Assertions.assertTrue(pCL.containsAnyParticipant(inputData.getParticipantInputData()));
	}

	@Test
	void add() {
		Pair pair = pCL.remove(0);
		Assertions.assertFalse(pCL.contains(pair));
		pCL.add(pair);
		Assertions.assertTrue(pCL.contains(pair));
	}

	@Test
	void remove() {
		Pair pair = pCL.remove(0);
		Assertions.assertFalse(pCL.contains(pair));
		pCL.add(pair);
		Assertions.assertTrue(pCL.contains(pair));
		pCL.remove(pair);
		Assertions.assertFalse(pCL.contains(pair));
	}

	@Test
	void contains() {
		Assertions.assertTrue(pCL.contains(inputData.getPairInputData().get(0)));
	}

	@Test
	void addAll() {
		List<Pair> pairs = new ArrayList<>(pCL.subList(0, 5));
		pCL.removeAll(pairs);
		Assertions.assertFalse(pCL.containsAll(pairs));
		pCL.addAll(pairs);
		Assertions.assertTrue(pCL.containsAll(pairs));
	}

	@Test
	void removeAll() {
		List<Pair> pairs = new ArrayList<>(pCL.subList(0, 5));
		pCL.removeAll(pairs);
		Assertions.assertFalse(pCL.containsAll(pairs));
	}

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

	@Test
	void containsAll() {
		Assertions.assertTrue(pCL.containsAll(inputData.getPairInputData()));
	}
}
