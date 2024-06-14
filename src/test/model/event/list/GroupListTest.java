package model.event.list;

import model.event.collection.Group;
import model.event.collection.Pair;
import model.event.io.InputData;
import model.event.list.identNumbers.IdentNumber;
import model.event.list.weight.GroupWeights;
import model.event.list.weight.PairingWeights;
import model.kitchen.Kitchen;
import model.person.FoodType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

class GroupListTest {
	private static PairList pairs;

	@BeforeEach
	void setUp() {
		InputData inputData = InputData.getInstance();
		PairingWeights pairingWeights = new PairingWeights(1, 1, 1);
		pairs = new PairList(inputData, pairingWeights);
	}

	@Test
	void groupListTest() {
		GroupWeights weights = new GroupWeights(1,1,1,1);
		GroupList groups = new GroupList(pairs, weights);

		Assertions.assertTrue(allGroupsLegal(groups));
		Assertions.assertTrue(eachGroupIdContainedThrice(groups));
		Assertions.assertTrue(eachKitchenOnlyUsedOncePerCourse(groups));
	}

	@Test
	void groupedListTestVariedGroupWeights() {
		GroupWeights weights1 = new GroupWeights(1,2,0,1);
		GroupWeights weights2 = new GroupWeights(2,0,1,2);

		GroupList groups1 = new GroupList(pairs, weights1);
		Assertions.assertTrue(allGroupsLegal(groups1));
		Assertions.assertTrue(eachGroupIdContainedThrice(groups1));
		Assertions.assertTrue(eachKitchenOnlyUsedOncePerCourse(groups1));

		IdentNumber identNumber1 = groups1.getIdentNumber();

		for (Pair pair : pairs) {
			pair.clearGroups();
		}

		GroupList groups2 = new GroupList(pairs, weights2);
		Assertions.assertTrue(allGroupsLegal(groups2));
		Assertions.assertTrue(eachGroupIdContainedThrice(groups2));
		Assertions.assertTrue(eachKitchenOnlyUsedOncePerCourse(groups2));

		Assertions.assertNotEquals(identNumber1.toString(), groups2.getIdentNumber().toString());
	}

	/**
	 * Checks if the specified {@link Group} is legal
	 *
	 * @param group the {@link Group} to be checked
	 * @return {@code true} if the {@link Group} is legal, {@code false} otherwise
	 */
	boolean isGroupLegal(Group group) {
		return !containsMoreMeatAndNoneThanVeggieAndVegan(group) && !GroupContainsPairMoreThanOnce(group);
	}

	/**
	 * Checks if the specified {@link Group} contains more Meat/None than Veggie/Vegan.
	 *
	 * @param group the {@link Group} to be checked
	 * @return {@code true} if the {@link Group} contains more Meat/None than Veggie/Vegan, {@code false} otherwise
	 */
	boolean containsMoreMeatAndNoneThanVeggieAndVegan(Group group) {
		FoodType[] foodTypes = new FoodType[] {group.getPairs()[0].getFoodType(), group.getPairs()[1].getFoodType(), group.getPairs()[2].getFoodType()};

		Map<Integer, Long> map = Arrays.stream(foodTypes)
				.collect(Collectors.groupingBy(FoodType::getValue, Collectors.counting()));

		if (!map.containsKey(FoodType.VEGGIE.getValue()) && !map.containsKey(FoodType.VEGAN.getValue())) {
			return false;
		}

		int amountMeatOrNone = 0;

		if (FoodType.MEAT.getValue() == FoodType.NONE.getValue()) { // is currently the case
			if (map.containsKey(FoodType.MEAT.getValue())) {
				amountMeatOrNone += map.get(FoodType.MEAT.getValue());
			}
		}
		else { // redundancy in case this gets changed
			if (map.containsKey(FoodType.MEAT.getValue())) {
				amountMeatOrNone += map.get(FoodType.MEAT.getValue());
			}
			if (map.containsKey(FoodType.NONE.getValue())) {
				amountMeatOrNone += map.get(FoodType.NONE.getValue());
			}
		}

		return amountMeatOrNone >= 2;
	}

	/**
	 * Checks if the specified {@link Group} contains any {@link Pair} more than once.
	 *
	 * @param group the {@link Group} to be checked
	 * @return {@code true} if the {@link Group} contains any {@link Pair} more than once, {@code false} otherwise
	 */
	boolean GroupContainsPairMoreThanOnce(Group group) {
		return Arrays.stream(group.getPairs()).collect(Collectors.groupingBy(Pair::hashCode, Collectors.counting())).size() != 3;
	}

	/**
	 * Checks if each {@link Group} is referenced exactly three times.
	 *
	 * @return {@code true} if each {@link Group} is referenced exactly three times, {@code false} otherwise
	 */
	 boolean eachGroupIdContainedThrice(GroupList groupList) {
		return groupList.getPairList().stream()
				.flatMap(pair -> pair.getGroups().stream())
				.filter(group -> !Objects.isNull(group))
				.collect(Collectors.groupingBy(Group::getId, Collectors.counting()))
				.values()
				.stream()
				.noneMatch(count -> count != 3);
	}

	boolean eachKitchenOnlyUsedOncePerCourse(GroupList groupList) {
		 return groupList.stream()
				 .collect(Collectors.groupingBy(Group::getKitchen))
				 .values()
				 .stream()
				 .map(this::getKitchenList)
				 .allMatch(this::containsEachKitchenExactlyOnce);
	}

	List<Kitchen> getKitchenList(List<Group> groupList) {
		 List<Kitchen> kitchens = new ArrayList<>();

		 groupList.forEach(group -> kitchens.add(group.getKitchen()));
		 return kitchens;
	}

	boolean containsEachKitchenExactlyOnce(List<Kitchen> kitchens) {
		 List<Kitchen> temp = new ArrayList<>(kitchens);

		 for (Kitchen kitchen : kitchens) {
			 temp.remove(kitchen);
			 if (temp.contains(kitchen)) {
				 return false;
			 }
		 }
		 return true;
	}

	/**
	 * Checks if the specified instances of {@link Group} are legal
	 *
	 * @param groupList the {@link GroupList} containing the instances of {@link Group}
	 * @return {@code true} if all instances of {@link Group} are legal, {@code false} otherwise
	 */
	boolean allGroupsLegal(GroupList groupList) {
		for (Group group : groupList.getGroups()) {
			if (!isGroupLegal(group)) {
				return false;
			}
		}
		return true;
		// return eachGroupIdContainedThrice(); // optional other approach
	}
}
