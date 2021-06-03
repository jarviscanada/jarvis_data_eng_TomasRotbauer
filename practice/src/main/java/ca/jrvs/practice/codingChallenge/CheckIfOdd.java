package ca.jrvs.practice.codingChallenge;

/**
 * Ticket link: https://www.notion.so/jarvisdev/Sample-Check-if-a-number-is-even-or-odd-a1cf504e98f0468f90bb41dec351978b
 */
public class CheckIfOdd {

  /**
   * Complexity: O(1) Justification: Only one arithmetic operation is performed per any input
   */
  public String oddOrEven(int number) {
    return number % 2 == 0 ? "Even" : "Odd";
  }

  /**
   * Complexity: O(1) Justification: Only one operation is performed, and the computation itself
   * only takes one CPU instruction.
   */
  public String bitwiseOddOrEven(int number) {
    return (1 & number) == 1 ? "Odd" : "Even";
  }
}
