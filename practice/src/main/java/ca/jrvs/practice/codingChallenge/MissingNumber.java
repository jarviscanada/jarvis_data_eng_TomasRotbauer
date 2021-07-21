package ca.jrvs.practice.codingChallenge;

import java.util.HashSet;
import java.util.Set;

/**
 * Ticket link: https://www.notion.so/jarvisdev/Missing-Number-5499a89010c14dbdab766d86d53ac746
 */
public class MissingNumber {

  /**
   * Complexity: O(n)
   * Justification: Only need to go through nums once, performing two arithmetic operations each iteration.
   */
  public int FindBySumming(int[] nums) {
    int expectedSum = nums.length;
    int actualSum = 0;

    for (int i = 0; i < nums.length; i++) {
      expectedSum += i;
      actualSum += nums[i];
    }

    return expectedSum - actualSum;
  }

  /**
   * Complexity: O(n)
   * Justification: Only need to go through nums once, performing two arithmetic operations each iteration.
   */
  public int FindUsingSet(int[] nums) {
    Set<Integer> set = new HashSet<>();

    for (int num : nums)
      set.add(num);

    for (int i = 0; i <= nums.length; i++)
      if (!set.contains(i))
        return i;

    return nums.length + 1;
  }

  /**
   * Complexity: O(n)
   * Justification: Only need to go through nums once, performing two arithmetic operations each iteration.
   */
  public int FindWithGaussFormula(int[] nums) {
    int expectedSum = (nums.length * nums.length + nums.length) / 2;
    int actualSum = 0;

    for (int num : nums)
      actualSum += num;

    return expectedSum - actualSum;
  }
}
