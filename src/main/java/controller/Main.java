package controller;

import model.InputData;

public class Main {
	public static void main(String[] args) {

		InputData inputData = new InputData ("C:\\Users\\morit\\Gitlab\\sp24_gruppex_brecher_hinkelma_krenzerm_piacenza\\src\\main\\java\\data\\teilnehmerliste.csv","C:\\Users\\morit\\Gitlab\\sp24_gruppex_brecher_hinkelma_krenzerm_piacenza\\src\\main\\java\\data\\partylocation.csv");

		System.out.println(inputData);
	}
}
