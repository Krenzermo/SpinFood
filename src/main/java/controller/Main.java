package controller;

import model.AgeRange;

import java.util.Arrays;

public class Main {
	public static void main(String[] args) {
		System.out.println("Hello world!");
		System.out.println(AgeRange.getAgeRange(56).getAgeDifference(AgeRange.getAgeRange(57)));
	}
}
