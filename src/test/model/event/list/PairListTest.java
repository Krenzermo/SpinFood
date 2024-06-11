package model.event.list;

import model.event.io.InputData;
import model.event.list.weight.PairingWeights;
import model.event.collection.Pair;
import model.person.FoodType;
import model.person.Participant;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PairListTest {
	@Test
	void pairListTest() {
		InputData inputData = InputData.getInstance();
		PairingWeights pairingWeights = new PairingWeights(1, 1, 1);
		PairList pairList = new PairList(inputData, pairingWeights);

		Assertions.assertTrue(allPairLegal(pairList));
	}

	@Test
	void pairListTestVariedPairingWeights() {
		InputData inputData = InputData.getInstance();

		PairingWeights pairingWeights = new PairingWeights(1, 3, 5);
		PairingWeights pairingWeights1 = new PairingWeights(1, 1, 1);

		PairList pairList1 = new PairList(inputData, pairingWeights);
		PairList pairList2 = new PairList(inputData, pairingWeights1);

		assert allPairLegal(pairList1) && allPairLegal(pairList2);

        assertNotEquals(pairList1.getPairs(), pairList2.getPairs());

	}

	boolean isPairLegal(Pair pair) {
		if (!pair.hasKitchen()) {
			return false;
		}

		List<FoodType> foodTypes = pair.getParticipants().stream().map(Participant::getFoodType).toList();

		if (foodTypes.get(0) == foodTypes.get(1)) {
			return foodTypes.get(0) == pair.getFoodType();
		}

		if (!(foodTypes.contains(FoodType.VEGGIE)) && !(foodTypes.contains(FoodType.VEGAN))) {
			return pair.getFoodType() == FoodType.MEAT;
		}
		int value = foodTypes.stream().mapToInt(FoodType::getValue).max().getAsInt();
		return pair.getFoodType() == FoodType.herbiFromValue(value);

	}

	boolean allPairLegal(PairList pairList) {
		for (Pair pair: pairList.getPairs()) {
			if (!isPairLegal(pair)) {
				return false;
			}
		}

		return true;
	}
}
