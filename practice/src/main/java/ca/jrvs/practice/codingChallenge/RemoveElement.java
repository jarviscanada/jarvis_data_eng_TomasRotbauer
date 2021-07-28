package ca.jrvs.practice.codingChallenge;

/**
 * Ticket link: https://www.notion.so/jarvisdev/Remove-Element-350710d0e69b4a7b9dd0f1eb2a870ef0
 */
public class RemoveElement {

  /**
   * Complexity: O(n)
   * Justification: Pass through the array once, performing O(1) operations each iteration.
   */
  public int remove(int[] nums, int val) {
    int j = 0;

    for (int i = 0; i < nums.length; i++)
      if (nums[i] != val) {
        if (i != j)
          nums[j] = nums[i];
        j++;
      }

    return j;
  }
}
