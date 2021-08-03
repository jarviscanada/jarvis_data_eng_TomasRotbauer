package ca.jrvs.practice.codingChallenge;

import static org.junit.Assert.*;

import org.junit.Test;

public class FindDuplicateNumberTest {

  private final FindDuplicateNumber solver = new FindDuplicateNumber();
  private final int[] arr1 = {1,3,4,2,2};
  private final int[] arr2 = {3,1,3,4,2};
  private final int[] arr3 = {1,1};
  private final int[] arr4 = {1,1,2};
  private final int[] arr5 = {1,6,2,2,2,3,4};

  @Test
  public void sorting() {
    assertEquals(2, solver.sorting(arr1));
    assertEquals(3, solver.sorting(arr2));
    assertEquals(1, solver.sorting(arr3));
    assertEquals(1, solver.sorting(arr4));
    assertEquals(2, solver.sorting(arr5));

  }

  @Test
  public void set() {
    assertEquals(2, solver.set(arr1));
    assertEquals(3, solver.set(arr2));
    assertEquals(1, solver.set(arr3));
    assertEquals(1, solver.set(arr4));
    assertEquals(2, solver.set(arr5));
  }
}