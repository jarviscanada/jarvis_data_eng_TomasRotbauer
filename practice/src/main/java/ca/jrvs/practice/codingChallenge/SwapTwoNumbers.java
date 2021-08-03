package ca.jrvs.practice.codingChallenge;

/**
 * Ticket link: https://www.notion.so/jarvisdev/Swap-two-numbers-ce7214380180439fb7fbf4209fc8c64c
 */
public class SwapTwoNumbers {

  /**
   * Complexity: O(1)
   * Justification: Just need to swap two numbers
   */
  public int[] swapByBits(int[] nums) {
    nums[0] ^= nums[1];
    nums[1] ^= nums[0];
    nums[0] ^= nums[1];

    return nums;
  }

  /**
   * Complexity: O(1)
   * Justification: Just need to swap two numbers
   */
  public int[] swapByArithmetic(int[] nums) {
    nums[0] -= nums[1];
    nums[1] += nums[0];
    nums[0] = nums[1] - nums[0];

    return nums;
  }
}
