package calculator.kata;

import java.math.BigDecimal;

public class StringCalculator {

	public static int add(final String numbers) {
		return CalculatorInput.from(numbers).omitLargeNumbers().omitNegativeNumbers().parse().stream()
				.reduce(BigDecimal.ZERO, BigDecimal::add).intValue();
	}

}
