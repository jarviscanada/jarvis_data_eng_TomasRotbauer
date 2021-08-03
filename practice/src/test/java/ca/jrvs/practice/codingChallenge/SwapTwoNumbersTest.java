package ca.jrvs.practice.codingChallenge;

import static org.junit.Assert.*;

import org.junit.Test;

public class SwapTwoNumbersTest {

  private final SwapTwoNumbers solver = new SwapTwoNumbers();

  @Test
  public void swapByBits() {
    int[] arr = {55, 999};
    solver.swapByBits(arr);
    assertEquals(55, arr[1]);
    assertEquals(999, arr[0]);
  }

  @Test
  public void swapByArithmetic() {
    int[] arr = {55, 999};
    solver.swapByArithmetic(arr);
    assertEquals(55, arr[1]);
    assertEquals(999, arr[0]);
  }
}