package controller;

import model.InputData;

import java.nio.file.Paths;

public class Main {
	public static void main(String[] args) {

		// path should directly lead to files from data directory
		InputData inputData = InputData.getInstance();

		System.out.println(inputData);
	}
}
