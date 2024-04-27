package model;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum AgeRange {
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

	AgeRange(int value, int min) {
		this.value = value;
		this.min = min;
	}

	public static AgeRange getAgeRange(int age) {
		List<AgeRange> values = Arrays.asList(AgeRange.values());
		Collections.reverse(values);
		for(AgeRange range: values) {
			if (age >= range.min) {
				return range;
			}
		}
		throw new RuntimeException("This should never happen");
	}

	public int getAgeDifference(AgeRange other) {
		return Math.abs(this.value - other.value);
	}
}
