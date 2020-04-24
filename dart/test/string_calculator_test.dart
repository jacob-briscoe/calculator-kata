import 'package:dart/string_calculator.dart';
import 'package:test/test.dart';

void main() {
  test('calculate with empty string', () {
    expect(add(''), 0);
  });

  test('calculate with 1 string', () {
    expect(add('1'), 1);
  });

  test('calculate with 2 strings', () {
    expect(add('1,1'), 2);
  });

  test('calculate with n strings', () {
    var numbers = 5;
    expect(add(List.generate(numbers, (int index) => 1).join(',')), numbers);
  });

  test('calculate with newline delims', () {
    expect(add('1\n2,3'), 6);
  });

  test('calculate with var delims', () {
    expect(add('//;\n1;2;3'), 6);
  });

  test('calculate with negative numbers throws exception', () {
    try {
      add('//;\n1;-2;3');
      fail('expected this to fail because there is a negative number');
    } on FormatException catch (fe) {
      expect(fe.message, 'negatives not allowed -2');
    }
  });

  test('calculate with more than one negative numbers throws exception', () {
    try {
      add('//;\n1;-2;-3');
      fail('expected this to fail because there is a negative number');
    } on FormatException catch (fe) {
      expect(fe.message, 'negatives not allowed -2,-3');
    }
  });

  test('numbers greater than 1000 ignored', () {
    expect(add('//;\n1;1;1001'), 2);
  });

  test('delims of any length', () {
    expect(add('//;;;\n1;;;1;;;1000'), 1002);
  });
}
