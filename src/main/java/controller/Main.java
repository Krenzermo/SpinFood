package controller;

import model.event.GroupWeights;
import model.event.InputData;
import model.event.OutputData;
import model.event.PairingWeights;
import model.event.collection.Group;
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
		System.out.println(groupList.getIdentNumber());

		OutputData outputData = new OutputData("src/main/java/data", groupList);
		outputData.makePairOutputFile();

	}
}
