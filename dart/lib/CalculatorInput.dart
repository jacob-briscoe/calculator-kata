class CalculatorInput {
  final DEFAULT_SEPERATOR = RegExp('\n|,');
  final String rawInput;
  RegExp seperator;
  String seperatedInput;

  CalculatorInput.from(this.rawInput) {
    seperator = DEFAULT_SEPERATOR;
    seperatedInput = rawInput;
  }

  List<int> parse() {
    final maybeMatch = RegExp('//(.*)\n').firstMatch(rawInput);

    if (maybeMatch != null) {
      seperator = RegExp(maybeMatch.group(1));
      seperatedInput = rawInput.replaceAll(RegExp(maybeMatch.group(0)), '');
    }

    final numbers = seperatedInput
        .split(seperator)
        .map(int.parse)
        .where(LESS_THAN_OR_EQUAL_TO_1000)
        .toList();

    validate(numbers);

    return numbers;
  }

  bool LESS_THAN_OR_EQUAL_TO_1000(number) => number <= 1000;

  void validate(List<int> numbers) {
    negativesNotAllowed(numbers);
  }

  void negativesNotAllowed(List<int> numbers) {
    final negativeNumbers = numbers.where((number) => number < 0).toList();
    if (negativeNumbers.isNotEmpty) {
      var message = 'negatives not allowed ${negativeNumbers.join(',')}';
      throw FormatException(message);
    }
  }
}
