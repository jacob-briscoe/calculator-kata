package calculator.kata;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("Calculator Input")
public class CalculatorInputSpec {

	@Nested
	@DisplayName("is empty")
	class IsEmpty {
		@Test
		@DisplayName("when null is provided")
		public void isNull() {
			assertTrue(CalculatorInput.from(null).parse().isEmpty(), () -> "null input should be empty result");
		}

		@Test
		@DisplayName("when empty string is provided")
		public void isEmpty() {
			assertTrue(CalculatorInput.from("  ").parse().isEmpty(), () -> "empty input should be empty result");
		}
	}

	@Nested
	@DisplayName("is parsed")
	class Parsed {

		@Test
		@DisplayName("when given a single number")
		public void single() {
			assertEquals(asList(BigDecimal.ONE), CalculatorInput.from("1").parse(), () -> "single input expected");
		}

		@Test
		@DisplayName("when given more than one number")
		public void moreThanOne() {
			assertEquals(asList(BigDecimal.ONE, BigDecimal.TEN), CalculatorInput.from("1,10").parse(),
					() -> "more than one input expected");
		}
	}

	@Nested
	@DisplayName("is parsed with custom delimiter(s)")
	class IsParsedWithCustomDelimiter {
		@Test
		@DisplayName("for a single number")
		public void withCustomDelimiterButJustOne() {
			assertEquals(asList(BigDecimal.ONE), CalculatorInput.from("//*\n1").parse(),
					() -> "with custom delimiter expected 1");
		}

		@Test
		@DisplayName("for more than one number")
		public void withCustomDelimiter() {
			assertEquals(asList(BigDecimal.ONE, BigDecimal.TEN), CalculatorInput.from("//*\n1*10").parse(),
					() -> "with custom delimiter expected 1,10");
		}

		@Test
		@DisplayName("with multiple character delimiters")
		public void withCustomDelimiterAndItHasMultipleCharacters() {
			assertEquals(asList(BigDecimal.ONE, BigDecimal.TEN), CalculatorInput.from("//***\n1***10").parse(),
					() -> "with custom delimiter and multiple characters expected 1,10");
		}

	}

	@Nested
	@DisplayName("is filtered")
	class Filtered {
		@Test
		@DisplayName("when it has negative numbers")
		public void hasNegativeNumbers() {
			try {
				assertEquals(asList(BigDecimal.TEN),
						CalculatorInput.from("//***\n-1***10***-3").omitNegativeNumbers().parse(),
						() -> "has negative number to be omited");
				fail(() -> "should have been a NumberFormatException due to negative number");
			} catch (final NumberFormatException nfe) {
				assertEquals(String.format(CalculatorInput.NEGATIVES_ERROR_MSG, "-1,-3"), nfe.getMessage());
			}
		}

		@Test
		@DisplayName("when it has large numbers")
		public void hasLargeNumbers() {
			assertEquals(asList(BigDecimal.ONE, BigDecimal.TEN),
					CalculatorInput.from("//***\n1***10***1001").omitLargeNumbers().parse(),
					() -> "has a large number, expected 1,10");
		}
	}

}
