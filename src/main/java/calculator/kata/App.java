package calculator.kata;

import java.util.Scanner;

public class App {

	public static void main(final String[] args) {
		final Scanner scanner = new Scanner(System.in);

		System.out.print("Enter numbers to add: ");
		final String numbers = scanner.nextLine();

		System.out.println(String.format("=%s", StringCalculator.add(numbers)));

		scanner.close();
	}
}
