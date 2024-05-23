package controller;

import model.event.InputData;

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
	}
}
