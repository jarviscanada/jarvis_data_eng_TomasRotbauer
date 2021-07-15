package ca.jrvs.practice.codingChallenge;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Ticket link: https://www.notion.so/jarvisdev/Find-Largest-Smallest-511d871f1cc4469b9025aa478aadc3b9
 */
public class FindLargestSmallest {

  /**
   * Complexity: O(n)
   * Justification: Need to check all n numbers. There is no other way.
   */
  public int forLoop(int[] arr, boolean largest) {
    if (arr.length == 0)
      throw new IllegalArgumentException("Array cannot be empty");

    int maxmin = arr[0];
    for (int num : arr)
      if (largest && maxmin < num)
        maxmin = num;
      else if (!largest && maxmin > num)
        maxmin = num;

    return maxmin;
  }

  /**
   * Complexity: O(n)
   * Justification: Need to check all n numbers. There is no other way.
   */
  public int streamAPI(int[] arr, boolean largest) {
    if (arr.length == 0)
      throw new IllegalArgumentException("Array cannot be empty");
    if (largest)
      return Arrays.stream(arr).max().getAsInt();
    else
      return Arrays.stream(arr).min().getAsInt();
  }

  /**
   * Complexity: O(n)
   * Justification: Need to check all n numbers. There is no other way.
   */
  public Integer javaAPI(List<Integer> arr, boolean largest) {
    if (arr.size() == 0)
      throw new IllegalArgumentException("Array cannot be empty");

    if (largest)
      return Collections.max(arr);
    else
      return Collections.min(arr);
  }
}
