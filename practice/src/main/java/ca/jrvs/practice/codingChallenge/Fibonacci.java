package ca.jrvs.practice.codingChallenge;

/**
 * Ticket link: https://www.notion.so/jarvisdev/Fibonacci-Number-Climbing-Stairs-538c07df5d7440f3941dfcf204a7bb83
 */
public class Fibonacci {

  /**
   * Complexity O(2^n) Justification: each iteration splits into two more, and the tree height is n
   */
  public int fibonacci(int n) {
    if (n <= 0) {
      return 0;
    } else if (n == 1) {
      return 1;
    }

    return fibonacci(n - 1) + fibonacci(n - 2);
  }

  /**
   * Complexity: O(n) Justification: Only one for-loop with n number of iterations needed.
   */
  public int dynamicFibonacci(int n) {
    if (n <= 0) {
      return 0;
    }

    int[] memo = new int[n + 1];
    memo[0] = 0;
    memo[1] = 1;

    for (int i = 1; i < n; i++) {
      memo[i + 1] = memo[i] + memo[i - 1];
    }

    return memo[n];
  }

}
