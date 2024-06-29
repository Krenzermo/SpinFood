package controller;

import model.event.list.weight.GroupWeights;
import model.event.io.InputData;
import model.event.io.OutputData;
import model.event.list.weight.PairingWeights;
import model.event.list.GroupList;
import model.event.list.PairList;
import view.MainFrame;

/**
 * @author Finn Brecher
 * @author Ole Krenzer
 * @author Davide Piacenza
 * @author Daniel Hinkelmann
 */
public class Main {
	public static void main(String[] args) {
		//MainFrame.launchFXML(args);
		// path should directly lead to files from data directory
		InputData inputData = InputData.getInstanceDebug();

		System.out.println(inputData);

		PairingWeights pairingWeights = new PairingWeights(1,1,1);
		PairList pairList = new PairList(pairingWeights);
		System.out.println(pairList);
		System.out.println("Standardwerte:");
		System.out.println(pairList.getIdentNumber());

		GroupWeights groupWeights = new GroupWeights(1,1,1,1);
		GroupList groupList = new GroupList(pairList,groupWeights);
		System.out.println(groupList.getIdentNumber());

		OutputData outputData = new OutputData("src/main/java/data", groupList);
		outputData.makePairOutputFile("pairs");
		outputData.makeGroupOutputFile("groups");

		System.out.println();
		System.out.println("A: preference deviation > path length > age difference > gender diversity > number " +
				"of elements:");
		PairingWeights pairingWeightsA = new PairingWeights(5,1,10);
		GroupWeights groupWeightsA = new GroupWeights(3,1,10,5);
		PairList pairListA = new PairList(pairingWeightsA);
		GroupList groupListA = new GroupList(pairListA,groupWeightsA);
		System.out.println(pairListA.getIdentNumber());
		System.out.println(groupListA.getIdentNumber());
		OutputData outputDataA = new OutputData("src/main/java/data", groupListA);
		outputDataA.makePairOutputFile("SP24_Gruppe01_Brecher_Hinkelma_Krenzerm_Piacenza_A");
		outputDataA.makeGroupOutputFile("SP24_Gruppe01_Brecher_Hinkelma_Krenzerm_Piacenza_A_groups");

		System.out.println();
		System.out.println("B number of elements > preference deviation > path length > gender diversity > " +
				"age difference");

		PairingWeights pairingWeightsB = new PairingWeights(5,1,10);
		GroupWeights groupWeightsB = new GroupWeights(3,1,10,5);
		PairList pairListB = new PairList(pairingWeightsB);
		GroupList groupListB = new GroupList(pairListB,groupWeightsB);
		System.out.println(pairListB.getIdentNumber());
		System.out.println(groupListB.getIdentNumber());
		OutputData outputDataB = new OutputData("src/main/java/data", groupListB);
		outputDataB.makePairOutputFile("SP24_Gruppe01_Brecher_Hinkelma_Krenzerm_Piacenza_B");
		outputDataB.makeGroupOutputFile("SP24_Gruppe01_Brecher_Hinkelma_Krenzerm_Piacenza_B_groups");

		System.out.println();
		System.out.println("C gender diversity > age difference > preference deviation > path length > number " +
				"of elements");

		PairingWeights pairingWeightsC = new PairingWeights(5,10,1);
		GroupWeights groupWeightsC = new GroupWeights(5,10,3,1);
		PairList pairListC = new PairList(pairingWeightsC);
		GroupList groupListC = new GroupList(pairListC,groupWeightsC);
		System.out.println(pairListC.getIdentNumber());
		System.out.println(groupListC.getIdentNumber());
		OutputData outputDataC = new OutputData("src/main/java/data", groupListC);
		outputDataC.makePairOutputFile("SP24_Gruppe01_Brecher_Hinkelma_Krenzerm_Piacenza_C");
		outputDataC.makeGroupOutputFile("SP24_Gruppe01_Brecher_Hinkelma_Krenzerm_Piacenza_C_groups");


	}
}
