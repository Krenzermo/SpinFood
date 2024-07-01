package controller;

import model.event.collection.Pair;
import model.event.io.CancellationHandler;
import model.event.list.weight.GroupWeights;
import model.event.io.InputData;
import model.event.io.OutputData;
import model.event.list.weight.PairingWeights;
import model.event.list.GroupList;
import model.event.list.PairList;
import model.kitchen.KitchenAvailability;
import model.person.FoodType;
import model.person.Gender;
import model.person.Name;
import model.person.Participant;
import view.MainFrame;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
		PairList pairList = new PairList(inputData,pairingWeights);
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
		PairingWeights pairingWeightsA = new PairingWeights(5,0.5,10);
		GroupWeights groupWeightsA = new GroupWeights(2.7,1.3,20,10);
		PairList pairListA = new PairList(inputData,pairingWeightsA);
		GroupList groupListA = new GroupList(pairListA,groupWeightsA);
		System.out.println(pairListA.getIdentNumber());
		System.out.println(groupListA.getIdentNumber());
		OutputData outputDataA = new OutputData("src/main/java/data", groupListA);
		outputDataA.makePairOutputFile("SP24_Gruppe01_Brecher_Hinkelma_Krenzerm_Piacenza_A");
		outputDataA.makeGroupOutputFile("SP24_Gruppe01_Brecher_Hinkelma_Krenzerm_Piacenza_A_groups");

		System.out.println();
		System.out.println("B number of elements > preference deviation > path length > gender diversity > " +
				"age difference");

		PairingWeights pairingWeightsB = new PairingWeights(0.5,2,10);
		GroupWeights groupWeightsB = new GroupWeights(1,1.3,20,10);
		PairList pairListB = new PairList(inputData,pairingWeightsB);
		GroupList groupListB = new GroupList(pairListB,groupWeightsB);
		System.out.println(pairListB.getIdentNumber());
		System.out.println(groupListB.getIdentNumber());
		OutputData outputDataB = new OutputData("src/main/java/data", groupListB);
		outputDataB.makePairOutputFile("SP24_Gruppe01_Brecher_Hinkelma_Krenzerm_Piacenza_B");
		outputDataB.makeGroupOutputFile("SP24_Gruppe01_Brecher_Hinkelma_Krenzerm_Piacenza_B_groups");

		System.out.println();
		System.out.println("C gender diversity > age difference > preference deviation > path length > number " +
				"of elements");

		PairingWeights pairingWeightsC = new PairingWeights(5,3,1);
		GroupWeights groupWeightsC = new GroupWeights(5,10,3,0.1);
		PairList pairListC = new PairList(inputData,pairingWeightsC);
		GroupList groupListC = new GroupList(pairListC,groupWeightsC);
		System.out.println(pairListC.getIdentNumber());
		System.out.println(groupListC.getIdentNumber());
		OutputData outputDataC = new OutputData("src/main/java/data", groupListC);
		outputDataC.makePairOutputFile("SP24_Gruppe01_Brecher_Hinkelma_Krenzerm_Piacenza_C");
		outputDataC.makeGroupOutputFile("SP24_Gruppe01_Brecher_Hinkelma_Krenzerm_Piacenza_C_groups");

		/*
		CancellationHandler cancellationHandler = new CancellationHandler(pairList,groupList);
		List<Participant> canceledParticipants = new ArrayList<>();
		canceledParticipants.add(pairList.getPairs().get(1).getParticipants().get(0));
		canceledParticipants.add(pairList.getPairs().get(3).getParticipants().get(0));
		canceledParticipants.add(pairList.getPairs().get(5).getParticipants().get(0));
		canceledParticipants.add(pairList.getPairs().get(45).getParticipants().get(0));
		canceledParticipants.add(pairList.getPairs().get(75).getParticipants().get(0));

		System.out.println();
		System.out.println(pairList.getPairs().get(1).getParticipants().get(0));
		System.out.println();
		System.out.println(pairList);

		cancellationHandler.handleCancellation(canceledParticipants,pairingWeights,groupWeights);
		pairList.deriveIdentNumber();

		System.out.println(pairList);

		System.out.println(groupList);
		System.out.println(pairList.deriveIdentNumber());
		System.out.println(groupList.deriveIdentNumber());
		System.out.println(groupList.getSuccessorPairs());

		 */






	}
}
