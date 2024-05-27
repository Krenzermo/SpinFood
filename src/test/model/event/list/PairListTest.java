package model.event.list;

import model.event.InputData;
import model.event.collection.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PairListTest {
	@Test
	void pairListTest() {
		InputData inputData = InputData.getInstance();
		PairList pairList = new PairList();
		pairList.setList(inputData.getPairInputData());

		Assertions.assertTrue(pairList.containsAll(inputData.getPairInputData()));
	}
}
