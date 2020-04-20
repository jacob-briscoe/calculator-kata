package briscoe.kata.calculator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class StringCalculatorSpec {

    @BeforeEach
    public void setup() {
        StringCalculator.reset();
    }

    @Test
    public void addNullString() {
        assertEquals(0, StringCalculator.add(null), () -> "null input should result in 0");
    }

    @Test
    public void addEmptyString() {
        assertEquals(0, StringCalculator.add(""), () -> "empty input should result in 0");
    }

    @Test
    public void addSingle() {
        assertEquals(1, StringCalculator.add("1"), () -> "single should result in 1");
    }

    @Test
    public void addNumbers() {
        assertEquals(3, StringCalculator.add("1,1,1"), () -> "should result in 3");
    }

    @Test
    public void addNumbersWithNewLineDelimiters() {
        assertEquals(3, StringCalculator.add("1\n1\n1"), () -> "should result in 3");
    }

    @Test
    public void addNumbersWithMixedDelimiters() {
        assertEquals(3, StringCalculator.add("1,1\n1"), () -> "should result in 3");
    }

    @Test
    public void addNumbersWithCustomDelimiter() {
        assertEquals(3, StringCalculator.add("//;\n1;1;1"), () -> "should result in 3");
    }

    @Test
    public void negativeNumbersNotAllowed(){
        try {
            StringCalculator.add("1,-1");
            fail("Expected add with negatives to fail.");
        } catch (final NumberFormatException numberFormatException) {
            assertEquals(String.format("%s-1", StringCalculator.NEGATIVES_NOT_ALLOWED_MSG), numberFormatException.getMessage());
        }
    }

    @Test
    public void calledCountIncrements() {
        var addTimes = 5;
        for (var i = 0; i < addTimes; i++) {
            StringCalculator.add("1,1");
        }

        assertEquals(addTimes, StringCalculator.getAddCallCount(), () -> String.format("should have been called %s times", addTimes));
    }

    @Test
    public void largeNumbersExcluded() {
        assertEquals(2, StringCalculator.add("//;\n1;1001;1"), () -> "should result in 2");
    }

    @Test
    public void delimitersOfAnyLength() {
        assertEquals(1002, StringCalculator.add("//;;;\n1;;;1000;;;1"), () -> "should result in 1002");
    }

}
