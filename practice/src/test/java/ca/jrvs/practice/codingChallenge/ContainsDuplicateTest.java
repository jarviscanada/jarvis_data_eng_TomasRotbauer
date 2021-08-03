package ca.jrvs.practice.codingChallenge;

import static org.junit.Assert.*;

import org.junit.Test;

public class ContainsDuplicateTest {

  private ContainsDuplicate solver = new ContainsDuplicate();
  private int[] arr1 = {1,1};
  private int[] arr2 = {0};
  private int[] arr3 = {0,1,1,1,3,4,5,6,7,7};
  private int[] arr4 = {1,2,3,1};
  private int[] arr5 = {1,2,3,4};
  private int[] arr6 = {1,1,1,3,3,4,3,2,4,2};

  @Test
  public void sorting() {
    assertTrue(solver.sorting(arr1));
    assertFalse(solver.sorting(arr2));
    assertTrue(solver.sorting(arr3));
    assertTrue(solver.sorting(arr4));
    assertFalse(solver.sorting(arr5));
    assertTrue(solver.sorting(arr6));
  }

  @Test
  public void set() {
    assertTrue(solver.set(arr1));
    assertFalse(solver.set(arr2));
    assertTrue(solver.set(arr3));
    assertTrue(solver.set(arr4));
    assertFalse(solver.set(arr5));
    assertTrue(solver.set(arr6));
  }
}