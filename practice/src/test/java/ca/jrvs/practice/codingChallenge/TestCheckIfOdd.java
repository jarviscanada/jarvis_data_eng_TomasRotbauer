package ca.jrvs.practice.codingChallenge;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestCheckIfOdd {

  private CheckIfOdd checkIfOdd;

  @Before
  public void setUp() {
    checkIfOdd = new CheckIfOdd();
  }

  @Test
  public void testOddOrEven() {
    Assert.assertEquals(checkIfOdd.oddOrEven(1), "Odd");
    Assert.assertEquals(checkIfOdd.oddOrEven(2), "Even");
    Assert.assertEquals(checkIfOdd.oddOrEven(-1), "Odd");
    Assert.assertEquals(checkIfOdd.oddOrEven(-2), "Even");
    Assert.assertEquals(checkIfOdd.oddOrEven(0), "Even");
  }

  @Test
  public void testBitwiseOddOrEven() {
    Assert.assertEquals(checkIfOdd.bitwiseOddOrEven(1), "Odd");
    Assert.assertEquals(checkIfOdd.bitwiseOddOrEven(2), "Even");
    Assert.assertEquals(checkIfOdd.bitwiseOddOrEven(-1), "Odd");
    Assert.assertEquals(checkIfOdd.bitwiseOddOrEven(-2), "Even");
    Assert.assertEquals(checkIfOdd.bitwiseOddOrEven(0), "Even");
  }
}
