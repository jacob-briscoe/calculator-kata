package calculator.kata;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("String Calculator")
class StringCalculatorSpec {

	@Test
	@DisplayName("is 0 with null input")
	void addIsZeroWithNullInput() {
		assertEquals(0, StringCalculator.add(null), () -> "add should be 0");
	}

	@Test
	@DisplayName("is 0 with empty input")
	void addIsZeroWithEmptyInput() {
		assertEquals(0, StringCalculator.add(""), () -> "add should be 0");
	}

	@Test
	@DisplayName("can add with one number")
	void addIsAddingOneNumber() {
		assertEquals(1, StringCalculator.add("1"), () -> "add should return the same number if only given one number");
	}

	@Test
	@DisplayName("can add with two numbers")
	void addTwoNumbers() {
		assertEquals(2, StringCalculator.add("1,1"), () -> "add should add two numbers");
	}

	@Test
	@DisplayName("can add with more than two numbers")
	void addMoreNumbers() {
		assertEquals(10, StringCalculator.add("2,2,2,2,2"), () -> "add should add more than two numbers");
	}

}
