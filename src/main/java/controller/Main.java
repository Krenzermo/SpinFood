package controller;

import model.InputData;

import java.nio.file.Paths;

public class Main {
	public static void main(String[] args) {

		// path should directly lead to files from data directory
		InputData inputData = new InputData (Paths.get("src/main/java/data/teilnehmerliste.csv").toString(), Paths.get("src/main/java/data/partylocation.csv").toString());

		System.out.println(inputData);
	}
}
