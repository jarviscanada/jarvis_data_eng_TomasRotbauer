package ca.jrvs.practice.codingChallenge;

/**
 * Ticket link: https://www.notion.so/jarvisdev/Duplicates-from-Sorted-Array-cc4ac0a1af5645368cf683f665847d9b
 */
public class RemoveDuplicates {

  /**
   * Complexity: O(n)
   * Justification: Pass through nums once, only performing O(1) operations each iteration.
   */
  public int twoPointers(int[] nums) {
    if(nums == null || nums.length == 0)
      return 0;
    else if(nums.length == 1)
      return 1;

    int previous = nums[0];
    int i = 1, j = 1;

    for (i = 1; i < nums.length; i++) {
      if (nums[i] != previous) {
        nums[j] = nums[i];
        j++;
      }
      previous = nums[i];
    }
    return j;
  }
}
