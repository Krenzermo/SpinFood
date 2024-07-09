package model.event.list;

import model.event.Course;
import model.event.io.InputData;
import model.event.list.weight.GroupWeights;
import model.event.collection.Group;
import model.event.collection.Pair;
import model.event.list.identNumbers.GroupIdentNumber;
import model.event.list.identNumbers.IdentNumber;
import model.event.list.weight.Weights;
import model.kitchen.Kitchen;
import model.person.FoodType;
import model.person.Participant;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * The GroupList class represents a collection of Groups of Pairs.
 * It provides functionality to group participants based on various criteria
 * and maintains a list of unpaired participants (successors).
 * <p>
 * This class extends ParticipantCollectionList and uses GroupWeights for grouping logic.
 *
 * @see model.event.list.weight.GroupWeights
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

	private static final InputData inputData = InputData.getInstance();
	private final PairList pairList;
	private IdentNumber identNumber;
	private List<Pair> successorPairs = new ArrayList<>();
	private final Weights weights;
	//private final List<Kitchen> starterKitchens = new ArrayList<>();
	//private final List<Kitchen> mainKitchens = new ArrayList<>();
	//private final List<Kitchen> dessertKitchens = new ArrayList<>();

	/**
	 * Copy constructor for class {@link GroupList}.
	 * Copies all fields including the {@link Group} class information.
	 * This constructor returns a deep copy (also copies the {@link PairList}, {@link Group}, {@link Pair}, {@link Participant} instances)
	 */
	public GroupList(GroupList groupList) {
		pairList = new PairList(groupList.pairList);
		identNumber = groupList.identNumber;
		successorPairs = groupList.successorPairs.stream().map(Pair::new).toList();
		weights = groupList.weights;

		// TODO: copy Group class information, maybe using MainController.getGroupCluster()
	}

	/**
	 * Constructs a GroupList instance.
	 *
	 * @param pairList     the list of pairs to be grouped
	 * @param groupWeights the weights used for grouping criteria
	 */
	public GroupList(PairList pairList, GroupWeights groupWeights) {
		this.weights = groupWeights;
		Group.COUNTER = 0;
		List<Pair> sortedPairsList = sortPairs(pairList);
		setList(buildBestGroups(sortedPairsList, groupWeights));
		this.identNumber = getIdentNumber();
		this.pairList = pairList;
		this.identNumber = deriveIdentNumber();
		successorPairs.addAll(inputData.getPairSuccessorList());
	}

	public GroupList(List<Pair> pairs, GroupWeights weights) {
		this(new PairList(pairs), weights);
	}

	/**
	 * Uses an instance of GroupList to call the buildBestGroups Algorithm.
	 * This is very ugly but necessary unless the algorithms are refactored into separate classes.
	 *
	 * @param instance any GroupList instance
	 * @param pairs the List of Pairs to be grouped
	 * @param weights the GroupWeights
	 * @return the List of Groups
	 */
	public static List<Group> getGroups(GroupList instance, List<Pair> pairs, GroupWeights weights) {
		return instance.buildBestGroups(pairs, weights);
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
	private List<Pair> sortPairs(PairList pairList) {     //TODO hier direkt List<Pair> genommen
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
	public List<Group> buildBestGroups(List<Pair> pairList, GroupWeights groupWeights) {
		List<Pair> sortedPairList = new ArrayList<>(pairList);
		List<Group> bestGroupList = new ArrayList<>();

		List<Kitchen> starterKitchens = new ArrayList<>();
		List<Kitchen> mainKitchens = new ArrayList<>();
		List<Kitchen> dessertKitchens = new ArrayList<>();

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
				matchedPairList.clear();
			} else { // TODO: remove print statements
				List<Group> tempList = makeGroups(matchedPairList, starterKitchens, mainKitchens, dessertKitchens);
				//System.out.println("Zahl in Gruppe" + tempList.size());
				if(tempList.size() <9){
					//System.out.println("Zahl wenn Gruppe unter 9 teiln: " + tempList.size());
					successorPairs.addAll(matchedPairList);
				}
				if(tempList.size() == 9){
					//System.out.println("Zahl wenn Gruppe gut(sollte 9 sein) " +tempList.size());
					bestGroupList.addAll(tempList);
				}
			}
		}

		successorPairs.addAll(sortedPairList);

		return bestGroupList;
	}

	/**
	 * Creates groups from a list of 9 matched pairs.
	 *
	 * @param pairsToBeGrouped the list of matched pairs
	 * @return the list of groups
	 */
	private List<Group> makeGroups(List<Pair> pairsToBeGrouped, List<Kitchen> starterKitchens, List<Kitchen> mainKitchens, List<Kitchen> dessertKitchens) {
		List<Group> groupList = new ArrayList<>();
		if (pairsToBeGrouped.size() < 9) {
			System.out.println("Error: matchedPairList has fewer than 9 elements. Size: " + pairsToBeGrouped.size());
			return groupList;
		}
		pairsToBeGrouped.sort(Comparator.comparingDouble(Pair::getDistance).reversed());
		pairsToBeGrouped = sortGroupCluster(pairsToBeGrouped);

		int vegCount = listCountFoodType(pairsToBeGrouped, FoodType.VEGGIE) + listCountFoodType(pairsToBeGrouped, FoodType.VEGAN);
		int meatCount = listCountFoodType(pairsToBeGrouped, FoodType.MEAT) + listCountFoodType(pairsToBeGrouped, FoodType.NONE);
		if (vegCount > 0) {
			if (meatCount == 2) {
				List<Integer> meatPositions = new ArrayList<>(findMeatPosition(pairsToBeGrouped));
				pairsToBeGrouped = swapPositions(pairsToBeGrouped, meatPositions, 2);
			} else if (meatCount == 3) {
				List<Integer> meatPositions = new ArrayList<>(findMeatPosition(pairsToBeGrouped));
				pairsToBeGrouped = swapPositions(pairsToBeGrouped, meatPositions, 3);
			}
		}

		List<Pair> pairsToBeGrouped5 = removeDoubleKitchens(pairsToBeGrouped, starterKitchens, mainKitchens, dessertKitchens);
		boolean test = true;
		for (int i = 0; i < 9; i++) {
			if (pairsToBeGrouped5.get(i) == null) {
				test = false;
				break;
			}
		}
		if (!test) {
			return groupList;
		}

		starterKitchens.add(pairsToBeGrouped5.get(0).getKitchen());
		starterKitchens.add(pairsToBeGrouped5.get(1).getKitchen());
		starterKitchens.add(pairsToBeGrouped5.get(2).getKitchen());
		mainKitchens.add(pairsToBeGrouped5.get(3).getKitchen());
		mainKitchens.add(pairsToBeGrouped5.get(4).getKitchen());
		mainKitchens.add(pairsToBeGrouped5.get(5).getKitchen());
		dessertKitchens.add(pairsToBeGrouped5.get(6).getKitchen());
		dessertKitchens.add(pairsToBeGrouped5.get(7).getKitchen());
		dessertKitchens.add(pairsToBeGrouped5.get(8).getKitchen());

		Group group1 = new Group(pairsToBeGrouped5.get(0), pairsToBeGrouped5.get(3), pairsToBeGrouped5.get(6), Course.STARTER, pairsToBeGrouped5.get(0).getKitchen());
		Group group2 = new Group(pairsToBeGrouped5.get(1), pairsToBeGrouped5.get(4), pairsToBeGrouped5.get(7), Course.STARTER, pairsToBeGrouped5.get(1).getKitchen());
		Group group3 = new Group(pairsToBeGrouped5.get(2), pairsToBeGrouped5.get(5), pairsToBeGrouped5.get(8), Course.STARTER, pairsToBeGrouped5.get(2).getKitchen());
		Group group4 = new Group(pairsToBeGrouped5.get(0), pairsToBeGrouped5.get(4), pairsToBeGrouped5.get(8), Course.MAIN, pairsToBeGrouped5.get(4).getKitchen());
		Group group5 = new Group(pairsToBeGrouped5.get(1), pairsToBeGrouped5.get(5), pairsToBeGrouped5.get(6), Course.MAIN, pairsToBeGrouped5.get(5).getKitchen());
		Group group6 = new Group(pairsToBeGrouped5.get(2), pairsToBeGrouped5.get(3), pairsToBeGrouped5.get(7), Course.MAIN, pairsToBeGrouped5.get(3).getKitchen());
		Group group7 = new Group(pairsToBeGrouped5.get(0), pairsToBeGrouped5.get(5), pairsToBeGrouped5.get(7), Course.DESSERT, pairsToBeGrouped5.get(7).getKitchen());
		Group group8 = new Group(pairsToBeGrouped5.get(1), pairsToBeGrouped5.get(3), pairsToBeGrouped5.get(8), Course.DESSERT, pairsToBeGrouped5.get(8).getKitchen());
		Group group9 = new Group(pairsToBeGrouped5.get(2), pairsToBeGrouped5.get(4), pairsToBeGrouped5.get(6), Course.DESSERT, pairsToBeGrouped5.get(6).getKitchen());

		pairsToBeGrouped5.get(0).setGroups(new Group[]{group1, group4, group7});
		pairsToBeGrouped5.get(1).setGroups(new Group[]{group2, group5, group8});
		pairsToBeGrouped5.get(2).setGroups(new Group[]{group3, group6, group9});
		pairsToBeGrouped5.get(3).setGroups(new Group[]{group1, group6, group8});
		pairsToBeGrouped5.get(4).setGroups(new Group[]{group2, group4, group9});
		pairsToBeGrouped5.get(5).setGroups(new Group[]{group3, group5, group7});
		pairsToBeGrouped5.get(6).setGroups(new Group[]{group1, group5, group9});
		pairsToBeGrouped5.get(7).setGroups(new Group[]{group2, group6, group7});
		pairsToBeGrouped5.get(8).setGroups(new Group[]{group3, group4, group8});

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

	private List<Pair> sortGroupCluster(List<Pair> pairsToBeGrouped) {
		List<Pair> sortedCluster = new ArrayList<>();

		sortedCluster.add(pairsToBeGrouped.remove(0));
		sortedCluster.add(pairsToBeGrouped.remove(0));
		sortedCluster.add(pairsToBeGrouped.remove(0));

		sortedCluster.add(pairsToBeGrouped.remove(findNearestPair(sortedCluster.get(0),pairsToBeGrouped)));
		sortedCluster.add(pairsToBeGrouped.remove(findNearestPair(sortedCluster.get(1),pairsToBeGrouped)));
		sortedCluster.add(pairsToBeGrouped.remove(findNearestPair(sortedCluster.get(2),pairsToBeGrouped)));
		sortedCluster.add(pairsToBeGrouped.remove(findNearestPair(sortedCluster.get(3),pairsToBeGrouped)));
		sortedCluster.add(pairsToBeGrouped.remove(findNearestPair(sortedCluster.get(4),pairsToBeGrouped)));
		sortedCluster.add(pairsToBeGrouped.remove(findNearestPair(sortedCluster.get(5),pairsToBeGrouped)));



		return  sortedCluster;
	}

	private int findNearestPair(Pair cookingPair, List<Pair> pairsToBeGrouped) {
		int nearestPairPos = 0;
		double distance = 1;
		for(int i = 0; i < pairsToBeGrouped.size(); i++){
			double kitchenDistance = cookingPair.getKitchen().location().getDistance(pairsToBeGrouped.get(i).getKitchen().location());
			if (kitchenDistance < distance){
				distance = kitchenDistance;
				nearestPairPos = i;
			}
		}

		return  nearestPairPos;
	}


	private List<Pair> removeDoubleKitchens(List<Pair> testedPairList2, List<Kitchen> starterKitchens, List<Kitchen> mainKitchens, List<Kitchen> dessertKitchens) {
		List<Pair> testedPairList = new ArrayList<>(testedPairList2);
		List<Pair> nulledList = new ArrayList<>();
		List<Pair> meatList = new ArrayList<>();
		boolean doubledKitchen = false;
		boolean doubledKitchenMeat = false;

		for (int i = 0; i < 9; i++) {							// liste mit null bauen, um Posis fest zuzuweisen
			nulledList.add(null);
		}

		int vegCount = listCountFoodType(testedPairList, FoodType.VEGGIE) + listCountFoodType(testedPairList, FoodType.VEGAN);
		int meatCount = listCountFoodType(testedPairList, FoodType.MEAT) + listCountFoodType(testedPairList, FoodType.NONE);
		boolean mixedGroup = vegCount > 0 && meatCount > 1;

		for (Pair pair: testedPairList){               //TODO constraint entspannen?
            if (starterKitchens.contains(pair.getKitchen()) || mainKitchens.contains(pair.getKitchen()) || dessertKitchens.contains(pair.getKitchen())) {
                doubledKitchen = true;
				if(mixedGroup&&(pair.getFoodType().equals(FoodType.MEAT)||pair.getFoodType().equals(FoodType.NONE))){
					doubledKitchenMeat = true;
				}

            }
		}
		if(!doubledKitchen){
			return testedPairList2;
		}

		if(mixedGroup) {//bei mixed group,meat doppel kitchen dies an anfang
			//sortPairs(matchedPairList);
			for (Pair pairMeat : testedPairList) {
				if (pairMeat.getFoodType().equals(FoodType.MEAT) || pairMeat.getFoodType().equals(FoodType.NONE)) {
					meatList.add(pairMeat);
				}
			}
		}



		if (mixedGroup) {
			for (Pair pairMixed : testedPairList) {   //loopen durch alle Paare
				//mixed group
				 //mind. eine kitchen in der gruppe doppelt belegt + mixed
					if ((pairMixed.getFoodType().equals(FoodType.MEAT) || pairMixed.getFoodType().equals(FoodType.NONE))       //meat+double pair
							&& (starterKitchens.contains(pairMixed.getKitchen()) || mainKitchens.contains(pairMixed.getKitchen()) || dessertKitchens.contains(pairMixed.getKitchen()))) {
						//erstmal meaties zuweisen
						boolean starterPossible = true;
						boolean mainPossible = true;
						boolean dessertPossible = true;

						for(Pair meatPair : meatList){
							if(starterKitchens.contains(meatPair.getKitchen())){
								starterPossible = false;
							}
							if(mainKitchens.contains(meatPair.getKitchen())){
								mainPossible = false;
							}
							if(dessertKitchens.contains(meatPair.getKitchen())){
								dessertPossible = false;
							}
						}
						if (!starterPossible && !mainPossible && !dessertPossible){
							return nulledList;
						}


						if (starterPossible) {
							nulledList.set(0,meatList.get(0));
							nulledList.set(1,meatList.get(1));
							if(meatList.size() == 3){
								nulledList.set(2,meatList.get(2));
							}
							continue;

						} else if (mainPossible) {
							nulledList.set(3,meatList.get(0));
							nulledList.set(4,meatList.get(1));
							if(meatList.size() == 3){
								nulledList.set(5,meatList.get(2));
							}
							continue;

						}else if (dessertPossible) {
							nulledList.set(6,meatList.get(0));
							nulledList.set(7,meatList.get(1));
							if(meatList.size() == 3){
								nulledList.set(8,meatList.get(2));
							}
							continue;

						}

					} //jetzt sind meaties zugeordnet

					if ((pairMixed.getFoodType().equals(FoodType.VEGGIE) || pairMixed.getFoodType().equals(FoodType.VEGAN)) && (starterKitchens.contains(pairMixed.getKitchen())
							|| mainKitchens.contains(pairMixed.getKitchen()) || dessertKitchens.contains(pairMixed.getKitchen()))) {
						//kitchen ist doppelt belegt und veggie

						if (!starterKitchens.contains(pairMixed.getKitchen())) {
							for (int i = 0; i < 3; i++) {
								if (nulledList.get(i) == null) {
									nulledList.set(i, pairMixed);
									break;
								}
							}
							if (nulledList.contains(pairMixed)) {
								continue;
							}
						}


						if (!mainKitchens.contains(pairMixed.getKitchen())) {
							for (int i = 3; i < 6; i++) {
								if (nulledList.get(i) == null) {
									nulledList.set(i, pairMixed);
									break;
								}
							}
							if (nulledList.contains(pairMixed)) {
								continue;
							}
						}


						if (!dessertKitchens.contains(pairMixed.getKitchen())) {
							for (int i = 6; i < 9; i++) {
								if (nulledList.get(i) == null) {
									nulledList.set(i, pairMixed);
									break;
								}
							}
							if (!nulledList.contains(pairMixed)) {
								return nulledList;
							}
						}
					}




			}

			if(!doubledKitchenMeat){   // meat zuordnen, falls meat nicht die gedopellte Küche ist
				if(meatList.size()==3){
					if(nulledList.get(0) == null && nulledList.get(1) == null && nulledList.get(2) == null){
						nulledList.set(0,meatList.get(0));
						nulledList.set(1,meatList.get(1));
						nulledList.set(2,meatList.get(2));

					} else if(nulledList.get(3) == null && nulledList.get(4) == null && nulledList.get(5) == null){
						nulledList.set(3,meatList.get(0));
						nulledList.set(4,meatList.get(1));
						nulledList.set(5,meatList.get(2));

					} else if(nulledList.get(6) == null && nulledList.get(7) == null && nulledList.get(8) == null){
						nulledList.set(6,meatList.get(0));
						nulledList.set(7,meatList.get(1));
						nulledList.set(8,meatList.get(2));

					} else {return nulledList;}
				}
				if(meatList.size()==2){
					if(nulledList.get(1) == null && nulledList.get(2) == null){
						nulledList.set(1,meatList.get(0));
						nulledList.set(2,meatList.get(1));

					}else if(nulledList.get(4) == null && nulledList.get(5) == null){
						nulledList.set(4,meatList.get(0));
						nulledList.set(5,meatList.get(1));

					} else if(nulledList.get(7) == null && nulledList.get(8) == null){
						nulledList.set(7,meatList.get(0));
						nulledList.set(8,meatList.get(1));

					} else {return nulledList;}

				}
			}


			//nicht hinzugefügte paare adden
			for(Pair placedPair : nulledList){
				testedPairList.remove(placedPair);
			}
			for (Pair remainingPair : testedPairList){
				for(int i =0; i<9;i++){
					if (nulledList.get(i) == null){
						nulledList.set(i,remainingPair);
						break;
					}
				}
			}

		}
			else { // non mixed groups
			for (Pair pairPure : testedPairList) {
				if ((starterKitchens.contains(pairPure.getKitchen()) || mainKitchens.contains(pairPure.getKitchen()) || dessertKitchens.contains(pairPure.getKitchen()))) {
					//mind. eine kitchen in der gruppe doppelt belegt + reine gruppe

					if (!starterKitchens.contains(pairPure.getKitchen())) {
						for (int i = 0; i < 3; i++) {
							if (nulledList.get(i) == null) {
								nulledList.set(i, pairPure);
								break;
							}
						}
						if (nulledList.contains(pairPure)) {
							continue;
						}
					}


					if (!mainKitchens.contains(pairPure.getKitchen())) {
						for (int i = 3; i < 6; i++) {
							if (nulledList.get(i) == null) {
								nulledList.set(i, pairPure);
								break;
							}
						}
						if (nulledList.contains(pairPure)) {
							continue;
						}
					}


					if (!dessertKitchens.contains(pairPure.getKitchen())) {
						for (int i = 6; i < 9; i++) {
							if (nulledList.get(i) == null) {
								nulledList.set(i, pairPure);
								break;
							}
						}
						if (!nulledList.contains(pairPure)) {
							return nulledList;
						}
					}

				}
			}

			//nicht hinzugefügte paare adden
			for(Pair placedPair : nulledList){
				testedPairList.remove(placedPair);
			}
			for (Pair remainingPair : testedPairList){
				for(int i =0; i<9;i++){
					if (nulledList.get(i) == null){
						nulledList.set(i,remainingPair);
						break;
					}
				}
			}
		}

		return  nulledList;
	}

	/**
	 * Swaps the positions of pairs in the list based on the given meat positions and number of swaps.
	 *
	 * @param matchedPairList the list of matched pairs
	 * @param meatPositions   the positions of meat pairs
	 * @param num             the number of swaps
	 * @return the list of pairs after swapping
	 */
	private List<Pair> swapPositions(List<Pair> matchedPairList, List<Integer> meatPositions, int num) {
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
	private List<Integer> findMeatPosition(List<Pair> matchedPairList) {
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
	private double calculateGroupScore(Pair pair1, Pair testedPair, GroupWeights groupWeights, List<Pair> matchedPairList) {
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
	private double compareGroupAge(List<Pair> matchedPairList, Pair testedPair, GroupWeights groupWeights) {
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
	private double compareGroupFoodPreference(Pair pair1, Pair testedPair, GroupWeights groupWeights) {
		double weight = groupWeights.getFoodPreferenceWeight();
		switch (pair1.getFoodType()) {
			case MEAT:
				if (testedPair.getFoodType() == FoodType.MEAT) return weight;
				return (testedPair.getFoodType() == FoodType.NONE) ? weight : -1000;
			case VEGGIE:
				if (testedPair.getFoodType() == FoodType.VEGGIE) return weight;
				if (testedPair.getFoodType() == FoodType.VEGAN) return 0.5 * weight;
				return (testedPair.getFoodType() == FoodType.NONE) ? 0.25 * weight : -1000;
			case VEGAN:
				if (testedPair.getFoodType() == FoodType.VEGAN) return weight;
				if (testedPair.getFoodType() == FoodType.VEGGIE) return 0.5 * weight;
				return (testedPair.getFoodType() == FoodType.NONE) ? 0.25 * weight : -1000;
			case NONE:
				return (testedPair.getFoodType() == FoodType.NONE || testedPair.getFoodType() == FoodType.MEAT) ? weight : 0.25 * weight;
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
	private double compareGroupGender(List<Pair> matchedPairList, Pair testedPair, GroupWeights groupWeights) {
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
	private double compareLocation(Pair pair1, Pair testedPair, GroupWeights groupWeights) {
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
	private boolean testGroupComposition(Pair testedPair, List<Pair> matchedPairList) {
		List <Kitchen> usedKitchens = new ArrayList<>();

		for (Pair pair: matchedPairList){                           //TODO should make same kitchen in group cluster impossible
			usedKitchens.add(pair.getKitchen());
		}

		if (usedKitchens.contains(testedPair.getKitchen())){
			return false;
		}

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
	private int listCountFoodType(List<Pair> matchedPairList, FoodType foodType) {
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

	public List<Pair> getSuccessorPairs() {
		return successorPairs;
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
	 * Checks if the specified {@link model.event.collection.ParticipantCollection} could be added to this {@link ParticipantCollectionList}.
	 *
	 * @param collection the {@link model.event.collection.ParticipantCollection} to be checked
	 *
	 * @throws NullPointerException if the {@link model.event.collection.ParticipantCollection} is or contains {@code null}
	 * @throws IllegalArgumentException if the {@link model.event.collection.ParticipantCollection} is already contained in {@code this}
	 */
	@Override
	protected void check(Group collection) {
		nullCheck(collection);
		duplicateElementCheck(collection);
		// no duplicateParticipantCheck as each Pair is contained in exactly three Groups
	}

	public List<Participant> getSuccessors() {
		return pairList.getSuccessors();
	}

	public Weights getWeights() {
		return weights;
	}
}
