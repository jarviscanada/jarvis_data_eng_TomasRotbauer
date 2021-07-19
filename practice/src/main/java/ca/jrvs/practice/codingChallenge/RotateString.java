package ca.jrvs.practice.codingChallenge;

/**
 * Ticket link: https://www.notion.so/jarvisdev/Rotate-String-7383d8eaff9e43d09d6942f4d6ce8bdd
 */
public class RotateString {

  /**
   * Complexity: O(n)
   * Justification: String.contains() is O(n) (need to iterate through the string once)
   */
  public boolean simpleCheck(String s, String goal) {

    if (s == null || goal == null)
      return false;
    if (s.length() != goal.length())
      return false;

    return (s+s).contains(goal);
  }
}
