package model.event.list;

import model.event.InputData;
import model.event.collection.Pair;
import model.person.Participant;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PairListTest {
	@Test
	void pairListTest() {
		InputData inputData = InputData.getInstance();
		PairList pairList = new PairList();
		pairList.setList(inputData.getPairInputData());
		List<Participant> participantList = pairList.getParticipants();

		Assertions.assertTrue(pairList.containsAll(inputData.getPairInputData()));
		Assertions.assertTrue(pairList.containsAnyParticipant(participantList));

		pairList.removeAll(inputData.getPairInputData());
		Assertions.assertFalse(pairList.containsAll(inputData.getPairInputData()));
		Assertions.assertFalse(pairList.containsAnyParticipant(participantList));
	}
}
