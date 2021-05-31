package ca.jrvs.practice.codingChallenge;

import java.util.Arrays;
import java.util.HashMap;

/**
 * Ticket link: https://www.notion.so/jarvisdev/Two-Sum-480811fd5223471ca62c65623ce3945e
 */

public class TwoSum {

  public static void main(String[] args) {
    TwoSum a = new TwoSum();
    System.out.println(Arrays.toString(a.sorting(new int[]{3,3}, 6)));
  }

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

  /**
   * Complexity: O(nlog(n))
   * Justification: Sorting = O(nlog(n)) and for each element, perform binary search
   * (i.e. n * log(n)). The final step is O(2*n) < O(nlog(n))
   */
  public int[] sorting(int[] nums, int target) {
    int[] solution = new int[2];
    int[] numsSorted = Arrays.copyOf(nums, nums.length);
    Arrays.sort(numsSorted);

    int j;
    for (int i = 0; i < nums.length; i++) {
      j = Arrays.binarySearch(numsSorted, i+1, nums.length, target-numsSorted[i]);
      if (j > -1) {
        solution[0] = i;
        solution[1] = j;
        break;
      }
    }

    for (int i = 0; i < 2; i++)
      for (j = 0; j < nums.length; j++)
        if (nums[j] == numsSorted[solution[i]]) {
          if (i == 1 && solution[0] == j)
            continue;
          solution[i] = j;
          break;
        }

    return solution;
  }

  /**
   * Complexity: O(n)
   * Justification: O(n) to create hash map, O(n) to find the sum pair.
   */
  public int[] linear(int[] nums, int target) {
    HashMap<Integer, Integer> map = new HashMap<>();
    int[] solution = {-1,-1};

    Arrays.stream(nums).forEach(num -> map.merge(num, 1, Integer::sum));

    int first = 0, second = 0;
    for (int num : nums) {
      map.put(num, map.get(num) - 1);
      if (map.get(target-num) != null && map.get(target-num) > 0) {
        first = num;
        second = target - num;
        break;
      }
    }

    for (int i = 0; i < nums.length && (solution[0] == -1 || solution[1] == -1); i++) {
      if (first == nums[i] && solution[0] == -1)
        solution[0] = i;
      else if (second == nums[i] && solution[1] == -1)
        solution[1] = i;
    }

    return solution;
  }
}
