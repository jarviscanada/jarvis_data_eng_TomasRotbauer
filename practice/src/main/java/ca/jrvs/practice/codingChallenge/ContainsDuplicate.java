package ca.jrvs.practice.codingChallenge;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Ticket link: https://www.notion.so/jarvisdev/Contains-Duplicate-1142d1a3295547d6aff717c838411f03
 */
public class ContainsDuplicate {

  /**
   * Complexity: time: O(nlogn), space: O(1)
   * Justification: Need to run quick sort
   */
  public boolean sorting(int[] nums) {
    Arrays.sort(nums);

    for (int previous = 0, i = 1; i < nums.length; i++, previous++) {
      if (nums[previous] == nums[i])
        return true;
    }

    return false;
  }

  /**
   * Complexity: time: O(n), Space: O(n)
   * Justification: Pass through the array at most once.
   */
  public boolean set(int[] nums) {
    Set<Integer> set = new HashSet<>();
    for (int num : nums)
      if (set.contains(num))
        return true;
      else
        set.add(num);
    return false;
  }
}