package ca.jrvs.practice.codingChallenge;

import java.util.Arrays;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TwoSumTest {

  private int[][] nums;
  private int[][] sols;
  private int[] target;
  private TwoSum twoSum;

  @Before
  public void setUp() {
    nums = new int[][] {{2, 7, 11, 15},
      {3, 2, 4},
      {3, 3},
      {15, 2, 3, 5, 7, 8, 9, 10},
      {19, 5, 3, 6, 7, 0, 14, 20}};

    target = new int[]{9, 6, 6, 20, 13};

    sols = new int[][] {{0,1},
        {1,2},
        {0,1},
        {0,3},
        {3,4}};

    twoSum = new TwoSum();

  }

  @Test
  public void testBruteForce() {
    System.out.println("TEST bruteForce()");
    int[] computed;
    for (int i = 0; i < nums.length; i++) {
      System.out.print("TEST "+i);
      computed = twoSum.bruteForce(nums[i], target[i]);
      Arrays.sort(computed);
      Assert.assertArrayEquals(computed, sols[i]);
      System.out.println(" PASSED");
    }
  }

  @Test
  public void testSorting() {
    System.out.println("\nTEST sorting()");
    int[] computed;
    for (int i = 0; i < nums.length; i++) {
      computed = twoSum.sorting(nums[i], target[i]);
      Arrays.sort(computed);
      System.out.print("TEST "+i);
      Assert.assertArrayEquals(computed, sols[i]);
      System.out.println(" PASSED");
    }
  }

  @Test
  public void testLinear() {
    System.out.println("\nTEST linear()");
    int[] computed;
    for (int i = 0; i < nums.length; i++) {
      computed = twoSum.linear(nums[i], target[i]);
      Arrays.sort(computed);
      System.out.print("TEST "+i);
      Assert.assertArrayEquals(computed, sols[i]);
      System.out.println(" PASSED");
    }
  }
}