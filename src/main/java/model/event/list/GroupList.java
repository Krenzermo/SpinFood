package model.event.list;

import model.event.Course;
import model.event.list.weight.GroupWeights;
import model.event.collection.Group;
import model.event.collection.Pair;
import model.event.collection.ParticipantCollection;
import model.event.list.identNumbers.GroupIdentNumber;
import model.event.list.identNumbers.IdentNumber;
import model.person.FoodType;
import model.person.Participant;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * The GroupList class represents a collection of Groups of Pairs.
 * It provides functionality to group participants based on various criteria
 * and maintains a list of unpaired participants (successors).
 *
 * This class extends ParticipantCollectionList and uses GroupWeights for grouping logic.
 *
 * @see GroupWeights
 * @see model.event.collection.Group
 * @see model.event.collection.Pair
 * @see model.event.list.ParticipantCollectionList
 * @see model.person.Participant
 * @see model.person.FoodType
 * @see model.kitchen.Kitchen
 * @see model.event.Course
 *
 *
 * @author Daniel Hinkelmann
 * @author Finn Brecher
 * @author Davide Piacenza
 * @author Ole Krenzer
 */
public class GroupList extends ParticipantCollectionList<Group> {

	private final PairList pairList;
	private IdentNumber identNumber;
	private static final List<Participant> successors = new ArrayList<>();
	private static final List<Pair> successorPairs = new ArrayList<>();

	/**
	 * Constructs a GroupList instance.
	 *
	 * @param pairList     the list of pairs to be grouped
	 * @param groupWeights the weights used for grouping criteria
	 */
	public GroupList(PairList pairList, GroupWeights groupWeights) {
		List<Pair> sortedPairList = sortPairs(pairList);
		setList(buildBestGroups(sortedPairList, groupWeights));
		this.identNumber = getIdentNumber();
		this.pairList = pairList;
		this.identNumber = deriveIdentNumber();
	}

	private GroupIdentNumber deriveIdentNumber() {

		return new GroupIdentNumber(this);
	}

	/**
	 * Sorts pairs based on their FoodType in a specific order: MEAT, NONE, VEGGIE, VEGAN.
	 *
	 * @param pairList the list of pairs to be sorted
	 * @return the sorted list of pairs
	 */
	private List<Pair> sortPairs(PairList pairList) {
		List<Pair> sortedPairList = new ArrayList<>();

		for (Pair pair : pairList) {
			if (pair.getFoodType() == FoodType.MEAT) {
				sortedPairList.add(pair);
			}
		}

		for (Pair pair : pairList) {
			if (pair.getFoodType() == FoodType.NONE) {
				sortedPairList.add(pair);
			}
		}

		for (Pair pair : pairList) {
			if (pair.getFoodType() == FoodType.VEGGIE) {
				sortedPairList.add(pair);
			}
		}

		for (Pair pair : pairList) {
			if (pair.getFoodType() == FoodType.VEGAN) {
				sortedPairList.add(pair);
			}
		}

		return sortedPairList;
	}

	/**
	 * Builds the best groups of pairs based on the provided GroupWeights.
	 * Groups are created in batches of 9 pairs each.
	 *
	 * @param pairList     the list of pairs to be grouped
	 * @param groupWeights the weights used for grouping criteria
	 * @return the list of groups
	 */
	private static List<Group> buildBestGroups(List<Pair> pairList, GroupWeights groupWeights) {
		List<Pair> sortedPairList = new ArrayList<>(pairList);
		List<Group> bestGroupList = new ArrayList<>();

		while (sortedPairList.size() >= 9) {
			List<Pair> matchedPairList = new ArrayList<>();
			Pair pair1 = sortedPairList.remove(0);
			matchedPairList.add(pair1);

			for (int i = 0; i < 8; i++) {
				int bestPairPosition = -1;
				double bestPairScore = -5000;

				for (int j = 0; j < sortedPairList.size(); j++) {
					Pair testedPair = sortedPairList.get(j);
					double score = calculateGroupScore(pair1, testedPair, groupWeights, matchedPairList);

					if (score > bestPairScore) {
						bestPairScore = score;
						bestPairPosition = j;
					}
				}

				if (bestPairPosition == -1) {
					break;
				}

				matchedPairList.add(sortedPairList.remove(bestPairPosition));
			}

			if (matchedPairList.size() != 9) {
				successorPairs.add(matchedPairList.remove(0));
				sortedPairList.addAll(matchedPairList);
			} else {
				bestGroupList.addAll(makeGroups(matchedPairList));
			}
		}

		successorPairs.addAll(sortedPairList);

		return bestGroupList;
	}

	/**
	 * Creates groups from a list of 9 matched pairs.
	 *
	 * @param matchedPairList the list of matched pairs
	 * @return the list of groups
	 */
	private static List<Group> makeGroups(List<Pair> matchedPairList) {
		List<Group> groupList = new ArrayList<>();
		matchedPairList.sort(Comparator.comparingDouble(Pair::getDistance).reversed());

		int vegCount = listCountFoodType(matchedPairList, FoodType.VEGGIE) + listCountFoodType(matchedPairList, FoodType.VEGAN);
		int meatCount = listCountFoodType(matchedPairList, FoodType.MEAT) + listCountFoodType(matchedPairList, FoodType.NONE);
		if (vegCount > 0) {
			if (meatCount == 2) {
				List<Integer> meatPositions = new ArrayList<>(findMeatPosition(matchedPairList));
				matchedPairList = swapPositions(matchedPairList, meatPositions, 2);
			} else if (meatCount == 3) {
				List<Integer> meatPositions = new ArrayList<>(findMeatPosition(matchedPairList));
				matchedPairList = swapPositions(matchedPairList, meatPositions, 3);
			}
		}

		Group group1 = new Group(matchedPairList.get(0), matchedPairList.get(3), matchedPairList.get(6), Course.STARTER, matchedPairList.get(0).getKitchen());
		Group group2 = new Group(matchedPairList.get(1), matchedPairList.get(4), matchedPairList.get(7), Course.STARTER, matchedPairList.get(1).getKitchen());
		Group group3 = new Group(matchedPairList.get(2), matchedPairList.get(5), matchedPairList.get(8), Course.STARTER, matchedPairList.get(2).getKitchen());
		Group group4 = new Group(matchedPairList.get(0), matchedPairList.get(4), matchedPairList.get(8), Course.MAIN, matchedPairList.get(4).getKitchen());
		Group group5 = new Group(matchedPairList.get(1), matchedPairList.get(5), matchedPairList.get(6), Course.MAIN, matchedPairList.get(5).getKitchen());
		Group group6 = new Group(matchedPairList.get(2), matchedPairList.get(3), matchedPairList.get(7), Course.MAIN, matchedPairList.get(3).getKitchen());
		Group group7 = new Group(matchedPairList.get(0), matchedPairList.get(5), matchedPairList.get(7), Course.DESSERT, matchedPairList.get(7).getKitchen());
		Group group8 = new Group(matchedPairList.get(1), matchedPairList.get(3), matchedPairList.get(8), Course.DESSERT, matchedPairList.get(8).getKitchen());
		Group group9 = new Group(matchedPairList.get(2), matchedPairList.get(4), matchedPairList.get(6), Course.DESSERT, matchedPairList.get(6).getKitchen());

		matchedPairList.get(0).setGroups(new Group[] {group1, group4, group7});
		matchedPairList.get(1).setGroups(new Group[] {group2, group5, group8});
		matchedPairList.get(2).setGroups(new Group[] {group3, group6, group9});
		matchedPairList.get(3).setGroups(new Group[] {group1, group6, group8});
		matchedPairList.get(4).setGroups(new Group[] {group2, group4, group9});
		matchedPairList.get(5).setGroups(new Group[] {group3, group5, group7});
		matchedPairList.get(6).setGroups(new Group[] {group1, group5, group9});
		matchedPairList.get(7).setGroups(new Group[] {group2, group6, group7});
		matchedPairList.get(8).setGroups(new Group[] {group3, group4, group8});

		groupList.add(group1);
		groupList.add(group2);
		groupList.add(group3);
		groupList.add(group4);
		groupList.add(group5);
		groupList.add(group6);
		groupList.add(group7);
		groupList.add(group8);
		groupList.add(group9);

		return groupList;
	}

	/**
	 * Swaps the positions of pairs in the list based on the given meat positions and number of swaps.
	 *
	 * @param matchedPairList the list of matched pairs
	 * @param meatPositions   the positions of meat pairs
	 * @param num             the number of swaps
	 * @return the list of pairs after swapping
	 */
	private static List<Pair> swapPositions(List<Pair> matchedPairList, List<Integer> meatPositions, int num) {
		List<Pair> swappedList = new ArrayList<>();

		if (num == 3) {
			swappedList.add(matchedPairList.get(meatPositions.get(0)));
			swappedList.add(matchedPairList.get(meatPositions.get(1)));
			swappedList.add(matchedPairList.get(meatPositions.get(2)));

			for (int i = 0; i < 9; i++) {
				if (!meatPositions.contains(i)) {
					swappedList.add(matchedPairList.get(i));
				}
			}
		} else if (num == 2) {
			swappedList.add(matchedPairList.get(meatPositions.get(0)));
			swappedList.add(matchedPairList.get(meatPositions.get(1)));

			for (int i = 0; i < 9; i++) {
				if (!meatPositions.contains(i)) {
					swappedList.add(matchedPairList.get(i));
				}
			}
		}

		return swappedList;
	}

	/**
	 * Finds the positions of meat pairs in the list.
	 *
	 * @param matchedPairList the list of matched pairs
	 * @return the list of positions of meat pairs
	 */
	private static List<Integer> findMeatPosition(List<Pair> matchedPairList) {
		List<Integer> meatPositions = new ArrayList<>();
		for (int i = 0; i < matchedPairList.size(); i++) {
			if (matchedPairList.get(i).getFoodType() == FoodType.MEAT || matchedPairList.get(i).getFoodType() == FoodType.NONE) {
				meatPositions.add(i);
			}
		}
		return meatPositions;
	}

	/**
	 * Calculates the group score for a tested pair based on various criteria.
	 *
	 * @param pair1            the initial pair
	 * @param testedPair       the pair being tested
	 * @param groupWeights     the weights used for grouping criteria
	 * @param matchedPairList  the list of matched pairs
	 * @return the calculated score
	 */
	private static double calculateGroupScore(Pair pair1, Pair testedPair, GroupWeights groupWeights, List<Pair> matchedPairList) {
		double score = 0;

		if (!testGroupComposition(testedPair, matchedPairList)) {
			return -100000;
		}

		score += compareLocation(pair1, testedPair, groupWeights);
		score += compareGroupGender(matchedPairList, testedPair, groupWeights);
		score += compareGroupFoodPreference(pair1, testedPair, groupWeights);
		score += compareGroupAge(matchedPairList, testedPair, groupWeights);
		return score;
	}

	/**
	 * Compares the group age difference.
	 *
	 * @param matchedPairList  the list of matched pairs
	 * @param testedPair       the pair being tested
	 * @param groupWeights     the weights used for grouping criteria
	 * @return the calculated score based on age difference
	 */
	private static double compareGroupAge(List<Pair> matchedPairList, Pair testedPair, GroupWeights groupWeights) {
		double groupAge = 0;
		for (Pair pair : matchedPairList) {
			groupAge += pair.getAverageAgeRange();
		}
		double ageDifference = Math.abs((groupAge / matchedPairList.size()) - testedPair.getAverageAgeRange());
		return groupWeights.getAgeDifferenceWeight() * (1 - 0.1 * ageDifference);
	}

	/**
	 * Compares the food preferences of the group.
	 *
	 * @param pair1        the initial pair
	 * @param testedPair   the pair being tested
	 * @param groupWeights the weights used for grouping criteria
	 * @return the calculated score based on food preference
	 */
	private static double compareGroupFoodPreference(Pair pair1, Pair testedPair, GroupWeights groupWeights) {
		double weight = groupWeights.getFoodPreferenceWeight();
		switch (pair1.getFoodType()) {
			case MEAT:
				if (testedPair.getFoodType() == FoodType.MEAT) return weight;
				return (testedPair.getFoodType() == FoodType.NONE) ? weight : -1000;
			case VEGGIE:
				if (testedPair.getFoodType() == FoodType.VEGGIE) return weight;
				if (testedPair.getFoodType() == FoodType.VEGAN) return 0.5 * weight;
				return (testedPair.getFoodType() == FoodType.NONE) ? 0.33 * weight : -1000;
			case VEGAN:
				if (testedPair.getFoodType() == FoodType.VEGAN) return weight;
				if (testedPair.getFoodType() == FoodType.VEGGIE) return 0.5 * weight;
				return (testedPair.getFoodType() == FoodType.NONE) ? 0.25 * weight : -1000;
			case NONE:
				if (testedPair.getFoodType() == FoodType.NONE || testedPair.getFoodType() == FoodType.MEAT) return weight;
				if (testedPair.getFoodType() == FoodType.VEGGIE) return 0.33 * weight;
				return 0.25 * weight;
			default:
				return 0;
		}
	}

	/**
	 * Compares the gender diversity of the group.
	 *
	 * @param matchedPairList  the list of matched pairs
	 * @param testedPair       the pair being tested
	 * @param groupWeights     the weights used for grouping criteria
	 * @return the calculated score based on gender diversity
	 */
	private static double compareGroupGender(List<Pair> matchedPairList, Pair testedPair, GroupWeights groupWeights) {
		double groupGenderDeviation = 0;
		for (Pair pair : matchedPairList) {
			groupGenderDeviation += pair.getGenderDeviation();
		}

		List<Pair> matchedPairListAdded = new ArrayList<>(matchedPairList);
		matchedPairListAdded.add(testedPair);

		double groupGenderDeviationAdded = 0;
		for (Pair pair : matchedPairListAdded) {
			groupGenderDeviationAdded += pair.getGenderDeviation();
		}

		double genderDifference = groupGenderDeviationAdded - groupGenderDeviation;

		return groupWeights.getGenderDifferenceWeight() * (1 - (genderDifference * matchedPairList.size()));
	}

	/**
	 * Compares the location distance between pairs.
	 *
	 * @param pair1        the initial pair
	 * @param testedPair   the pair being tested
	 * @param groupWeights the weights used for grouping criteria
	 * @return the calculated score based on location distance
	 */
	private static double compareLocation(Pair pair1, Pair testedPair, GroupWeights groupWeights) {
		double distance = pair1.getKitchen().location().getDistance(testedPair.getKitchen().location());
		double maxPossibleDistance = 0.07268611849857841; // Max distance in the given context
		return groupWeights.getDistanceWeight() * (1 - distance / maxPossibleDistance);
	}

	/**
	 * Tests the composition of the group based on food type.
	 *
	 * @param testedPair       the pair being tested
	 * @param matchedPairList  the list of matched pairs
	 * @return true if the group composition is valid, false otherwise
	 */
	private static boolean testGroupComposition(Pair testedPair, List<Pair> matchedPairList) {
		if (testedPair.getFoodType() == FoodType.VEGAN || testedPair.getFoodType() == FoodType.VEGGIE) {
			return listCountFoodType(matchedPairList, FoodType.MEAT) + listCountFoodType(matchedPairList, FoodType.NONE) <= 3;
		} else if (listCountFoodType(matchedPairList, FoodType.VEGGIE) == 0 && listCountFoodType(matchedPairList, FoodType.VEGAN) == 0) {
			return true;
		} else {
			return listCountFoodType(matchedPairList, FoodType.MEAT) + listCountFoodType(matchedPairList, FoodType.NONE) < 3;
		}
	}

	/**
	 * Counts the number of pairs with a specific food type in the list.
	 *
	 * @param matchedPairList the list of matched pairs
	 * @param foodType        the food type to count
	 * @return the count of pairs with the specified food type
	 */
	private static int listCountFoodType(List<Pair> matchedPairList, FoodType foodType) {
		int foodTypeCount = 0;
		for (Pair pair : matchedPairList) {
			if (pair.getFoodType() == foodType) {
				foodTypeCount++;
			}
		}
		return foodTypeCount;
	}

	/**
	 * Gets the identifying number for this GroupList.
	 *
	 * @return the {@link IdentNumber} (Identifying Numbers) of this GroupList
	 */
	@Override
	public IdentNumber getIdentNumber() {
		// TODO: Implement this method
		return identNumber;
	}

	/**
	 * Evaluates the GroupList.
	 *
	 * @return the evaluation of this GroupList
	 */
	@Override
	public double evaluate() {
		// TODO: Implement this method
		return 0;
	}

	/**
	 * Gets the list of groups.
	 *
	 * @return the list of groups
	 */
	public List<Group> getGroups() {
		return this;
	}

	/**
	 * Gets the list of successor participants.
	 *
	 * @return the list of successor participants
	 */
	public List<Participant> getSuccessors() {
		return successors;
	}

	/**
	 * Gets the pair list.
	 *
	 * @return the pair list
	 */
	public PairList getPairList() {
		return pairList;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("GroupList:").append(System.lineSeparator());
		for (Group group : this) {
			sb.append(group.asOutputString()).append(System.lineSeparator());
		}
		return sb.toString();
	}

	/**
	 * Checks if the specified {@link ParticipantCollection} could be added to this {@link ParticipantCollectionList}.
	 *
	 * @param collection the {@link ParticipantCollection} to be checked
	 *
	 * @throws NullPointerException if the {@link ParticipantCollection} is or contains {@code null}
	 * @throws IllegalArgumentException if the {@link ParticipantCollection} is already contained in {@code this}
	 */
	@Override
	protected void check(Group collection) {
		nullCheck(collection);
		duplicateElementCheck(collection);
		// no duplicateParticipantCheck as each Pair is contained in exactly three Groups
	}

}
