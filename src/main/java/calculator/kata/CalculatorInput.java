package calculator.kata;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;

public class CalculatorInput {

	static final String NEGATIVES_ERROR_MSG = "negatives not allowed found: %s";
	static final String DEFAULT_DELIMITER = ",";
	static final Pattern CUSTOM_DELIMITER_PATTERN = Pattern.compile("//(.*)\n(.*)");
	private final String input;
	private boolean omitNegativeNumbers;
	private boolean omitLargeNumbers;
	private static final Predicate<? super BigDecimal> NEGATIVES = amount -> amount.compareTo(BigDecimal.ZERO) < 0;
	private static final Predicate<? super BigDecimal> LARGE_NUMBERS = amount -> amount
			.compareTo(BigDecimal.valueOf(1000)) < 0;

	public static CalculatorInput from(final String input) {
		return new CalculatorInput(input);
	}

	public List<BigDecimal> parse() {
		if (StringUtils.isBlank(input))
			return Collections.emptyList();

		return readInput();
	}

	public CalculatorInput omitNegativeNumbers() {
		this.omitNegativeNumbers = true;
		return this;
	}

	public CalculatorInput omitLargeNumbers() {
		this.omitLargeNumbers = true;
		return this;
	}

	private CalculatorInput(final String input) {
		this.input = input;
	}

	private List<BigDecimal> readInput() {
		final Delimiter delimiter = maybeUsingCustomDelimiter().orElse(Delimiter.standard(input));

		final List<String> splitResults = Splitter.on(delimiter.getDelimeter()).omitEmptyStrings().trimResults()
				.splitToList(delimiter.getDelimitedInput());

		return filterResults(splitResults.stream().map(item -> new BigDecimal(item))
				.collect(collectingAndThen(toList(), ImmutableList::copyOf)));
	}

	private List<BigDecimal> filterResults(final List<BigDecimal> results) {
		return filterLargeNumbers(filterNegatives(results));
	}

	private List<BigDecimal> filterNegatives(final List<BigDecimal> results) {
		if (!this.omitNegativeNumbers)
			return results;

		final List<BigDecimal> negativeAmounts = results.stream()
				.filter(NEGATIVES)
				.collect(collectingAndThen(toList(), ImmutableList::copyOf));

		if (negativeAmounts.isEmpty())
			return results;

		final String negativeAmountsSummary = negativeAmounts.stream().map(BigDecimal::toString)
				.collect(Collectors.joining(","));
		throw new NumberFormatException(String.format(NEGATIVES_ERROR_MSG, negativeAmountsSummary));
	}

	private List<BigDecimal> filterLargeNumbers(final List<BigDecimal> results) {
		if (!this.omitLargeNumbers)
			return results;

		return results.parallelStream()
				.filter(LARGE_NUMBERS)
				.collect(collectingAndThen(toList(), ImmutableList::copyOf));
	}


	private static class Delimiter {
		private String delimeter;
		private String delimitedInput;

		public static Delimiter standard(final String input) {
			return new Delimiter(DEFAULT_DELIMITER, input);
		}

		public static Delimiter of(final String delimiter, final String input) {
			return new Delimiter(delimiter, input);
		}

		public String getDelimeter() {
			return delimeter;
		}

		public String getDelimitedInput() {
			return delimitedInput;
		}

		private Delimiter(final String delimeter, final String delimitedInput) {
			this.delimeter = delimeter;
			this.delimitedInput = delimitedInput;
		}

	}

	private Optional<Delimiter> maybeUsingCustomDelimiter() {
		final Matcher m = CUSTOM_DELIMITER_PATTERN.matcher(input);

		if (!m.find())
			return Optional.empty();

		return Optional.of(Delimiter.of(m.group(1), m.group(2)));
	}

}
