package ca.jrvs.practice.codingChallenge;

/**
 * Ticket link: https://www.notion.so/jarvisdev/Sample-Check-if-a-number-is-even-or-odd-a1cf504e98f0468f90bb41dec351978b
 */
public class CheckIfOdd {
  /**
   * Complexity: O(1)
   * Justification: Only one arithmetic operation is performed per any input
   */
  public String oddOrEven(int number) {
    return number % 2 == 1 ? "Odd" : "Even";
  }


}
