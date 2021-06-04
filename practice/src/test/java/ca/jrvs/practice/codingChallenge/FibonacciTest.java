package ca.jrvs.practice.codingChallenge;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class FibonacciTest {

  private int[] fibArray;
  private Fibonacci fibonacci;

  @Before
  public void setUp() {
    fibArray = new int[]{0, 1, 1, 2, 3, 5, 8, 13, 21, 34};
    fibonacci = new Fibonacci();
  }

  @Test
  public void testFibonacci() {
    for (int i = 0; i < fibArray.length; i++) {
      Assert.assertEquals(fibonacci.fibonacci(i), fibArray[i]);
    }
  }

  @Test
  public void testDynamicFibonacci() {
    for (int i = 0; i < fibArray.length; i++) {
      Assert.assertEquals(fibonacci.dynamicFibonacci(i), fibArray[i]);
    }
  }
}