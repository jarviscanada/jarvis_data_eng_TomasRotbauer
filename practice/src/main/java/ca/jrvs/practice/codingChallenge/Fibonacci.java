package ca.jrvs.practice.codingChallenge;

/**
 * Ticket link: https://www.notion.so/jarvisdev/Fibonacci-Number-Climbing-Stairs-538c07df5d7440f3941dfcf204a7bb83
 */
public class Fibonacci {

  /**
   * Complexity O(2^n)
   * Each iteration splits into two more, and the tree height is n
   */
  public int fibonacci(int n) {
    if (n <= 0)
      return 0;
    else if (n == 1)
      return 1;

    return fibonacci(n - 1) + fibonacci(n - 2);
  }

}
