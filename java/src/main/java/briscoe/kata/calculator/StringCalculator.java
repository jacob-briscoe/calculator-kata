package briscoe.kata.calculator;

import com.google.common.base.Splitter;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class StringCalculator {

    public static final String NEGATIVES_NOT_ALLOWED_MSG = "negatives not allowed: ";
    private static int addCallCount;

    public static int add(String input) {
        addCallCount++;

        if (StringUtils.isBlank(input))
            return 0;

        return validateNumbers(getNumbers(input)).stream()
                .filter(number -> number <= 1000)
                .reduce(0, Integer::sum);
    }

    public static void reset() {
        addCallCount = 0;
    }

    public static int getAddCallCount() {
        return addCallCount;
    }

    private static List<Integer> getNumbers(final String input) {
        var separatedInput = SeparatedInput.from(input);

        return Splitter.onPattern(separatedInput.getSeparatorRegex())
                .splitToList(separatedInput.getSeparatedInput())
                .stream().map(Integer::parseInt).collect(Collectors.toList());
    }

    private static List<Integer> validateNumbers(final List<Integer> numbers) {
        negatives(numbers);
        return numbers;
    }

    private static void negatives(final List<Integer> numbers) {
        final List<Integer> negatives = numbers.stream().filter(number -> number < 0).collect(Collectors.toList());
        if (!negatives.isEmpty())
            throw new NumberFormatException(NEGATIVES_NOT_ALLOWED_MSG + negatives.stream().map(Object::toString).collect(Collectors.joining(",")));
    }

    static class SeparatedInput {
        public static final String DEFAULT_SEPARATOR_REGEX = ",|\n";
        private static final Pattern CUSTOM_SEPARATOR_INPUT_PATTERN = Pattern.compile("//(.*)\n(.*)");
        public static final String SEPARATOR_KEY = "separator";
        public static final String INPUT_KEY = "input";

        private final String separatorRegex;
        private final String separatedInput;

        public String getSeparatorRegex() {
            return separatorRegex;
        }

        public String getSeparatedInput() {
            return separatedInput;
        }

        public static SeparatedInput from(final String input) {
            return new SeparatedInput(input);
        }

        private SeparatedInput(final String input) {
            final Map<String, String> parseResult = parse(input);

            this.separatorRegex = parseResult.get(SEPARATOR_KEY);
            this.separatedInput = parseResult.get(INPUT_KEY);
        }

        private static Map<String, String> parse(final String input) {
            final Matcher customSeparatorMatcher = CUSTOM_SEPARATOR_INPUT_PATTERN.matcher(input);
            if (customSeparatorMatcher.find()) {
                return Map.of(SEPARATOR_KEY, customSeparatorMatcher.group(1), INPUT_KEY, customSeparatorMatcher.group(2));
            }
            return Map.of(SEPARATOR_KEY, DEFAULT_SEPARATOR_REGEX, INPUT_KEY, input);
        }
    }

}
