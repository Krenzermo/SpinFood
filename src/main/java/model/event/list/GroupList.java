package model.event.list;

import model.event.Course;
import model.event.GroupWeights;
import model.event.collection.Group;
import model.event.collection.Pair;
import model.event.list.identNumbers.IdentNumber;
import model.person.FoodType;
import model.person.Participant;

import java.lang.reflect.Array;
import java.util.*;

/**
 * The GroupList class represents a collection of Groups of Pairs.
 * It provides functionality to group participants based on various criteria
 * and maintains a list of unpaired participants (successors).
 *
 * @author Daniel Hinkelmann
 * @author Finn Brecher
 */
public class GroupList extends ParticipantCollectionList<Group> {

	private final PairList pairList;
	private IdentNumber identNumber;
	private static final List<Participant> successors = new ArrayList<>();
	private static final List<Pair> successorPairs = new ArrayList<>();


	public GroupList(PairList pairList, GroupWeights groupWeights) {
		setList(buildBestGroups(pairList, groupWeights));
		this.identNumber = getIdentNumber();
		this.pairList = pairList;



	}

	/*
	1. paar nehmen
	8 mal beste paare suchen
	alle 9 entfernen
	gruppe aus 9 bilden
	dabei nach distanz sortieren
	wenn 2-3 any in veggiegruppe, bzw 2-3 veggie in vegan, dann spezielle sortierung
	bis nurnoch <9 da (vegan+veggie zuerst?)
	*/
	private static List<Group> buildBestGroups(List<Pair> pairList, GroupWeights groupWeights) {
		List<Pair> sortedPairList = new ArrayList<>(pairList);
		List<Group> bestGroupList = new ArrayList<>();

		while (sortedPairList.size() >= 9) {
			List<Pair> matchedPairList = new ArrayList<>();
			Pair pair1 = sortedPairList.remove(0);
			matchedPairList.add(pair1);                                //brauchen pair1 schon in matchedList für test auf gemischte gruppen

			for (int i = 0; i < 8; i++) {
				int bestPairPosition = -1;
				double bestPairScore = Double.NEGATIVE_INFINITY;

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
				successorPairs.add(matchedPairList.remove(0));       // wenn pair1 nicht 8 paare findet, add ihn zu successor und
				sortedPairList.addAll(matchedPairList);                        // adde gematchte paare wieder in die liste
			} else {
				bestGroupList.addAll(makeGroups(matchedPairList));
			}
		}

		successorPairs.addAll(sortedPairList);

		return bestGroupList;
	}

	//TODO make groups out of the 9 pairs, sort first by distance, or special sorting for mixed groups
//V036 (0, 3, 6)
//V147 (1, 4, 7)
//V258 (2, 5, 8)
//H048 (0, 4, 8)
//H156 (1, 5, 6)
//H237 (2, 3, 7)
//D057 (0, 5, 7)
//D138 (1, 3, 8)
//D246 (2, 4, 6)
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
		groupList.add(new Group(matchedPairList.get(0), matchedPairList.get(3), matchedPairList.get(6), Course.STARTER, matchedPairList.get(0).getKitchen()));
		groupList.add(new Group(matchedPairList.get(1), matchedPairList.get(4), matchedPairList.get(7), Course.STARTER, matchedPairList.get(1).getKitchen()));
		groupList.add(new Group(matchedPairList.get(2), matchedPairList.get(5), matchedPairList.get(8), Course.STARTER, matchedPairList.get(2).getKitchen()));
		groupList.add(new Group(matchedPairList.get(0), matchedPairList.get(4), matchedPairList.get(8), Course.MAIN, matchedPairList.get(4).getKitchen()));
		groupList.add(new Group(matchedPairList.get(1), matchedPairList.get(5), matchedPairList.get(6), Course.MAIN, matchedPairList.get(5).getKitchen()));
		groupList.add(new Group(matchedPairList.get(2), matchedPairList.get(3), matchedPairList.get(7), Course.MAIN, matchedPairList.get(3).getKitchen()));
		groupList.add(new Group(matchedPairList.get(0), matchedPairList.get(5), matchedPairList.get(7), Course.DESSERT, matchedPairList.get(7).getKitchen()));
		groupList.add(new Group(matchedPairList.get(1), matchedPairList.get(3), matchedPairList.get(8), Course.DESSERT, matchedPairList.get(8).getKitchen()));
		groupList.add(new Group(matchedPairList.get(2), matchedPairList.get(4), matchedPairList.get(6), Course.DESSERT, matchedPairList.get(6).getKitchen()));


		return groupList;
	}

	private static List<Pair> swapPositions(List<Pair> matchedPairList, List<Integer> meatPositions, int num) {
		List<Pair> swappedList = new ArrayList<>();
		/*double sum = 0;
		for (int num : meatPositions) {
			sum += num;
		}

		double positionAverage = sum / meatPositions.size();
		*/


		if (num == 3) {
			swappedList.add(matchedPairList.get(meatPositions.get(0)));
			swappedList.add(matchedPairList.get(meatPositions.get(1)));
			swappedList.add(matchedPairList.get(meatPositions.get(2)));

			for (int i = 0; i < 9; i++) {
				if (i != meatPositions.get(0) && i != meatPositions.get(1) && i != meatPositions.get(2)) {
					swappedList.add(matchedPairList.get(i));
				}
			}

		} else if (num == 2) {
			swappedList.add(matchedPairList.get(meatPositions.get(0)));
			swappedList.add(matchedPairList.get(meatPositions.get(1)));

			for (int i = 0; i < 9; i++) {
				if (i != meatPositions.get(0) && i != meatPositions.get(1)) {
					swappedList.add(matchedPairList.get(i));
				}
			}
		}

		/* else if (positionAverage <6){

			int i =0;
			while(swappedList.size()<3){
				if (i!= meatPositions.get(0) && i!= meatPositions.get(1) && i!= meatPositions.get(2)){
					swappedList.add(matchedPairList.get(i));
				}
				i++;
			}

			swappedList.add(matchedPairList.get(meatPositions.get(0)));
			swappedList.add(matchedPairList.get(meatPositions.get(1)));
			swappedList.add(matchedPairList.get(meatPositions.get(2)));
			while(swappedList.size()<10){
				if (i!= meatPositions.get(0) && i!= meatPositions.get(1) && i!= meatPositions.get(2)){
					swappedList.add(matchedPairList.get(i));
				}
				i++;
			}

		} else {

			for(int i=0;i<9;i++){
				if (i!= meatPositions.get(0) && i!= meatPositions.get(1) && i!= meatPositions.get(2)){
					swappedList.add(matchedPairList.get(i));
				}
			}
			swappedList.add(matchedPairList.get(meatPositions.get(0)));
			swappedList.add(matchedPairList.get(meatPositions.get(1)));
			swappedList.add(matchedPairList.get(meatPositions.get(2)));

		} */
		return swappedList;
	}

	private static List<Integer> findMeatPosition(List<Pair> matchedPairList) {
		List<Integer> meatPositions = new ArrayList<>();
		for (int i = 0; i < matchedPairList.size(); i++) {
			if (matchedPairList.get(i).getFoodType() == FoodType.MEAT || matchedPairList.get(i).getFoodType() == FoodType.NONE) {
				meatPositions.add(i);
			}
		}
		return meatPositions;
	}

	//TODO score weights anpassen
	private static double calculateGroupScore(Pair pair1, Pair testedPair, GroupWeights groupWeights, List<Pair> matchedPairList) {
		double score = 0;


		if (!testGroupComposition(testedPair, matchedPairList)) {
			return -Double.NEGATIVE_INFINITY;
		}


		score += compareLocation(pair1, testedPair, groupWeights);
		score += compareGroupGender(matchedPairList, testedPair, groupWeights);
		score += compareGroupFoodPreference(pair1, testedPair, groupWeights);
		score += compareGroupAge(matchedPairList, testedPair, groupWeights);
		return score;

	}

	//abstand von durschnittssalter-done
	private static double compareGroupAge(List<Pair> matchedPairList, Pair testedPair, GroupWeights groupWeights) {
		double groupAge = 0;
		for (Pair pair : matchedPairList) {
			groupAge = groupAge + pair.getAverageAgeRange();
		}
		double ageDifference = Math.abs((groupAge / matchedPairList.size()) - testedPair.getAverageAgeRange());
		return groupWeights.getAgeDifferenceWeight() * (1 - ageDifference);
	}

	//gleich wie bei pair i guess, vorliebe vom gruppencluster gleich dem 1. paar-done
	private static double compareGroupFoodPreference(Pair pair1, Pair testedPair, GroupWeights groupWeights) {
		double weight = groupWeights.getFoodPreferenceWeight();
		switch (pair1.getFoodType()) {
			case MEAT:
				if (testedPair.getFoodType() == FoodType.MEAT) return weight;
				return (testedPair.getFoodType() == FoodType.NONE) ? 0.5 * weight : -1000;
			case VEGGIE:
				if (testedPair.getFoodType() == FoodType.VEGGIE) return weight;
				if (testedPair.getFoodType() == FoodType.VEGAN) return 0.5 * weight;
				return (testedPair.getFoodType() == FoodType.NONE) ? 0.25 * weight : -1000;
			case VEGAN:
				if (testedPair.getFoodType() == FoodType.VEGAN) return weight;
				if (testedPair.getFoodType() == FoodType.VEGGIE) return 0.5 * weight;
				return (testedPair.getFoodType() == FoodType.NONE) ? 0.25 * weight : -1000;
			case NONE:
				return (testedPair.getFoodType() == FoodType.NONE || testedPair.getFoodType() == FoodType.MEAT) ? 0.5 * weight : 0.25 * weight;
			default:
				return 0;
		}

	}

	//minimaleGenderDeviaton-done
//dh, gruppen die schon gute diversität haben, achten weniger auf diversität i guess?
//0 ist perfekt
//TODO eventuell Wertung auf 0,5 mal das weight ändern wenn zu impactful?
	private static double compareGroupGender(List<Pair> matchedPairList, Pair testedPair, GroupWeights groupWeights) {
		double groupGenderdeviation = 0;
		for (Pair pair : matchedPairList) {
			groupGenderdeviation = groupGenderdeviation + pair.getGenderDeviation();
		}

		List<Pair> matchedPairListAdded = new ArrayList<>(matchedPairList);
		matchedPairListAdded.add(testedPair);

		double groupGenderdeviationAdded = 0;
		for (Pair pair : matchedPairListAdded) {
			groupGenderdeviationAdded = groupGenderdeviationAdded + pair.getGenderDeviation();
		}

		double genderDifference = groupGenderdeviationAdded - groupGenderdeviation;


		return groupWeights.getGenderDifferenceWeight() * (1 - (genderDifference * matchedPairList.size()));    // *size, damit die späteren paare vollen impact haben
	}
//minimale Locationdiff,vlt hier nah an pair1? - done
//TODO maxDistance eventuell unrealistisch, vlt kleiner ansetzten? vlt auch egal

	//Minimum Longitude: 8.65772146601726
//Maximum Longitude: 8.719772776126227
//Range of Longitude: 0.06205131010896636
//Minimum Latitude: 50.57093176601726
//Maximum Latitude: 50.60878552060185
//Range of Latitude: 0.03785375458458873
	private static double compareLocation(Pair pair1, Pair testedPair, GroupWeights groupWeights) {
		double distance = pair1.getKitchen().location().getDistance(testedPair.getKitchen().location());
		double maxPossibleDistance = 0.07268611849857841;
		return groupWeights.getDistanceWeight() * (1 - distance / maxPossibleDistance);    //relativiert die Distance mit max Distance
	}

	//wenn mixedgroup, dann max 3 any/fleischie-done
	private static boolean testGroupComposition(Pair testedPair, List<Pair> matchedPairList) {
		if (testedPair.getFoodType() == FoodType.VEGAN || testedPair.getFoodType() == FoodType.VEGGIE) {
			return listCountFoodType(matchedPairList, FoodType.MEAT) + listCountFoodType(matchedPairList, FoodType.NONE) > 3;
		} else if (listCountFoodType(matchedPairList, FoodType.VEGGIE) == 0 && listCountFoodType(matchedPairList, FoodType.VEGAN) == 0) {
			return true;
		} else
			return listCountFoodType(matchedPairList, FoodType.MEAT) + listCountFoodType(matchedPairList, FoodType.NONE) != 3;
	}

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
		// TODO: this
		return null;
	}

	/**
	 * Evaluates the GroupList.
	 *
	 * @return the evaluation of this GroupList
	 */
	@Override
	public double evaluate() {
		// TODO: this
		return 0;
	}

	public List<Group> getGroups() {
		return this;
	}

	public List<Participant> getSuccessors() {
		return successors;
	}


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
}