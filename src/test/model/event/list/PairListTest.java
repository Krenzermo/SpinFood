package model.event.list;

import model.event.io.InputData;
import model.event.list.weight.PairingWeights;
import model.event.collection.Pair;
import model.person.FoodType;
import model.person.Participant;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class PairListTest {
	@Test
	void pairListTest() {
		InputData inputData = InputData.getInstanceDebug();
		PairingWeights pairingWeights = new PairingWeights(1, 1, 1);
		PairList pairList = new PairList(pairingWeights);

		Assertions.assertTrue(allPairsLegal(pairList));
	}

	@Test
	void pairListTestVariedPairingWeights() {
		InputData inputData = InputData.getInstanceDebug();

		PairingWeights pairingWeights = new PairingWeights(1, 3, 5);
		PairingWeights pairingWeights1 = new PairingWeights(1, 1, 1);

		PairList pairList1 = new PairList(pairingWeights);
		PairList pairList2 = new PairList(pairingWeights1);

		Assertions.assertTrue(allPairsLegal(pairList1) && allPairsLegal(pairList2));

        Assertions.assertFalse(pairList1.containsAll(pairList2.getPairs()));
		Assertions.assertFalse(pairList2.containsAll(pairList1.getPairs()));
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

	boolean allPairsLegal(PairList pairList) {
		for (Pair pair: pairList.getPairs()) {
			if (!isPairLegal(pair)) {
				return false;
			}
		}

		return true;
	}
}
