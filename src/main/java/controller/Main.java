package controller;

import model.event.GroupWeights;
import model.event.InputData;
import model.event.OutputData;
import model.event.PairingWeights;
import model.event.collection.Group;
import model.event.collection.Pair;
import model.event.list.GroupList;
import model.event.list.PairList;

/**
 * @author Finn Brecher
 * @author Ole Krenzer
 * @author Davide Piacenza
 * @author Daniel Hinkelmann
 */
public class Main {
	public static void main(String[] args) {

		// path should directly lead to files from data directory
		InputData inputData = InputData.getInstance("src/main/java/data/teilnehmerliste.csv" , "src/main/java/data/partylocation.csv");

		System.out.println(inputData);

		PairingWeights pairingWeights = new PairingWeights(1,1,1);
		PairList pairList = new PairList(inputData,pairingWeights);
		System.out.println(pairList);
		System.out.println(pairList.getIdentNumber());

		GroupWeights groupWeights = new GroupWeights(1,1,1,1);
		GroupList groupList = new GroupList(pairList,groupWeights);
		System.out.println(groupList);
		System.out.println(groupList.getIdentNumber());

		boolean foundDuplicate = false;

		for (int i = 0; i < groupList.getPairList().size() - 1; i++) {
			Pair pair1 = groupList.getPairList().get(i);

			for (int j = i + 1; j < groupList.getPairList().size(); j++) {
				Pair pair2 = groupList.getPairList().get(j);

				// Check if the kitchen and course are the same for two different pairs
				if ((pair1.getMainNumber() != 0) && (pair2.getMainNumber() != 0) && (!pair1.equals(pair2)) && pair1.getKitchen().equals(pair2.getKitchen()) && pair1.getCourse().equals(pair2.getCourse())) {
					foundDuplicate = true;
					System.out.println(groupList.getPairList().get(i));
					System.out.println(groupList.getPairList().get(j));
					break;
				}
			}

			if (foundDuplicate) {
				break;
			}
		}

		if (foundDuplicate) {
			System.out.println("There are two different pairs with the same kitchen and course.");
		} else {
			System.out.println("No two different pairs have the same kitchen and course.");
		}

		OutputData outputData = new OutputData("src/main/java/data", groupList);
		outputData.makePairOutputFile();

	}
}
