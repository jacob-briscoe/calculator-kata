import 'package:dart/CalculatorInput.dart';

int add(String input) {
  if (input.isEmpty) return 0;

  return CalculatorInput.from(input)
      .parse()
      .reduce((value, element) => value + element);
}
