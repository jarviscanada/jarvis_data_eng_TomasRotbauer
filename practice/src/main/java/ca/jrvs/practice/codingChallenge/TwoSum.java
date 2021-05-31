package ca.jrvs.practice.codingChallenge;

/**
 * Ticket link: https://www.notion.so/jarvisdev/Two-Sum-480811fd5223471ca62c65623ce3945e
 */

public class TwoSum {

  /**
   * Complexity: O(n^2)
   * Justification: Two nested for-loops each iterating over n elements
   */
  public int[] bruteForce(int[] nums, int target) {
    int[] solution = new int[2];

    for (int i = 0; i < nums.length; i++)
      for (int j = i + 1; j < nums.length; j++)
        if (nums[i] + nums[j] == target) {
          solution[0] = i;
          solution[1] = j;
          return solution;
        }

    return solution;
  }
}
