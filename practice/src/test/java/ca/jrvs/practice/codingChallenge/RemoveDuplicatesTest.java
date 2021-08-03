package ca.jrvs.practice.codingChallenge;

import static org.junit.Assert.*;

import org.junit.Test;

public class RemoveDuplicatesTest {

  private final RemoveDuplicates solver = new RemoveDuplicates();
  private final int[] arr1 = null;
  private final int[] arr2 = {};
  private final int[] arr3 = {0};
  private final int[] arr4 = {1,1};
  private final int[] arr5 = {0,1};
  private final int[] arr6 = {0,1,2,3,4,5,6,7,8,9,10};
  private final int[] arr7 = {0,1,1,1,2,3,4,5,5,5,6,7,8,9,9,9};

  @Test
  public void twoPointers() {
    assertEquals(0, solver.twoPointers(arr1));
    assertEquals(0, solver.twoPointers(arr2));
    assertEquals(1, solver.twoPointers(arr3));
    assertEquals(1, solver.twoPointers(arr4));
    assertEquals(1, arr4[0]);
    assertEquals(2, solver.twoPointers(arr5));
    for (int i = 0; i < 2; i++)
      assertEquals(i, arr5[i]);
    assertEquals(11, solver.twoPointers(arr6));
    for (int i = 0; i < 11; i++)
      assertEquals(i, arr6[i]);
    assertEquals(10, solver.twoPointers(arr7));
    for (int i = 0; i < 10; i++)
      assertEquals(i, arr7[i]);
  }
}