package ca.jrvs.practice.codingChallenge;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Ticket link: https://www.notion.so/jarvisdev/Find-the-Duplicate-Number-d25d8eeb4fc249e1b6a5150b033c6e99
 */
public class FindDuplicateNumber {

  /**
   * Complexity: time = O(nlogn), space = O(1)
   * Justification: Sorting is bottleneck, uses O(nlogn) time.
   */
  public int sorting(int[] nums) {
    Arrays.sort(nums);

    int previous = 0;
    for (int num : nums)
      if (num == previous)
        return num;
      else
        previous = num;

    return -1;
  }

  /**
   * Complexity: time = O(n), space = O(n)
   * Justification: Pass through the array only once - O(n), storing each element in a set - O(1).
   */
  public int set(int[] nums) {
    Set<Integer> set = new HashSet<>();

    for (int num : nums)
      if (set.contains(num))
        return num;
      else
        set.add(num);

    return -1;
  }
}
