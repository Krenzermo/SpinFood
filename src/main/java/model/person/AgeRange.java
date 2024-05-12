package model.person;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Finn Brecher
 * @author Daniel Hinkelmann
 * the AgeRange of a participant
 */
public enum AgeRange {
	// the method getAgeRange relies on the correct order of the AgeRanges
	ZERO(0,0),
	ONE(1,18),
	TWO(2,24),
	THREE(3,28),
	FOUR(4,31),
	FIVE(5,36),
	SIX(6,42),
	SEVEN(7,47),
	EIGHT(8,57);

	public final int value;
	public final int min;

	/**
	 * private Constructor for AgeRange
	 *
	 * @param value the value of the AgeRange
	 * @param min the minimum age to fall into said AgeRange, the maximum is determined by the next AgeRanges minimum
	 */
	AgeRange(int value, int min) {
		this.value = value;
		this.min = min;
	}

	/**
	 * Returns the AgeRange constant, given a specific age.
	 * this method relies on the correct (ascending) order of the AgeRange.
	 *
	 * @param age the age of the participant
	 * @return the AgeRange of the participant
	 *
	 */
	public static AgeRange getAgeRange(int age) {
		AgeRange[] values = AgeRange.values();

		AgeRange ret = AgeRange.ZERO;
		for(AgeRange range: values) {
			if (age >= range.min) {
				ret = range;
			}
		}

		return ret;
	}

	/**
	 * This method calculates the difference in the AgeRange of two participants
	 *
	 * @param other the other AgeRange
	 * @return the difference in AgeRanges
	 */
	public int getAgeDifference(AgeRange other) {
		return Math.abs(this.value - other.value);
	}
}
